package com.qintingfm.web.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class Config {
    @Bean
    OkHttpClient okHttpClient(){
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        return builder.build();
    }
}
