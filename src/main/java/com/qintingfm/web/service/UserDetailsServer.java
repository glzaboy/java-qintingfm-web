package com.qintingfm.web.service;

import com.qintingfm.web.dao.Role;
import com.qintingfm.web.dao.User;
import com.qintingfm.web.jpa.RoleJpa;
import com.qintingfm.web.jpa.UserJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServer implements UserDetailsService {
    @Autowired
    UserJpa userJpa;
    @Autowired
    RoleJpa roleJpa;
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        if(userName==null || userName.isEmpty()){
            return null;
        }
        User user=new User();
        user.setUsername(userName);
        Optional<User> one = userJpa.findOne(Example.of(user));
        if(!one.isPresent()){
            return null;
        }
        User user1 = one.get();
        Role role = new Role();
        role.setUserId(user1.getId());
        user1.setAuthorities(roleJpa.findAll(Example.of(role)));
        return user1;
    }
}
