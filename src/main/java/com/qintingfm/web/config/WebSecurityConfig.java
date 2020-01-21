package com.qintingfm.web.config;

import com.qintingfm.web.security.*;
import com.qintingfm.web.service.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    UUID key= UUID.randomUUID();
    @Autowired
    AppUserDetailsService appUserDetailsService;
    @Autowired
    WebLogoutSuccessHandler webLogoutSuccessHandler;
    @Autowired
    WebLoginSuccessHandler webLoginSuccessHandler;
    @Autowired
    WebLoginFailHandler webLoginFailHandler;
    @Autowired
    JpaTokenRepositoryImpl jpaTokenRepository;
    @Bean
    RememberMeServices rememberMeServices(){
        return new PersistentTokenBasedRememberMeServices(key.toString(),appUserDetailsService,jpaTokenRepository);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/user/login", "/xmlrpc/xmlrpcserver","/","/page/*","/blog/**").permitAll()
                .antMatchers(HttpMethod.POST,"/xmlrpc/xmlrpcserver").permitAll()
                .and().authorizeRequests().anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/user/login").loginProcessingUrl("/login").
                successHandler(webLoginSuccessHandler).failureHandler(webLoginFailHandler)
                .permitAll()
                .and().logout().logoutUrl("/logout").logoutSuccessHandler(webLogoutSuccessHandler).permitAll()
                .and().rememberMe().tokenRepository(jpaTokenRepository).key(key.toString())
                .and().csrf(cs->{cs.ignoringAntMatchers("/xmlrpc/xmlrpcserver");});
    }

    @Bean
    PasswordEncoder loginPasswordEncoder() {
        PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return delegatingPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(appUserDetailsService);
    }
}
