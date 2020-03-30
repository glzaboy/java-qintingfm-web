package com.qintingfm.web.controller;

import com.qintingfm.web.common.AjaxDto;
import com.qintingfm.web.pojo.BingBgImage;
import com.qintingfm.web.service.BingImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用户登录和退出
 * @author guliuzhong
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    BingImageService bingImageService;

    @Autowired
    public void setBingImageService(BingImageService bingImageService) {
        this.bingImageService = bingImageService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginPage(ModelAndView modelAndView) {
        BingBgImage image = bingImageService.getImage();
        image.getImageList();
        modelAndView.addObject("image", image.getImageList().get(0));
        modelAndView.setViewName("user/login");
        return modelAndView;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ModelAndView logout(ModelAndView modelAndView) {
        return modelAndView;
    }


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerPage(ModelAndView modelAndView) {
        BingBgImage image = bingImageService.getImage();
        image.getImageList();
        modelAndView.addObject("image", image.getImageList().get(0));
        modelAndView.setViewName("user/register");
        return modelAndView;
    }
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public AjaxDto registerPpost(ModelAndView modelAndView) {
        AjaxDto ajaxDto=new AjaxDto();
        ajaxDto.setMessage("暂不开放注册。如果有需求，直接联系我 postmaster@qintingfm.com");
        return ajaxDto;
    }
}
