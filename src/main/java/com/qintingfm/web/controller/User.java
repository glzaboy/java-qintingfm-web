package com.qintingfm.web.controller;

import com.qintingfm.web.common.AjaxDto;
import com.qintingfm.web.pojo.BingBGImage;
import com.qintingfm.web.service.BingImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Controller
@RequestMapping("/user")
public class User {
    @Autowired
    BingImageService bingImageService;

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public ModelAndView loginPage(ModelAndView modelAndView){
//        ApiOutDto<BingBGImage> bingBGImage = bingService.getBingBGImage();
//        if(bingBGImage.isSuccess()){
//            BingBGImage.Image image = bingBGImage.getData().getImageList().get(0);
//            modelAndView.addObject("image",image);
//        }
        BingBGImage image = bingImageService.getImage();
        modelAndView.addObject("image",image.getImageList().get(0));
        modelAndView.setViewName("user/login");
        return modelAndView;
    }



    @RequestMapping(value = "/logout",method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public AjaxDto logout(){
        AjaxDto ajaxDto=new AjaxDto();
        ajaxDto.setMessage("退出成功");
        ajaxDto.setLink(MvcUriComponentsBuilder.fromMethodName(Index.class,"index").buildAndExpand().encode().toString());
        return ajaxDto;
    }
}
