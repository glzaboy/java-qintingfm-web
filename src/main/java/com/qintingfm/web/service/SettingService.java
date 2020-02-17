package com.qintingfm.web.service;

import com.qintingfm.web.jpa.SettingJpa;
import com.qintingfm.web.jpa.entity.SettingItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
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
        Optional<Map.Entry<String, String>> first = settingItems.entrySet().stream().filter(settingItem -> {
            if (settingItem.getKey().equalsIgnoreCase(ENABLE)) {
                return true;
            }
            return false;
        }).findFirst();
        AtomicBoolean ret= new AtomicBoolean(false);
        ret.set(false);
        first.ifPresent(item->{
            if (NUM_Y.equals(item.getValue()) || TRUE.equalsIgnoreCase(item.getValue())|| Y.equalsIgnoreCase(item.getValue())|| YES.equalsIgnoreCase(item.getValue())) {
                ret.set(true);
            }
        });

        return ret.get();
    }
}
