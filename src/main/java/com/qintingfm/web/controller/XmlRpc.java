package com.qintingfm.web.controller;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("/xmlrpc")
@Slf4j
public class XmlRpc {
    @RequestMapping(value = "/xmlrpcserver" ,method = {RequestMethod.POST,RequestMethod.GET,RequestMethod.OPTIONS})
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
}
