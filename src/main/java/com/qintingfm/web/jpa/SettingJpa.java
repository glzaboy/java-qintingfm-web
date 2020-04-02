package com.qintingfm.web.jpa;

import com.qintingfm.web.jpa.entity.SettingItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

/**
 * @author guliuzhong
 */
public interface SettingJpa extends JpaRepository<SettingItem, Integer> {
    /**
     * 获取指定的系统设置
     *
     * @param settingName 系统名称
     * @return
     */
    Stream<SettingItem> findByName(String settingName);
}
