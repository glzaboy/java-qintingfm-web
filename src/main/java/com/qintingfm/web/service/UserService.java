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
public class UserService {
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

    public User getUser(String userName)  {
        if (userName == null || userName.isEmpty()) {
            return null;
        }
        Optional<User> one = userJpa.findByUsername(userName);
        if (!one.isPresent()) {
            return null;
        }
        return one.get();
    }
    public User getUser(Long userId)  {
        if (userId == null || userId==0) {
            return null;
        }
        Optional<User> one = userJpa.findById(userId);
        if (!one.isPresent()) {
            return null;
        }
        return one.get();
    }
}
