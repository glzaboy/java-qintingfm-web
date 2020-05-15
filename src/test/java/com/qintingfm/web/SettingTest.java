package com.qintingfm.web;

import com.qintingfm.web.settings.SettingField;
import com.qintingfm.web.spider.BaiduSpiderSetting;
import com.qintingfm.web.settings.SettingData;
import com.qintingfm.web.settings.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
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
    void readFromString() throws ClassNotFoundException {
        Class<?> aClass = this.getClass().getClassLoader().loadClass("com.qintingfm.web.spider.BaiduSpiderSetting");
        SettingField annotation1 = AnnotationUtils.getAnnotation(aClass, SettingField.class);
        System.out.println(annotation1.value());
        while (aClass!=null){

            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field field:declaredFields) {
                System.out.println(field.getName());
                SettingField annotation1 = AnnotationUtils.getAnnotation(field, SettingField.class);


                System.out.println(annotation1.value());
                System.out.println(annotation1.title());
            }


            if (aClass == SettingData.class) {
                break;
            }
            aClass=aClass.getSuperclass();
        }



    }
}
