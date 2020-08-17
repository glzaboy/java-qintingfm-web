package com.qintingfm.web;

import com.qintingfm.web.service.BaiduAiApi;
import com.qintingfm.web.spider.BaiduSpider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

@SpringBootTest
@Slf4j
class BaiduAiTests {
    @Autowired
    private BaiduAiApi baiduAiApi;

    @Test
    void testPlant() throws IOException {
        String plant = baiduAiApi.plant(URI.create("file:///Users/guliuzhong/1.jpeg"), 3);
        log.info(plant);
        String plant2 = baiduAiApi.plant(URI.create("https://img-blog.csdn.net/20180119101851037?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvc2luYXRfMzc5MjQxNzY=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast"), 3);
        log.info(plant2);
    }
    @Test
    void testGetPicInfo() throws IOException {
        String plant = baiduAiApi.getPicInfo(URI.create("file:///Users/guliuzhong/1.png"));
        log.info(plant);
        String plant2 = baiduAiApi.getPicInfo(URI.create("http://img.ewebweb.com/uploads/20191104/13/1572845253-jUhDidVySF.jpeg"));
        log.info(plant2);
    }
}
