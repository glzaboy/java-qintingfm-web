package com.qintingfm.web.jpa;

import com.qintingfm.web.jpa.entity.User;
import com.qintingfm.web.jpa.entity.UserRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 用户注册jpa
 *
 * @author guliuzhong
 */
@Component
public interface UserRegisterJpa extends JpaRepository<UserRegister, Long> {
}
