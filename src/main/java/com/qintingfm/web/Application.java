package com.qintingfm.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
@EnableJdbcRepositories(basePackages = "com.qintingfm.web.jdbc")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
