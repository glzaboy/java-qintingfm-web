package com.qintingfm.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.theme.CookieThemeResolver;
import org.springframework.web.servlet.theme.ThemeChangeInterceptor;

@Configuration
public class WebMvcConfigurerAdapter extends WebMvcConfigurationSupport {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getThemeChangeInterceptor()).addPathPatterns("/misc/changeTheme");
    }
    @Bean
    public HandlerInterceptor getThemeChangeInterceptor(){
        return new ThemeChangeInterceptor();
    }
    @Bean("themeResolver")
    public ThemeResolver getCookieThemeResolver(){
        CookieThemeResolver cookieThemeResolver = new CookieThemeResolver();
        cookieThemeResolver.setCookieMaxAge(30*86400);
        return cookieThemeResolver;
    }
}
