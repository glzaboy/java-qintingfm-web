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
            log.error("MetaWebLog invoke ERROR{}", e.getMessage());
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
