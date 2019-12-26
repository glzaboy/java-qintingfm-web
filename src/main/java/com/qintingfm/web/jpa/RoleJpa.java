package com.qintingfm.web.jpa;

import com.qintingfm.web.dao.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface RoleJpa extends JpaRepository<Role,Long> {
}
