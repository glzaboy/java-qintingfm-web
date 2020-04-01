package com.qintingfm.web.service;

import com.qintingfm.web.common.AjaxDto;
import com.qintingfm.web.jpa.entity.Blog;
import com.qintingfm.web.jpa.entity.BlogCont;
import com.qintingfm.web.jpa.entity.Category;
import com.qintingfm.web.service.xmlrpc.RpcController;
import com.qintingfm.web.service.xmlrpc.StreamConfig;
import com.qintingfm.web.storage.Manager;
import com.qintingfm.web.storage.ManagerException;
import com.qintingfm.web.storage.StorageObject;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.parser.XmlRpcRequestParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.xml.sax.SAXException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author guliuzhong
 */
@Service
public class MetaWebLogServer extends XmlRpcServer {
    public static final List<String> NO_LOGIN_METHOD = Stream.of("system.listMethods").collect(Collectors.toList());
    final Map<String, String> methodMap = new HashMap<>();
    AppUserDetailsServiceImpl appUserDetailsService;
    PasswordEncoder passwordEncoder;
    CategoryService categoryService;
    Manager manager;
    BlogService blogServer;

    public MetaWebLogServer() {
        super();
        setRpcController(new RpcController());
        setStreamConfig(new StreamConfig());
        methodMap.put("blogger.getUsersBlogs", "getUsersBlogs");
        methodMap.put("wp.getUsersBlogs", "getUsersBlogs");
        methodMap.put("blogger.deletePost", "deletePost");
        methodMap.put("metaWeblog.getCategories", "getCategories");
        methodMap.put("metaWeblog.getRecentPosts", "getRecentPosts");
        methodMap.put("metaWeblog.newPost", "newPost");
        methodMap.put("metaWeblog.editPost", "editPost");
        methodMap.put("metaWeblog.getPost", "getPost");
        methodMap.put("metaWeblog.newMediaObject", "newMediaObject");
        methodMap.put("wp.newCategory", "newCategory");
        methodMap.put("system.listMethods", "listMethods");

    }

    @Autowired
    public void setAppUserDetailsService(AppUserDetailsServiceImpl appUserDetailsService) {
        this.appUserDetailsService = appUserDetailsService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Autowired
    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @Autowired
    public void setBlogServer(BlogService blogServer) {
        this.blogServer = blogServer;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @Transactional(rollbackFor = Exception.class)
    public void invoke(InputStream inputStream, OutputStream outputStream) throws XmlRpcException, SAXException, IOException {
        XmlRpcRequestParser xmlRequestParser = getXmlRequestParser(inputStream);
        String methodName = xmlRequestParser.getMethodName();
        Class<? extends MetaWebLogServer> aClass = this.getClass();
        Optional<Map.Entry<String, String>> first = methodMap.entrySet().stream().filter(item -> methodName.equals(item.getKey())).findFirst();

        first.ifPresent(findMethod -> {
            try {
                if (NO_LOGIN_METHOD.stream().filter(item -> {
                    return item.equals(findMethod.getKey());
                }).count() == 0) {
                    //需要登录
                    List params = xmlRequestParser.getParams();
                    if (params == null) {
                        responseError(outputStream, 403, "无权限调用");
                    }
                    if (params != null) {
                        String username = xmlRequestParser.getParams().get(1).toString();
                        String password = xmlRequestParser.getParams().get(2).toString();
                        if ("blogger.deletePost".equals(methodName)) {
                            username = xmlRequestParser.getParams().get(2).toString();
                            password = xmlRequestParser.getParams().get(3).toString();
                        }
                        Optional<UserDetails> login = login(username, password);
                        if (!login.isPresent()) {
                            responseError(outputStream, 403, "无权限执行");
                            return;
                        }
                        Method method = aClass.getDeclaredMethod(findMethod.getValue(), XmlRpcRequestParser.class, UserDetails.class);
                        Object invoke = method.invoke(this, xmlRequestParser, login.get());
                        response(outputStream, invoke);
                    }
                } else {
                    Method method = aClass.getDeclaredMethod(findMethod.getValue(), XmlRpcRequestParser.class);
                    Object invoke = method.invoke(this, xmlRequestParser);
                    response(outputStream, invoke);
                }
            } catch (NoSuchMethodException e) {
                responseError(outputStream, 404, "服务不存在");
                return;
            } catch (IllegalAccessException e) {
                responseError(outputStream, 404, "服务执行出错 IllegalAccessException");
                return;
            } catch (InvocationTargetException e) {
                Throwable t = e.getCause();
                AjaxDto ajaxDto=new AjaxDto();
                do{
                    if( t instanceof ConstraintViolationException){
                        Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) t).getConstraintViolations();
                        logger.error(constraintViolations.toString());
                        Map<String, String> collect = constraintViolations.stream().collect(Collectors.toMap(k -> {
                            return k.getPropertyPath().toString();
                        }, i -> i.getMessage(),(v1,v2)->v1+","+v2));
                        responseError(outputStream, 500, "服务执行出错"+collect.toString());
                        return;
                    }
                }while ((t=t.getCause())!=null);
                responseError(outputStream, 500, "服务执行出错 InvocationTargetException");
                return;
            }
        });
    }

