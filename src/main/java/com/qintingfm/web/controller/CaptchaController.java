package com.qintingfm.web.controller;

import com.qintingfm.web.captcha.GifCaptcha;
import com.qintingfm.web.captcha.SpecCaptcha;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author guliuzhong
 */
@Controller()
@RequestMapping("/captcha")
public class CaptchaController extends BaseController{
    @RequestMapping(value = {"showCaptcha"})
    @ResponseBody
    public void showCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sessionKey="captcha";
        /**
         * 设置请求头为输出图片类型
         */
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        GifCaptcha captcha = new GifCaptcha(130, 48);
        request.getSession().setAttribute(sessionKey,captcha.text().toLowerCase());
        captcha.out(response.getOutputStream());
    }
}
