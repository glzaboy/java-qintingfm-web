package com.qintingfm.web;

import com.qintingfm.web.jpa.entity.SettingItem;
import com.qintingfm.web.settings.BaiduSpiderSetting;
import com.qintingfm.web.settings.SettingData;
import com.qintingfm.web.settings.SettingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Arrays;

@SpringBootTest
public class SettingTest {
    @Autowired
    SettingService settingService;
    @Test
    @Transactional
    void test() {
        BaiduSpiderSetting baidu1 = settingService.getConfig("baidu", BaiduSpiderSetting.class);
        System.out.println(baidu1.toString());
        SettingData baidu2 = settingService.getConfig("register", SettingData.class);
        System.out.println(baidu2.toString());
    }
}
