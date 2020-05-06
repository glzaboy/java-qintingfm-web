package com.qintingfm.web;

import com.qintingfm.web.spider.BaiduSpiderSetting;
import com.qintingfm.web.settings.SettingData;
import com.qintingfm.web.settings.SettingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
public class SettingTest {
    @Autowired
    SettingService settingService;
    @Test
    @Transactional
    void test() {
        Optional<BaiduSpiderSetting> baidu = settingService.getSettingBean("baidu", BaiduSpiderSetting.class);
        System.out.println(baidu.toString());
        Optional<SettingData> register = settingService.getSettingBean("register", SettingData.class);
        System.out.println(register.toString());
    }
}
