package com.qintingfm.web.security;

import com.qintingfm.web.common.exception.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码拦截器
 * @author guliuzhong
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    WebLoginFailHandler webLoginFailHandler;
    @Autowired
    public void setWebLoginFailHandler(WebLoginFailHandler webLoginFailHandler) {
        this.webLoginFailHandler = webLoginFailHandler;
    }

    public CaptchaFilter() {
        super();

    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String loginAction="/login";
        String loginMethod="POST";
        String sessionKey="captcha";
        if (loginAction.equals(httpServletRequest.getRequestURI()) && loginMethod.equals(httpServletRequest.getMethod())) {
            String captcha = (String) httpServletRequest.getSession().getAttribute("captcha");
            httpServletRequest.getSession().removeAttribute(sessionKey);
            if(captcha==null){
                webLoginFailHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, new AuthException("请通过正常方式进行登录"));
                return;
            }
            if(!captcha.equalsIgnoreCase(httpServletRequest.getParameter(sessionKey))){
                webLoginFailHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, new AuthException("验证码不正确，请重新输入"));
                return;
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
