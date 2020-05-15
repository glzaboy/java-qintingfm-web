package com.qintingfm.web.jpa;

import com.qintingfm.web.jpa.entity.SettingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author guliuzhong
 */
public interface SettingInfoJpa extends JpaRepository<SettingInfo, String> {
}
