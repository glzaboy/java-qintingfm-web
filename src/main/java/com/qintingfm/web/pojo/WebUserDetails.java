package com.qintingfm.web.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 应用的用户鉴权服务对象
 * @author guliuzhong
 */
@Setter
@Getter
public class WebUserDetails implements UserDetails {
    private String username;
    private String password;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;
    private Integer userId;
    Collection<? extends GrantedAuthority> authorities=new ArrayList<>();
}