    private List<String> listMethods(XmlRpcRequestParser xmlRpcRequestParser) {
        return methodMap.entrySet().stream().map(item -> {
            return item.getKey();
        }).collect(Collectors.toList());
    }

    private Vector<Map<String, String>> getUsersBlogs(XmlRpcRequestParser xmlRpcRequestParser, UserDetails userDetails) {
        Vector<Map<String, String>> mapVector = new Vector<>();
        Map<String, String> userBlog = new HashMap<>(10);
        userBlog.put("blogid", "1");
        userBlog.put("blogName", "qintingfm");
        userBlog.put("url", ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString());
        userBlog.put("xmlrpc", ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString());
        userBlog.put("isAdmin", "1");
        mapVector.add(userBlog);
        return mapVector;
    }

    private Vector<Map<String, String>> getCategories(XmlRpcRequestParser xmlRequestParser, UserDetails userDetails) {
        Vector<Map<String, String>> mapVector = new Vector<>();
        categoryService.getAllCategory(1, 10000).stream().forEach((item) -> {
            Map<String, String> caterories = new HashMap<>(10);
            caterories.put("title", item.getTitle());
            caterories.put("categoryid", item.getCatId().toString());
            caterories.put("description", item.getDescription());
            caterories.put("rssUrl", "");
            caterories.put("htmlUrl", ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString().replaceAll("/xmlrpc[\\S]{0,}[\\.]{0,}", "/") + "blog/category/" + item.getCatId());
            mapVector.add(caterories);
        });

        return mapVector;
    }

    private Map<String, String> newMediaObject(XmlRpcRequestParser xmlRequestParser, UserDetails userDetails) throws ManagerException {
        @SuppressWarnings("unchecked")
        Map<String, Object> stringObjectMap = (Map<String, Object>) xmlRequestParser.getParams().get(3);
        Map<String, String> mediaObject = new HashMap<>(10);
        byte[] bits = (byte[]) stringObjectMap.get("bits");
        //String name = (String) stringObjectMap.get("name");
        String s = DigestUtils.md5DigestAsHex(bits);
        StorageObject put = manager.put(bits, s);
        mediaObject.put("url", put.getUrl());
        return mediaObject;
    }

    Boolean deletePost(XmlRpcRequestParser xmlRequestParser, UserDetails userDetails) {
        Integer postId = Integer.valueOf(xmlRequestParser.getParams().get(1).toString());
        blogServer.deleteBlog(postId);
        return true;
    }

    private Map<String, Object> getPost(XmlRpcRequestParser xmlRequestParser, UserDetails userDetails) {
        Map<String, Object> postMap = new HashMap<>(10);
        Integer postId = Integer.valueOf(xmlRequestParser.getParams().get(0).toString());
        Optional<Blog> blog = blogServer.getBlog(postId);
        blog.ifPresent(blog1 -> {
            postMap.put("dateCreated", blog1.getDateCreated() == null ? new Date() : blog1.getDateCreated());
            postMap.put("title", blog1.getTitle());
            postMap.put("postid", blog1.getPostId());
            postMap.put("description", blog1.getBlogCont().getCont());
            postMap.put("link", ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString().replaceAll("/xmlrpc[\\S]{0,}[\\.]{0,}", "/") + "blog/view/" + blog1.getPostId());
        });
        return postMap;
    }

