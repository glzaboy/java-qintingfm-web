package com.qintingfm.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qintingfm.web.common.AjaxDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出登录成功后的操作
 * <p>
 * 本应用因为自定义了页面并且使用了ajax因此 主要修改向客户端输出json。
 *
 * @author guliuzhong
 */
@Component
@Slf4j
public class WebLogoutSuccessHandler implements LogoutSuccessHandler {
    ObjectMapper objectMapper;
    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        log.info("logout");
        AjaxDto ajaxDto = new AjaxDto();
        ajaxDto.setLink("/");
        ajaxDto.setMessage(authentication.getName() + "您已经成功退出，再见。");
        ajaxDto.setAutoJump(3);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.getWriter().println(objectMapper.writeValueAsString(ajaxDto));
    }
}
