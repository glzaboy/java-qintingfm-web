package com.qintingfm.web.jpa;

import com.qintingfm.web.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 用户jpa
 *
 * @author guliuzhong
 */
@Component
public interface UserJpa extends JpaRepository<User, Long> {
    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 查找到的用户
     */
    Optional<User> findByUsername(String username);
}
