package com.qintingfm.web;

import com.qintingfm.web.service.form.Form;
import com.qintingfm.web.pojo.vo.settings.BaiduSpiderSettingVo;
import com.qintingfm.web.service.SettingService;
import com.qintingfm.web.pojo.vo.settings.RegisterSettingVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Slf4j
public class SettingTest {
    @Autowired
    SettingService settingService;
    @Test
    @Transactional
    void test() {
        Optional<BaiduSpiderSettingVo> baidu = settingService.getSettingBean("baidu", BaiduSpiderSettingVo.class);
        log.debug(baidu.toString());
        Optional<RegisterSettingVo> register = settingService.getSettingBean("register", RegisterSettingVo.class);
        log.debug(register.toString());
    }
    @Test
    @Transactional
    @Rollback(value = false)
    void testWrite()  {
        Optional<BaiduSpiderSettingVo> baidu = settingService.getSettingBean("baidu1", BaiduSpiderSettingVo.class);
        baidu.ifPresent(baiduSpiderSetting -> {
            log.debug(baiduSpiderSetting.toString());
            baiduSpiderSetting.setSettingName("现在时间");
            baiduSpiderSetting.setEnable(true);
            BaiduSpiderSettingVo baidu1 = settingService.saveSettingBean("baidu1", baiduSpiderSetting);
            log.debug(baidu1.toString());
        });
        Optional<RegisterSettingVo> register = settingService.getSettingBean("register", RegisterSettingVo.class);
        register.ifPresent(settingData -> {
            log.debug(settingData.toString());
            settingService.saveSettingBean("register",settingData);
        });
    }
    @Test
    @Transactional
    void readFromString() {
        Form baidu = settingService.getFormBySettingName("baidu");
        System.out.println(baidu);

    }
}
