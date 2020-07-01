package com.qintingfm.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qintingfm.web.common.AjaxDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author guliuzhong
 */
@Component
@Slf4j
public class WebLoginFailHandler implements AuthenticationFailureHandler {

    ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        log.info("loginFail");
        AjaxDto ajaxDto = new AjaxDto();
        ajaxDto.setMessage("登录失败" + e.getLocalizedMessage());
        ajaxDto.setAutoHide("1");
        ajaxDto.setAutoJump(3);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.getWriter().println(objectMapper.writeValueAsString(ajaxDto));
    }
}
