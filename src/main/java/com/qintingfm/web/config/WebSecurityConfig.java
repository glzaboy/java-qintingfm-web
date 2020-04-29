package com.qintingfm.web.config;

import com.qintingfm.web.security.JpaTokenRepositoryImpl;
import com.qintingfm.web.security.WebLoginFailHandler;
import com.qintingfm.web.security.WebLoginSuccessHandler;
import com.qintingfm.web.security.WebLogoutSuccessHandler;
import com.qintingfm.web.service.AppUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

import java.util.UUID;

/**
 * @author guliuzhong
 */
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    UUID key = UUID.randomUUID();
    AppUserDetailsServiceImpl appUserDetailsService;

    @Autowired
    public void setAppUserDetailsService(AppUserDetailsServiceImpl appUserDetailsService) {
        this.appUserDetailsService = appUserDetailsService;
    }

    WebLogoutSuccessHandler webLogoutSuccessHandler;

    @Autowired
    public void setWebLogoutSuccessHandler(WebLogoutSuccessHandler webLogoutSuccessHandler) {
        this.webLogoutSuccessHandler = webLogoutSuccessHandler;
    }

    WebLoginSuccessHandler webLoginSuccessHandler;

    @Autowired
    public void setWebLoginSuccessHandler(WebLoginSuccessHandler webLoginSuccessHandler) {
        this.webLoginSuccessHandler = webLoginSuccessHandler;
    }

    WebLoginFailHandler webLoginFailHandler;

    @Autowired
    public void setWebLoginFailHandler(WebLoginFailHandler webLoginFailHandler) {
        this.webLoginFailHandler = webLoginFailHandler;
    }

    JpaTokenRepositoryImpl jpaTokenRepository;

    @Autowired
    public void setJpaTokenRepository(JpaTokenRepositoryImpl jpaTokenRepository) {
        this.jpaTokenRepository = jpaTokenRepository;
    }

    @Bean
    RememberMeServices rememberMeServices() {
        return new PersistentTokenBasedRememberMeServices(key.toString(), appUserDetailsService, jpaTokenRepository);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/user/login","/user/active/*", "/user/register", "/xmlrpc/server", "/misc/changeTheme", "/xmlrpc.php", "/", "/page/*", "/blog/**", "/robots.txt","/favicon.ico").permitAll()
                .and().authorizeRequests().anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/user/login").loginProcessingUrl("/login").
                successHandler(webLoginSuccessHandler).failureHandler(webLoginFailHandler)
                .permitAll()
                .and().logout().logoutUrl("/logout").logoutSuccessHandler(webLogoutSuccessHandler).permitAll()
                .and().rememberMe().tokenRepository(jpaTokenRepository).key(key.toString())
                .and().csrf(cs -> cs.ignoringAntMatchers("/xmlrpc/server", "/xmlrpc.php", "/user/register"));
    }

    @Bean
    PasswordEncoder loginPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(appUserDetailsService);
    }
}
