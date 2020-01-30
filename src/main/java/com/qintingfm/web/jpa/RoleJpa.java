package com.qintingfm.web.jpa;

import com.qintingfm.web.jpa.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * 权限角色
 * @author guliuzhong
 */
@Component
public interface RoleJpa extends JpaRepository<Role,Long> {
}
