package com.qintingfm.web.controller;

import com.qintingfm.web.common.AjaxDto;
import com.qintingfm.web.jpa.entity.UserRegister;
import com.qintingfm.web.pojo.BingBgImage;
import com.qintingfm.web.pojo.request.UserRegisterPojo;
import com.qintingfm.web.service.BingImageService;
import com.qintingfm.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用户登录和退出
 *
 * @author guliuzhong
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    BingImageService bingImageService;

    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setBingImageService(BingImageService bingImageService) {
        this.bingImageService = bingImageService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginPage(ModelAndView modelAndView) {
        BingBgImage image = bingImageService.getImage();
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
        modelAndView.addObject("image", image.getImageList().get(0));
        modelAndView.setViewName("user/register");
        return modelAndView;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public AjaxDto registerPost(ModelAndView modelAndView, @RequestParam(value = "email",required = true) String email,@RequestParam(value = "userName",required = true) String userName,@RequestParam(value = "tel",required = true) String tel) {
        AjaxDto ajaxDto = new AjaxDto();
        UserRegisterPojo.UserRegisterPojoBuilder builder = UserRegisterPojo.builder();
        builder.email(email).userName(userName).tel(tel);
        UserRegister register = userService.register(builder.build());
        ajaxDto.setMessage("注册成功，我们已经发送一封邮件到您留下的邮箱，账号需要激活才能使用。");
        return ajaxDto;
    }
    @RequestMapping(value = "active/{activeKey}")
    public ModelAndView active(ModelAndView modelAndView,@PathVariable(value = "activeKey") String activeKey){

        return modelAndView;
    }
}
