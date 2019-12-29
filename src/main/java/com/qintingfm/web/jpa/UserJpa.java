package com.qintingfm.web.jpa;

import com.qintingfm.web.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserJpa extends JpaRepository<User,Long> {
}
