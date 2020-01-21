package com.qintingfm.web.controller;

import com.qiniu.util.Auth;
import com.qintingfm.web.config.StorageConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class Index {
    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
