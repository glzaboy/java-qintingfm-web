package com.qintingfm.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    String mailUser;



    @Test
    void contextLoads() {
    }
    @Test
    void testSendMail() throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);


        helper.setText("This is test");
        helper.setTo("glzaboy@163.com");
        helper.setSubject("这个是测试"+new Date().toString());
        helper.setFrom(mailUser);
        mailSender.send(mimeMessage);

    }

}
