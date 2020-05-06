package com.qintingfm.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * 应用程序启动类
 *
 * @author guliuzhong
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.qintingfm.web.jpa")
public class Application {
    public static void main(String[] args) {
//        Properties properties = System.getProperties();
//        properties.setProperty("spring.profiles.active","test");
//        System.setProperties(properties);
        SpringApplication.run(Application.class, args);
    }
}
