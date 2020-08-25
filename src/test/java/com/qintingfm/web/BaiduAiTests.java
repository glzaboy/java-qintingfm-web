package com.qintingfm.web;

import com.qintingfm.web.service.BaiduAiApiService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;

@SpringBootTest
@Slf4j
class BaiduAiTests {
    @Autowired
    private BaiduAiApiService baiduAiApiService;

    @Test
    void testPlant() throws IOException {
//        String plant = baiduAiApi.plant(URI.create("file:///Users/guliuzhong/1.jpeg"), 3);
//        log.info(plant);
        String plant2 = baiduAiApiService.plant(URI.create("https://img-blog.csdn.net/20180119101851037?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc2luYXRfMzc5MjQxNzY=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast"), 3);
        log.info(plant2);
    }
    @Test
    void testGetPicInfo() throws IOException {
        String plant = baiduAiApiService.getPicInfo(URI.create("file:///Users/guliuzhong/1.png"));
        log.info(plant);
        String plant2 = baiduAiApiService.getPicInfo(URI.create("http://img.ewebweb.com/uploads/20191104/13/1572845253-jUhDidVySF.jpeg"));
        log.info(plant2);
    }
}
