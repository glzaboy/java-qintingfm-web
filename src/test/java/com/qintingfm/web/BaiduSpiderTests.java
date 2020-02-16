package com.qintingfm.web;

import com.qintingfm.web.spider.BaiduSpider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@SpringBootTest
class BaiduSpiderTests {
    @Autowired
    private BaiduSpider baiduSpider;

    @Test
    void contextLoads() {
    }
    @Test
    void testBaiduSpider() throws MessagingException {
        Collection<String> collection= Arrays.asList("https://qintingfm.com/page/2","https://qintingfm.com/page/1","https://qintingfm.com/blog/view/26");
        baiduSpider.pushUrlToSpider(collection);

    }

}
