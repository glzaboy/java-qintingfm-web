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
    private Function<HttpServletRequest, Map<String, String>> resolveHiddenInputs = request -> Collections
            .emptyMap();

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public ModelAndView loginPage(ModelAndView modelAndView, @Autowired HttpServletRequest request){
//        ApiOutDto<BingBGImage> bingBGImage = bingService.getBingBGImage();
//        if(bingBGImage.isSuccess()){
//            BingBGImage.Image image = bingBGImage.getData().getImageList().get(0);
//            modelAndView.addObject("image",image);
//        }
        BingBGImage image = bingImageService.getImage();
        modelAndView.addObject("image",image.getImageList().get(0));
        for (Map.Entry<String, String> input : this.resolveHiddenInputs.apply(request).entrySet()) {
            log.info(input.getKey()+input.getValue());
//            sb.append("<input name=\"").append(input.getKey()).append("\" type=\"hidden\" value=\"").append(input.getValue()).append("\" />\n");
        }
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
