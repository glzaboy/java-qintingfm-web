package com.qintingfm.web;

import com.qintingfm.web.spider.BaiduSpider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collection;

@SpringBootTest
@Slf4j
class BaiduSpiderTests {
    @Autowired
    private BaiduSpider baiduSpider;

    @Test
    void testBaiduSpider() {
        Collection<String> collection = Arrays.asList("https://qintingfm.com/page/2", "https://qintingfm.com/page/1", "https://qintingfm.com/blog/view/26");
        String s = baiduSpider.pushUrlToSpider(collection);
        log.trace(s);
    }

}
