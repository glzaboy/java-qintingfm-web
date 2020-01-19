package com.qintingfm.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("/xmlrpc")
@Slf4j
public class XmlRpc {
    @RequestMapping(value = "/xmlrpcserver" ,method = {RequestMethod.POST,RequestMethod.OPTIONS},produces = {"application/xml;charset=utf-8"})
    @ResponseBody
    public String xmlRpcServer(@Autowired HttpServletRequest  request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        byte[] bytes=new byte[1024];
        int readnum =0;
        do {
             readnum = inputStream.read(bytes, 0, 10);
             if(readnum>0){
                 byteArrayOutputStream.write(bytes,0,readnum);
             }

        }while (readnum>0);

        log.info(byteArrayOutputStream.toString());
        return "xmlrpc";
    }
    @RequestMapping(value = "/xmlrpcserver" ,method = {RequestMethod.GET},produces = {"application/xml;charset=utf-8"})
    @ResponseBody
    public String xmlRpcServer( ) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<rsd version=\"1.0\" xmlns=\"http://archipelago.phrasewise.com/rsd\">\n" +
                "\t<service>\n" +
                "\t\t<engineName>MetaWeblog</engineName>\n" +
                "\t\t<engineLink>http://qintingfm.com/</engineLink>\n" +
                "\t\t<homePageLink>http://qintingfm.com/</homePageLink>\n" +
                "\t\t<apis>\n" +
                "\t\t\t<api name=\"MetaWeblog\" blogID=\"1\" preferred=\"false\" apiLink=\""+ ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString() +"\" />\n" +
                "\t\t</apis>\n" +
                "\t</service>\n" +
                "</rsd>";
    }
}
