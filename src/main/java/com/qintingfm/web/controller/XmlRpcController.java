package com.qintingfm.web.controller;

import com.qintingfm.web.jpa.CategoryJpa;
import com.qintingfm.web.service.AppUserDetailsServiceImpl;
import com.qintingfm.web.service.BlogService;
import com.qintingfm.web.service.MetaWebLogServer;
import com.qintingfm.web.storage.Manager;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlrpc.XmlRpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.xml.sax.SAXException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author guliuzhong
 */
@Controller
@Slf4j
public class XmlRpcController {
    AppUserDetailsServiceImpl appUserDetailsService;
    PasswordEncoder passwordEncoder;
    CategoryJpa categoryJpa;
    Manager manager;

    BlogService blogServer;

    MetaWebLogServer metaWebLogServer;

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

    @Autowired
    public void setMetaWebLogServer(MetaWebLogServer metaWebLogServer) {
        this.metaWebLogServer = metaWebLogServer;
    }

    @RequestMapping(value = {"/xmlrpc/server", "xmlrpc.php"}, method = {RequestMethod.POST, RequestMethod.OPTIONS}, produces = {"application/xml;charset=utf-8", "text/xml"}, consumes = {"application/xml", "text/xml"})
    @ResponseBody
    public String xmlRpcServer(@Autowired HttpServletRequest request, @Autowired HttpServletResponse response) throws IOException, XmlRpcException {
        ServletInputStream inputStream = request.getInputStream();
        response.setContentType("application/xml");
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
            try {
                metaWebLogServer.invoke(byteInputStream, response.getOutputStream());
            } catch (SAXException e) {
                e.printStackTrace();
            }


//            String methodName = xmlRequestParser.getMethodName();
//            List params = xmlRequestParser.getParams();
//            Optional<UserDetails> login=Optional.empty();
//            if (params != null) {
//                String username = xmlRequestParser.getParams().get(1).toString();
//                String password = xmlRequestParser.getParams().get(2).toString();
//                if (BLOGGER_DELETE_POST.equals(methodName)) {
//                    username = xmlRequestParser.getParams().get(2).toString();
//                    password = xmlRequestParser.getParams().get(3).toString();
//                }
//                log.info("用户名{}密码{}", username, password);
//                login = login(username, password);
//            }
//            if(noLoginMethod.contains(methodName)){
//                switch (methodName){
//                    case "system.listMethods":
//                        xmlRpcServer.response(response.getOutputStream(), listMethods());
//                        break;
//                    default:
//                }
//                return "";
//            }
//
//
//            if(!login.isPresent()){
//                xmlRpcServer.responseError(response.getOutputStream(), 1001, "登录出错");
//                return "";
//            }
//            switch (methodName) {
//                case BLOGGER_GET_USERS_BLOGS:
//                case WP_GET_USERS_BLOGS:
//                    xmlRpcServer.response(response.getOutputStream(), getUserBlog());
//                    break;
//                case META_WEBLOG_GET_CATEGORIES:
//                    xmlRpcServer.response(response.getOutputStream(), getCaterory());
//                    break;
//                case META_WEBLOG_GET_RECENT_POSTS:
//                    xmlRpcServer.response(response.getOutputStream(), getRecentPosts(xmlRequestParser));
//                    break;
//                case META_WEBLOG_NEW_POST:
//                    xmlRpcServer.response(response.getOutputStream(), newPost(xmlRequestParser));
//                    break;
//                case META_WEBLOG_EDIT_POST:
//                    xmlRpcServer.response(response.getOutputStream(), editPost(xmlRequestParser));
//                    break;
//                case META_WEBLOG_GET_POST:
//                    xmlRpcServer.response(response.getOutputStream(), getPost(xmlRequestParser));
//                    break;
//                case BLOGGER_DELETE_POST:
//                    xmlRpcServer.response(response.getOutputStream(), deletePost(xmlRequestParser));
//                    break;
//                case META_WEBLOG_NEW_MEDIA_OBJECT:
//                    xmlRpcServer.response(response.getOutputStream(), newMediaObject(xmlRequestParser));
//                    break;
//                default:
//                    xmlRpcServer.responseError(response.getOutputStream(), 1001, "服务未实现");
//            }
//        } catch (XmlRpcException e) {
//            try {
//                xmlRpcServer.responseError(response.getOutputStream(), e.code, e.getMessage());
//            } catch (SAXException ex) {
//                log.error(ex.getMessage());
//            }
//        } catch (SAXException e) {
//            try {
//                xmlRpcServer.responseError(response.getOutputStream(), 1001, e.getMessage());
//            } catch (SAXException ex) {
//                log.error(ex.getMessage());
//            }
//        } catch (ManagerException e) {
//            try {
//                xmlRpcServer.responseError(response.getOutputStream(), 1001, e.getMessage());
//            } catch (SAXException ex) {
//                log.error(ex.getMessage());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
}