    Vector<Map<String, Object>> getRecentPosts(XmlRpcRequestParser xmlRequestParser, UserDetails userDetails) {
        Vector<Map<String, Object>> mapVector = new Vector<>();
        Integer pageSize = Integer.valueOf(xmlRequestParser.getParams().get(3).toString());
        if (pageSize >= 100) {
            pageSize = 100;
        }
        if (pageSize <= 0) {
            pageSize = 50;
        }
        Page<Blog> postId = blogServer.getBlogList(null, 1, Sort.by(new Sort.Order(Sort.Direction.DESC, "postId")), pageSize);
        postId.get().forEach(item -> {
            Map<String, Object> post = new HashMap<>(10);
            post.put("dateCreated", item.getDateCreated() == null ? new Date() : item.getDateCreated());
            post.put("title", item.getTitle());
            post.put("postid", item.getPostId());
            BlogCont blogCont = item.getBlogCont();
            if (blogCont != null) {
                post.put("description", blogCont.getCont());
            } else {
                post.put("description", "");
            }
            post.put("link", ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString().replaceAll("/xmlrpc[\\S]{0,}[\\.]{0,}", "/") + "blog/view/" + item.getPostId());
            mapVector.add(post);
        });
        return mapVector;

    }

    String editPost(XmlRpcRequestParser xmlRequestParser, UserDetails userDetails) {
        @SuppressWarnings("unchecked")
        HashMap<String, Object> stringObjectHashMap = (HashMap<String, Object>) xmlRequestParser.getParams().get(3);
        Optional<Blog> blog1 = blogServer.getBlog(Integer.valueOf(xmlRequestParser.getParams().get(0).toString()));
        blog1.ifPresent(blog -> {
            blog.setTitle(stringObjectHashMap.get("title").toString());
            blog.getBlogCont().setCont(stringObjectHashMap.get("description").toString());
            if (stringObjectHashMap.get("dateCreated") != null) {
                Date dateCreated = (Date) stringObjectHashMap.get("dateCreated");
                blog.setDateCreated(dateCreated);
            }
            if (stringObjectHashMap.get("categories") != null) {
                Object[] categories = (Object[]) stringObjectHashMap.get("categories");
                List<String> collect = Stream.of(categories).map(item -> {
                    return (String) item;
                }).collect(Collectors.toList());
                List<Category> category = categoryService.getCategory(collect);
                blog.setBlogCategory(category);
            }
            blogServer.save(blog);
        });
        if (!blog1.isPresent()) {
            Blog blog = new Blog();
            blog.setTitle(stringObjectHashMap.get("title").toString());
            BlogCont blogCont = new BlogCont();
            blogCont.setCont(stringObjectHashMap.get("description").toString());
            blog.setBlogCont(blogCont);
            if (stringObjectHashMap.get("dateCreated") != null) {
                Date dateCreated = (Date) stringObjectHashMap.get("dateCreated");
                blog.setDateCreated(dateCreated);
            }
            if (stringObjectHashMap.get("categories") != null) {
                Object[] categories = (Object[]) stringObjectHashMap.get("categories");
                List<String> collect = Stream.of(categories).map(item -> {
                    return (String) item;
                }).collect(Collectors.toList());
                List<Category> category = categoryService.getCategory(collect);
                blog.setBlogCategory(category);
            }
            blogServer.save(blog);
        }
        return "";
    }

    String newPost(XmlRpcRequestParser xmlRequestParser, UserDetails userDetails) {
        @SuppressWarnings("unchecked")
        HashMap<String, Object> stringObjectHashMap = (HashMap<String, Object>) xmlRequestParser.getParams().get(3);
        Blog blog = new Blog();
        blog.setTitle(stringObjectHashMap.get("title").toString());
        BlogCont blogCont = new BlogCont();
        blogCont.setCont(stringObjectHashMap.get("description").toString());
        blog.setBlogCont(blogCont);
        if (stringObjectHashMap.get("dateCreated") != null) {
            Date dateCreated = (Date) stringObjectHashMap.get("dateCreated");
            blog.setDateCreated(dateCreated);
        }
        if (stringObjectHashMap.get("categories") != null) {
            Object[] categories = (Object[]) stringObjectHashMap.get("categories");
            List<String> collect = Stream.of(categories).map(item -> (String) item).collect(Collectors.toList());
            List<Category> category = categoryService.getCategory(collect);
            blog.setBlogCategory(category);
        }
        Blog save = blogServer.save(blog);
        return save.getPostId().toString();
    }


    private Optional<UserDetails> login(String username, String password) {
        try {
            UserDetails userDetails = appUserDetailsService.loadUserByUsername(username);
            if (userDetails == null) {
                return Optional.empty();
            }
            boolean matches = passwordEncoder.matches(password, userDetails.getPassword());
            if (matches) {
                return Optional.of(userDetails);
            }
        } catch (UsernameNotFoundException exception) {
            return Optional.empty();
        }
        return Optional.empty();
    }
}
