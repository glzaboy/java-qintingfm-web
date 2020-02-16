package com.qintingfm.web.service;

import com.qintingfm.web.jpa.SettingJpa;
import com.qintingfm.web.jpa.entity.SettingItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * 系统设置
 *
 * @author guliuzhong
 */
@Service
@Slf4j
public class SettingService {
    public final String ENABLE="ENABLE";
    public final String YES="YES";
    public final String Y="Y";
    public final String TRUE="TRUE";
    public final String NUM_Y="1";
    SettingJpa settingJpa;

    @Autowired
    public void setSettingJpa(SettingJpa settingJpa) {
        this.settingJpa = settingJpa;
    }

    public Stream<SettingItem> getSettings(String name) {
        return settingJpa.findByName(name);
    }
    public boolean isEnable(Map<String, String> settingItems) {
        Assert.notEmpty(settingItems);
        String s = settingItems.get(ENABLE);
        if(s==null){
            return false;
        }
        if (NUM_Y.equals(s) || TRUE.equalsIgnoreCase(s)|| Y.equalsIgnoreCase(s)|| YES.equalsIgnoreCase(s)) {
            return true;
        }
        return false;
    }
}
