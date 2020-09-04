package com.qintingfm.web.jpa;

import com.qintingfm.web.jpa.entity.MiniApp;
import com.qintingfm.web.jpa.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author guliuzhong
 */
public interface MiniAppJpa  extends JpaRepository<MiniApp, Integer> {
}
