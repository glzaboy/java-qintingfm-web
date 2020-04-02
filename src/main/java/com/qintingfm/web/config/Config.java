package com.qintingfm.web.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 一般性配置
 *
 * @author guliuzhong
 */
@Configuration
public class Config {
    /**
     * http客户端
     *
     * @return OkHttpClient
     */
    @Bean
    OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        return builder.build();
    }
}
