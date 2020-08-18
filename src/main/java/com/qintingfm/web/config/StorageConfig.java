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
    @ConfigurationProperties(prefix = "storage.upload")
    Config config() {
        return new Config();
    }
    @Bean(name = "uploadQss")
    Oss oss(@Autowired @Qualifier("uploadConfig") Config config) {
        QiniuOssImpl oss = new QiniuOssImpl();
        oss.setConfig(config);
        return oss;
    }
    @Bean(name = "upload")
    @Primary
    Manager manager(@Autowired @Qualifier("uploadQss") Oss oss) {
        Manager manager = new Manager();
        manager.setOss(oss);
        return manager;
    }
    @Bean(name = "wxUploadConfig")
    @ConfigurationProperties(prefix = "storage.wx")
    Config wxConfig() {
        return new Config();
    }

    @Bean(name = "wxQss")
    Oss wxOss(@Autowired @Qualifier("wxUploadConfig") Config config) {
        QiniuOssImpl oss = new QiniuOssImpl();
        oss.setConfig(config);
        return oss;
    }
    @Bean(name = "wxUpload")
    Manager wxManager(@Autowired @Qualifier("wxQss") Oss oss) {
        Manager manager = new Manager();
        manager.setOss(oss);
        return manager;
    }

}
