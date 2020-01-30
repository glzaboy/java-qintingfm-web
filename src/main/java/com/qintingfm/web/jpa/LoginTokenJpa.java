package com.qintingfm.web.jpa;

import com.qintingfm.web.jpa.entity.LoginToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * 用户登录成功后token
 * @author guliuzhong
 */
@Component
public interface LoginTokenJpa extends JpaRepository<LoginToken,String> {
        /**
         * 删除指定用户的token
         * @param username 用户名
         */
        void deleteByUsername(String username);
}
