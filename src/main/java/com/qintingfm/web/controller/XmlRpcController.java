package com.qintingfm.web.controller;

import com.qintingfm.web.jpa.CategoryJpa;
import com.qintingfm.web.jpa.entity.Blog;
import com.qintingfm.web.jpa.entity.BlogCont;
import com.qintingfm.web.jpa.entity.Category;
import com.qintingfm.web.service.AppUserDetailsServiceImpl;
import com.qintingfm.web.service.BlogService;
import com.qintingfm.web.service.XmlRpcServer;
import com.qintingfm.web.service.xmlrpcconfig.RpcController;
import com.qintingfm.web.service.xmlrpcconfig.StreamConfig;
import com.qintingfm.web.storage.Manager;
import com.qintingfm.web.storage.ManagerException;
import com.qintingfm.web.storage.StorageObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.parser.XmlRpcRequestParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.xml.sax.SAXException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author guliuzhong
 */
@Controller
@Slf4j
public class XmlRpcController {
    public static final String BLOGGER_GET_USERS_BLOGS = "blogger.getUsersBlogs";
    public static final String META_WEBLOG_GET_CATEGORIES = "metaWeblog.getCategories";
    public static final String META_WEBLOG_GET_RECENT_POSTS = "metaWeblog.getRecentPosts";
    public static final String META_WEBLOG_NEW_POST = "metaWeblog.newPost";
    public static final String META_WEBLOG_EDIT_POST = "metaWeblog.editPost";
    public static final String META_WEBLOG_GET_POST = "metaWeblog.getPost";
    public static final String BLOGGER_DELETE_POST = "blogger.deletePost";
    public static final String META_WEBLOG_NEW_MEDIA_OBJECT = "metaWeblog.newMediaObject";
    AppUserDetailsServiceImpl appUserDetailsService;
    PasswordEncoder passwordEncoder;
    CategoryJpa categoryJpa;
    Manager manager;

    BlogService blogServer;
    @Autowired
    public void setBlogServer(BlogService blogServer) {
        this.blogServer = blogServer;
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
    public void setCategoryJpa(CategoryJpa categoryJpa) {
        this.categoryJpa = categoryJpa;
    }

    @Autowired
    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @RequestMapping(value = {"/xmlrpc/server", "xmlrpc.php"}, method = {RequestMethod.POST, RequestMethod.OPTIONS}, produces = {"application/xml;charset=utf-8", "text/xml"}, consumes = {"application/xml", "text/xml"})
    @ResponseBody
    public String xmlRpcServer(@Autowired HttpServletRequest request, @Autowired HttpServletResponse response) throws IOException, XmlRpcException {
        ServletInputStream inputStream = request.getInputStream();
        XmlRpcServer xmlRpcServer = new XmlRpcServer();
        xmlRpcServer.setRpcController(new RpcController());
        xmlRpcServer.setStreamConfig(new StreamConfig());
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int readNum;
            do {
                readNum = inputStream.read(bytes, 0, 1024);
                if (readNum > 0) {
                    byteArrayOutputStream.write(bytes, 0, readNum);
                }
            } while (readNum > 0);
            log.info(byteArrayOutputStream.toString());
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            XmlRpcRequestParser xmlRequestParser = xmlRpcServer.getXmlRequestParser(byteInputStream);
            String username = xmlRequestParser.getParams().get(1).toString();
            String password = xmlRequestParser.getParams().get(2).toString();
            if (xmlRequestParser.getMethodName() != null && BLOGGER_DELETE_POST.equals(xmlRequestParser.getMethodName())) {
                username = xmlRequestParser.getParams().get(2).toString();
                password = xmlRequestParser.getParams().get(3).toString();
            }
            log.info("用户名{}密码{}",username,password);
            Optional<UserDetails> login = login(username, password);
            if (!login.isPresent()) {
                xmlRpcServer.responseError(response.getOutputStream(), 1001, "登录出错");
                return "";
            }
            log.info(xmlRequestParser.getMethodName());
            switch (xmlRequestParser.getMethodName()) {
                case BLOGGER_GET_USERS_BLOGS:
                    xmlRpcServer.response(response.getOutputStream(), getUserBlog());
                    break;
                case META_WEBLOG_GET_CATEGORIES:
                    xmlRpcServer.response(response.getOutputStream(), getCaterory());
                    break;
                case META_WEBLOG_GET_RECENT_POSTS:
                    xmlRpcServer.response(response.getOutputStream(), getRecentPosts(xmlRequestParser));
                    break;
                case META_WEBLOG_NEW_POST:
                    xmlRpcServer.response(response.getOutputStream(), newPost(xmlRequestParser));
                    break;
                case META_WEBLOG_EDIT_POST:
                    xmlRpcServer.response(response.getOutputStream(), editPost(xmlRequestParser));
                    break;
                case META_WEBLOG_GET_POST:
                    xmlRpcServer.response(response.getOutputStream(), getPost(xmlRequestParser));
                    break;
                case BLOGGER_DELETE_POST:
                    xmlRpcServer.response(response.getOutputStream(), deletePost(xmlRequestParser));
                    break;
                case META_WEBLOG_NEW_MEDIA_OBJECT:
                    xmlRpcServer.response(response.getOutputStream(), newMediaObject(xmlRequestParser));
                    break;
                default:
                    xmlRpcServer.responseError(response.getOutputStream(), 1001, "服务未实现");
            }
        } catch (XmlRpcException e) {
            try {
                xmlRpcServer.responseError(response.getOutputStream(), e.code, e.getMessage());
            } catch (SAXException ex) {
                log.error(ex.getMessage());
            }
        } catch (SAXException e) {
            try {
                xmlRpcServer.responseError(response.getOutputStream(), 1001, e.getMessage());
            } catch (SAXException ex) {
                log.error(ex.getMessage());
            }
        } catch (ManagerException e) {
            try {
                xmlRpcServer.responseError(response.getOutputStream(), 1001, e.getMessage());
            } catch (SAXException ex) {
                log.error(ex.getMessage());
            }
        }
        return "";
    }

