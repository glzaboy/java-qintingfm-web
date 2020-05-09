package com.qintingfm.web.service;

import com.qintingfm.web.common.AjaxDto;
import com.qintingfm.web.controller.BlogController;
import com.qintingfm.web.jpa.entity.Blog;
import com.qintingfm.web.jpa.entity.BlogCont;
import com.qintingfm.web.jpa.entity.User;
import com.qintingfm.web.pojo.request.BlogPojo;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
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
    CategoryService categoryService;
    Manager manager;
    BlogService blogServer;

    UserService userService;

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

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void invoke(InputStream inputStream, OutputStream outputStream) throws XmlRpcException, SAXException, IOException {
        XmlRpcRequestParser xmlRequestParser = getXmlRequestParser(inputStream);
        String methodName = xmlRequestParser.getMethodName();
        Class<? extends MetaWebLogServer> aClass = this.getClass();
        Optional<Map.Entry<String, String>> first = methodMap.entrySet().stream().filter(item -> methodName.equals(item.getKey())).findFirst();

        first.ifPresent(findMethod -> {
            try {
                if (NO_LOGIN_METHOD.stream().noneMatch(item -> item.equals(findMethod.getKey()))) {
                    //需要登录
                    @SuppressWarnings("rawtypes")
                    List params = xmlRequestParser.getParams();
                    if (params == null) {
                        responseError(outputStream, 403, "无权限调用");
                    }
                    if (params != null) {
                        String userName = xmlRequestParser.getParams().get(1).toString();
                        String password = xmlRequestParser.getParams().get(2).toString();
                        if ("blogger.deletePost".equals(methodName)) {
                            userName = xmlRequestParser.getParams().get(2).toString();
                            password = xmlRequestParser.getParams().get(3).toString();
                        }
                        Optional<User> login = userService.validPassword(userName, password);
                        if (!login.isPresent()) {
                            responseError(outputStream, 403, "无权限执行");
                            return;
                        }
                        Method method = aClass.getDeclaredMethod(findMethod.getValue(), XmlRpcRequestParser.class, User.class);
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
            } catch (IllegalAccessException e) {
                responseError(outputStream, 404, "服务执行出错 IllegalAccessException");
            } catch (InvocationTargetException e) {
                Throwable t = e.getCause();
                AjaxDto ajaxDto = new AjaxDto();
                do {
                    if (t instanceof ConstraintViolationException) {
                        Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) t).getConstraintViolations();
                        logger.error(constraintViolations.toString());
                        Map<String, String> collect = constraintViolations.stream().collect(Collectors.toMap(k -> k.getPropertyPath().toString(), ConstraintViolation::getMessage, (v1, v2) -> v1 + "," + v2));
                        responseError(outputStream, 500, "服务执行出错" + collect.toString());
                        return;
                    }
                } while ((t = t.getCause()) != null);
                responseError(outputStream, 500, "服务执行出错 InvocationTargetException");
            }
        });
    }

    private List<String> listMethods(XmlRpcRequestParser xmlRpcRequestParser) {
        return new ArrayList<>(methodMap.keySet());
    }

    private ArrayList<Map<String, String>> getUsersBlogs(XmlRpcRequestParser xmlRpcRequestParser, User userDetails) {
        ArrayList<Map<String, String>> mapVector = new ArrayList<>();
        Map<String, String> userBlog = new HashMap<>(10);
        userBlog.put("blogid", "1");
        userBlog.put("blogName", "qintingfm");
        userBlog.put("url", ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString());
        userBlog.put("xmlrpc", ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString());
        userBlog.put("isAdmin", "1");
        mapVector.add(userBlog);
        return mapVector;
    }

    private ArrayList<Map<String, String>> getCategories(XmlRpcRequestParser xmlRequestParser, User userDetails) {
        ArrayList<Map<String, String>> mapVector = new ArrayList<>();
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

    private Map<String, String> newMediaObject(XmlRpcRequestParser xmlRequestParser, User userDetails) throws ManagerException {
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

    Boolean deletePost(XmlRpcRequestParser xmlRequestParser, User userDetails) {
        Integer postId = Integer.valueOf(xmlRequestParser.getParams().get(1).toString());
        blogServer.deleteBlog(postId);
        return true;
    }

    private Map<String, Object> getPost(XmlRpcRequestParser xmlRequestParser, User userDetails) {
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

    ArrayList<Map<String, Object>> getRecentPosts(XmlRpcRequestParser xmlRequestParser, User userDetails) {
        ArrayList<Map<String, Object>> mapVector = new ArrayList<>();
        int pageSize = Integer.parseInt(xmlRequestParser.getParams().get(3).toString());
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
            Method detail;
            try {
                detail = BlogController.class.getDeclaredMethod("detail", ModelAndView.class, Integer.class, Integer.class);
                String s = MvcUriComponentsBuilder.fromMethod(BlogController.class, detail, null, item.getPostId(), null).build().toString();
                post.put("link", s);
            } catch (NoSuchMethodException e) {
                logger.error("获取文章地址出错{}",e.getMessage());
            }
            mapVector.add(post);
        });
        return mapVector;

    }

    String editPost(XmlRpcRequestParser xmlRequestParser, User userDetails) {
        @SuppressWarnings("unchecked")
        HashMap<String, Object> stringObjectHashMap = (HashMap<String, Object>) xmlRequestParser.getParams().get(3);
        BlogPojo.BlogPojoBuilder builder = BlogPojo.builder();
        builder.postId(Integer.valueOf(xmlRequestParser.getParams().get(0).toString()));
        builder.title(stringObjectHashMap.get("title").toString());
        builder.cont(stringObjectHashMap.get("description").toString());
        if (stringObjectHashMap.get("dateCreated") != null) {
            builder.createDate((Date) stringObjectHashMap.get("dateCreated"));
        }
        if (stringObjectHashMap.get("categories") != null) {
            Object[] categories = (Object[]) stringObjectHashMap.get("categories");
            builder.catNames(Stream.of(categories).map(item -> (String) item).collect(Collectors.toList()));
        }
        builder.authorId(userDetails.getId());
        builder.state("publish");
        blogServer.save(builder.build());
        return "";
    }

    String newPost(XmlRpcRequestParser xmlRequestParser, User userDetails) {
        @SuppressWarnings("unchecked")
        HashMap<String, Object> stringObjectHashMap = (HashMap<String, Object>) xmlRequestParser.getParams().get(3);
        BlogPojo.BlogPojoBuilder builder = BlogPojo.builder();
        builder.title(stringObjectHashMap.get("title").toString());
        builder.cont(stringObjectHashMap.get("description").toString());
        if (stringObjectHashMap.get("dateCreated") != null) {
            builder.createDate((Date) stringObjectHashMap.get("dateCreated"));
        }
        if (stringObjectHashMap.get("categories") != null) {
            Object[] categories = (Object[]) stringObjectHashMap.get("categories");
            builder.catNames(Stream.of(categories).map(item -> (String) item).collect(Collectors.toList()));
        }
        builder.authorId(userDetails.getId());
        builder.state("publish");
        Blog save = blogServer.save(builder.build());
        return save.getPostId().toString();
    }
}
