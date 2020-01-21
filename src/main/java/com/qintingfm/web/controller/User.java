package com.qintingfm.web.controller;

import com.qintingfm.web.common.AjaxDto;
import com.qintingfm.web.pojo.BingBGImage;
import com.qintingfm.web.service.BingImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

@Controller
@RequestMapping("/user")
@Slf4j
public class User {
    @Autowired
    BingImageService bingImageService;
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public ModelAndView loginPage(ModelAndView modelAndView){
        BingBGImage image = bingImageService.getImage();
        modelAndView.addObject("image",image.getImageList().get(0));
        modelAndView.setViewName("user/login");
        return modelAndView;
    }



    @RequestMapping(value = "/logout",method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public ModelAndView logout(ModelAndView modelAndView){
        return modelAndView;
    }
}