    private Map<String, String> newMediaObject(XmlRpcRequestParser xmlRequestParser) throws ManagerException {
        @SuppressWarnings("unchecked")
        Map<String, Object> stringObjectMap = (Map<String, Object>) xmlRequestParser.getParams().get(3);
        Map<String, String> mediaObject = new HashMap<>(10);
        byte[] bits = (byte[]) stringObjectMap.get("bits");
//        String name = (String) stringObjectMap.get("name");
        String s = DigestUtils.md5DigestAsHex(bits);
        StorageObject put = manager.put(bits, s);
        mediaObject.put("url", put.getUrl());
        return mediaObject;
    }

    @Transactional(rollbackOn = {Exception.class})
    Boolean deletePost(XmlRpcRequestParser xmlRequestParser) {
        Integer postId = Integer.valueOf(xmlRequestParser.getParams().get(1).toString());
        blogServer.deleteBlog(postId);
        return true;
    }

    private Map<String, Object> getPost(XmlRpcRequestParser xmlRequestParser) {
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

    Vector<Map<String, Object>> getRecentPosts(XmlRpcRequestParser xmlRequestParser) {
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
    @Transactional(rollbackOn = {Exception.class})
    String editPost(XmlRpcRequestParser xmlRequestParser) {
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
            blogServer.save(blog);
        }
        return "";
    }
    @Transactional(rollbackOn = {Exception.class})
    String newPost(XmlRpcRequestParser xmlRequestParser) {
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
        Blog save = blogServer.save(blog);
        return save.getPostId().toString();
    }

    private Vector<Map<String, String>> getCaterory() {
        Vector<Map<String, String>> mapVector = new Vector<>();

        List<Category> all = categoryJpa.findAll();
        all.forEach((item) -> {
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

    private Vector<Map<String, String>> getUserBlog() {
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

    @RequestMapping(value = {"/xmlrpc/server", "xmlrpc.php"}, method = {RequestMethod.GET}, produces = {"application/xml;charset=utf-8"})
    @ResponseBody
    public String xmlRpcServer() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<rsd version=\"1.0\" xmlns=\"http://archipelago.phrasewise.com/rsd\">\n" +
                "\t<service>\n" +
                "\t\t<engineName>qintingfm</engineName>\n" +
                "\t\t<engineLink>http://qintingfm.com/</engineLink>\n" +
                "\t\t<homePageLink>http://qintingfm.com/</homePageLink>\n" +
                "\t\t<apis>\n" +
                "\t\t\t<api name=\"MetaWeblog\" blogID=\"1\" preferred=\"false\" apiLink=\"" + ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString() + "\" />\n" +
                "\t\t</apis>\n" +
                "\t</service>\n" +
                "</rsd>";
    }

    private Optional<UserDetails> login(String username, String password) {
        try {
            UserDetails userDetails = appUserDetailsService.loadUserByUsername(username);
            if (userDetails == null) {
                return Optional.empty();
//                return false;
            }
            boolean matches = passwordEncoder.matches(password, userDetails.getPassword());
            if (matches) {
                return Optional.of(userDetails);
//                return true;
            }
        } catch (UsernameNotFoundException exception) {
            return Optional.empty();
//            return false;
        }
//        return false;
        return Optional.empty();
    }

}
