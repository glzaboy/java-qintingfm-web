package com.qintingfm.web.jpa;

import com.qintingfm.web.jpa.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserJpa extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
}
