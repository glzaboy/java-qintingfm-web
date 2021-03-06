package com.qintingfm.web.service;

import com.qintingfm.web.jpa.RoleJpa;
import com.qintingfm.web.jpa.UserJpa;
import com.qintingfm.web.jpa.entity.Role;
import com.qintingfm.web.jpa.entity.User;
import com.qintingfm.web.pojo.WebUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author guliuzhong
 */
@Service
public class AppUserDetailsServiceImpl implements UserDetailsService {
    UserJpa userJpa;
    RoleJpa roleJpa;

    @Autowired
    public void setUserJpa(UserJpa userJpa) {
        this.userJpa = userJpa;
    }

    @Autowired
    public void setRoleJpa(RoleJpa roleJpa) {
        this.roleJpa = roleJpa;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if (userName == null || userName.isEmpty()) {
            throw new UsernameNotFoundException("用户不存在");
        }
//        User user=new User();
        Optional<User> one = userJpa.findByUsername(userName);
        if (!one.isPresent()) {
            throw new UsernameNotFoundException("用户不存在");
            //return null;
        }
        User user1 = one.get();

        WebUserDetails webUserDetails = new WebUserDetails();
        webUserDetails.setUsername(user1.getUsername());
        webUserDetails.setPassword(user1.getPassword());
        webUserDetails.setAccountNonExpired(user1.isAccountNonExpired());
        webUserDetails.setAccountNonLocked(user1.isAccountNonLocked());
        webUserDetails.setCredentialsNonExpired(user1.isCredentialsNonExpired());
        webUserDetails.setEnabled(user1.isEnabled());
        webUserDetails.setUserId(user1.getId());
        Role role = new Role();
        role.setUserId(user1.getId());
        List<Role> all = roleJpa.findAll(Example.of(role));

        Collection<GrantedAuthority> grantedAuthorities = new LinkedList<>(all);

        webUserDetails.setAuthorities(grantedAuthorities);
        return webUserDetails;
    }
}
