package com.qintingfm.web.config;

import com.qintingfm.web.storage.Config;
import com.qintingfm.web.storage.Manager;
import com.qintingfm.web.storage.Oss;
import com.qintingfm.web.storage.QiniuOssImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author guliuzhong
 */
@Configuration
public class StorageConfig {
    @Bean(name = "uploadConfig")
    @ConfigurationProperties(prefix = "storage.qiniu")

    Config qiniuConfig() {
        return new Config();
    }

    @Bean(name = "uploadQss")
    Oss qiniuOss(@Autowired @Qualifier("uploadConfig") Config config) {
        QiniuOssImpl qiniuOss = new QiniuOssImpl();
        qiniuOss.setConfig(config);
        return qiniuOss;
    }
    @Bean(name = "upload")
    @Primary
    Manager manager(@Autowired @Qualifier("uploadQss") Oss oss) {
        Manager manager = new Manager();
        manager.setOss(oss);
        return manager;
    }
    @Bean(name = "wxuploadConfig")
    @ConfigurationProperties(prefix = "storage.wx")
    Config wxqiniuConfig() {
        return new Config();
    }

    @Bean(name = "wxuploadQss")
    Oss wxqiniuOss(@Autowired @Qualifier("uploadConfig") Config config) {
        QiniuOssImpl qiniuOss = new QiniuOssImpl();
        qiniuOss.setConfig(config);
        return qiniuOss;
    }
    @Bean(name = "wxupload")
    Manager wxmanager(@Autowired @Qualifier("wxuploadQss") Oss oss) {
        Manager manager = new Manager();
        manager.setOss(oss);
        return manager;
    }

}
