package com.qintingfm.web;

import com.qintingfm.web.spider.BaiduSpider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.Collection;

@SpringBootTest
class BaiduSpiderTests {
    @Autowired
    private BaiduSpider baiduSpider;

    @Test
    void contextLoads() {
    }

    @Test
    void testBaiduSpider() throws MessagingException {
        Collection<String> collection = Arrays.asList("https://qintingfm.com/page/2", "https://qintingfm.com/page/1", "https://qintingfm.com/blog/view/26");
        baiduSpider.pushUrlToSpider(collection);

    }

}
