package com.qintingfm.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qintingfm.web.common.AjaxDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class WebLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    ObjectMapper objectMapper;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("loginSuccess");
        AjaxDto ajaxDto=new AjaxDto();
        ajaxDto.setLink("/");
        ajaxDto.setMessage("登录成功");
//        ObjectMapper objectMapper = new ObjectMapper();
        response.setCharacterEncoding("utf-8");
        response.getWriter().println(objectMapper.writeValueAsString(ajaxDto));
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        log.info("loginSuccess");
        AjaxDto ajaxDto=new AjaxDto();
        ajaxDto.setLink("/");
        ajaxDto.setMessage("登录成功");
//        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().println(objectMapper.writeValueAsString(ajaxDto));
    }
}
