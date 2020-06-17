package com.qintingfm.web.controller;

import com.qintingfm.web.jpa.entity.User;
import com.qintingfm.web.pojo.WebUserDetails;
import com.qintingfm.web.service.BaseService;
import com.qintingfm.web.service.BlogService;
import com.qintingfm.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 服务基础类
 *
 * @author guliuzhong
 */
public class BaseController extends BaseService {
    UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取当前已经登录用户
     * @return
     */
    public Optional<User> getLoginUser(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        WebUserDetails principal = (WebUserDetails) authentication.getPrincipal();
        User user = userService.getUser(principal.getUsername());
        return Optional.ofNullable(user);
    }
}
