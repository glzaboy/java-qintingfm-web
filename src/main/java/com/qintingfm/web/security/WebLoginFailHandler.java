package com.qintingfm.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qintingfm.web.common.AjaxDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class WebLoginFailHandler implements AuthenticationFailureHandler {
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.info("loginFail");
        AjaxDto ajaxDto=new AjaxDto();
        ajaxDto.setMessage("登录失败"+e.getLocalizedMessage());
        ajaxDto.setAutoHide("1");
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.getWriter().println(objectMapper.writeValueAsString(ajaxDto));
    }
}
