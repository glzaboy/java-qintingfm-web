package com.qintingfm.web;

import com.qintingfm.web.htmlsucker.Article;
import com.qintingfm.web.service.HtmlService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

/**
 * 应用程序启动类
 *
 * @author guliuzhong
 */

public class ApplicationT {
    public static void main(String[] args)  throws IOException {
        HtmlService htmlService=new HtmlService();
        Article parse = htmlService.parse("https://qintingfm.com/blog/view/124",3000);
        System.out.println(htmlService.filterSimpleText(parse.getContent()));
    }
}
