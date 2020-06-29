package com.qintingfm.web.controller;

import com.qintingfm.web.service.MetaWebLogServer;
import com.qintingfm.web.settings.repo.SiteSetting;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlrpc.XmlRpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 网站 XMlRPC 接口，支持 MetaWebLog
 *
 * @author guliuzhong
 * @since 1.0
 */
@Controller
@Slf4j
public class XmlRpcController extends BaseController{

    MetaWebLogServer metaWebLogServer;

    @Autowired
    public void setMetaWebLogServer(MetaWebLogServer metaWebLogServer) {
        this.metaWebLogServer = metaWebLogServer;
    }

    @RequestMapping(value = { "xmlrpc.php"}, method = {RequestMethod.OPTIONS}, produces = {"application/xml;charset=utf-8", "text/xml"}, consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<String> xmlRpcServerOption(@Autowired HttpServletRequest request, @Autowired HttpServletResponse response) throws IOException, XmlRpcException {
        return ResponseEntity.ok().header("Allow", "GET,POST,OPTIONS").header("PowerBy", "qintingfm.com").body("");
    }

    @RequestMapping(value = {"xmlrpc.php"}, method = {RequestMethod.POST}, produces = {"application/xml;charset=utf-8", "text/xml"}, consumes = {"application/xml", "text/xml"})
    @ResponseBody
    public String xmlRpcServer(@Autowired HttpServletRequest request, @Autowired HttpServletResponse response) throws IOException, XmlRpcException {
        ServletInputStream inputStream = request.getInputStream();
        response.setContentType("application/xml");
        metaWebLogServer.setRequestUrl(request.getRequestURI());
        try {
            metaWebLogServer.invoke(inputStream, response.getOutputStream());
        } catch (SAXException e) {
            log.error("MetaWebLog invoke ERROR{}", e.getMessage());
        }finally {
            metaWebLogServer.removeRequestUrl();
        }
        return "";
    }

    @RequestMapping(value = {"xmlrpc.php"}, method = {RequestMethod.GET}, produces = {"application/xml;charset=utf-8"})
    @ResponseBody
    public String xmlRpcServer() {
        SiteSetting siteSetting = getSiteSetting();
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<rsd version=\"1.0\" xmlns=\"http://archipelago.phrasewise.com/rsd\">\n" +
                "\t<service>\n" +
                "\t\t<engineName>qintingfm</engineName>\n" +
                "\t\t<engineLink>http://qintingfm.com/</engineLink>\n" +
                "\t\t<homePageLink>http://qintingfm.com/</homePageLink>\n" +
                "\t\t<apis>\n" +
                "\t\t\t<api name=\"MetaWeblog\" blogID=\"1\" preferred=\"true\" apiLink=\"" + siteSetting.getMainUrl()+ "/xmlrpc.php\" />\n" +
                "\t\t</apis>\n" +
                "\t</service>\n" +
                "</rsd>";
    }
}
