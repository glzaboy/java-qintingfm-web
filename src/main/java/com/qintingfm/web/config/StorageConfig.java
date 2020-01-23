package com.qintingfm.web.config;

import com.qintingfm.web.storage.Config;
import com.qintingfm.web.storage.Manager;
import com.qintingfm.web.storage.Oss;
import com.qintingfm.web.storage.QiniuOssImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {
    @Bean
    @ConfigurationProperties(prefix="storage.qiniu")
    Config qiniuConfig(){
        return new Config();
    }
    @Bean
    @Autowired
    Oss qiniuOss( Config config){
        QiniuOssImpl qiniuOss = new QiniuOssImpl();
        qiniuOss.setConfig(config);
        return qiniuOss;
    }
    @Bean
    Manager manager(@Autowired Oss oss){
        Manager manager = new Manager();
        manager.setOss(oss);
        return manager;
    }


}
