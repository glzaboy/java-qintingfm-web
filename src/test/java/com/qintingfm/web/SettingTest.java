package com.qintingfm.web;

import com.qintingfm.web.spider.BaiduSpiderSetting;
import com.qintingfm.web.settings.SettingData;
import com.qintingfm.web.settings.SettingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
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
    @Test
    @Transactional
    @Rollback(value = false)
    void testWrite()  {
        Optional<BaiduSpiderSetting> baidu = settingService.getSettingBean("baidu1", BaiduSpiderSetting.class);
        System.out.println(baidu.toString());
        BaiduSpiderSetting baiduSpiderSetting = baidu.get();
        baiduSpiderSetting.setSettingName("现在时间");
        baiduSpiderSetting.setEnable(false);
        BaiduSpiderSetting baidu1 = settingService.saveSettingBean("baidu1", baiduSpiderSetting);
    }
}
