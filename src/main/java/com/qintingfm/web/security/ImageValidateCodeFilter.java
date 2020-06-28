package com.qintingfm.web.security;

import com.qintingfm.web.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ImageValidateCodeFilter extends OncePerRequestFilter {
    WebLoginFailHandler webLoginFailHandler;
    @Autowired
    public void setWebLoginFailHandler(WebLoginFailHandler webLoginFailHandler) {
        this.webLoginFailHandler = webLoginFailHandler;
    }

    public ImageValidateCodeFilter() {
        super();

    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if ("/login".equals(httpServletRequest.getRequestURI()) && "POST".equals(httpServletRequest.getMethod())) {
//            throw new BusinessException("test");
            webLoginFailHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, new AuthExc("test"));
            return;
        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
    public static class  AuthExc extends AuthenticationException {


        public AuthExc(String msg, Throwable t) {
            super(msg, t);
        }

        public AuthExc(String msg) {
            super(msg);
        }
    }
}
