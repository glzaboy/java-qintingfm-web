package com.qintingfm.web.jpa;

import com.qintingfm.web.jpa.entity.LoginToken;
import com.qintingfm.web.jpa.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface LoginTokenJpa extends JpaRepository<LoginToken,String> {
        void deleteByUsername(String username);
}
