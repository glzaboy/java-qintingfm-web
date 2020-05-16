package com.qintingfm.web;

import com.qintingfm.web.settings.Form;
import com.qintingfm.web.settings.FormField;
import com.qintingfm.web.spider.BaiduSpiderSetting;
import com.qintingfm.web.settings.SettingData;
import com.qintingfm.web.settings.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Slf4j
public class SettingTest {
    @Autowired
    SettingService settingService;
    @Test
    @Transactional
    void test() {
        Optional<BaiduSpiderSetting> baidu = settingService.getSettingBean("baidu", BaiduSpiderSetting.class);
        log.debug(baidu.toString());
        Optional<SettingData> register = settingService.getSettingBean("register", SettingData.class);
        log.debug(register.toString());
    }
    @Test
    @Transactional
    @Rollback(value = false)
    void testWrite()  {
        Optional<BaiduSpiderSetting> baidu = settingService.getSettingBean("baidu1", BaiduSpiderSetting.class);
        baidu.ifPresent(baiduSpiderSetting -> {
            log.debug(baiduSpiderSetting.toString());
            baiduSpiderSetting.setSettingName("现在时间");
            baiduSpiderSetting.setEnable(true);
            BaiduSpiderSetting baidu1 = settingService.saveSettingBean("baidu1", baiduSpiderSetting);
            log.debug(baidu1.toString());
        });
        Optional<SettingData> register = settingService.getSettingBean("register", SettingData.class);
        register.ifPresent(settingData -> {
            log.debug(settingData.toString());
            settingService.saveSettingBean("register",settingData);
        });
    }
    @Test
    @Transactional
    void readFromString() {
//        List<FromField> form = settingService.getFormByClass("com.qintingfm.web.spider.BaiduSpiderSetting");
//        System.out.println(form);
        Form baidu = settingService.getFormBySettingName("baidu");
        System.out.println(baidu);

    }
}
