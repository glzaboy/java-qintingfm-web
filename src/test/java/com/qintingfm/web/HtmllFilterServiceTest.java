package com.qintingfm.web;

import com.qintingfm.web.htmlsucker.Article;
import com.qintingfm.web.service.HtmlService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
@Slf4j
public class HtmllFilterServiceTest {
    @Autowired
    HtmlService htmlFilterService;
    @Test
    public void testHtmlFilter(){
        String filter = htmlFilterService.filter("<img src=\"http://baidu.com\" alt=\"tst\"><script>alert(1)</script><p>afds</p>");
        log.info(filter);
    }
    @Test
    public void testHtmlTool() throws IOException {
        Article parse = htmlFilterService.parse("https://shareapp.cyol.com/cmsfile/News/202003/14/share349788.html?t=1584188623", 2000);

        log.info(String.valueOf(parse));
        log.info(htmlFilterService.filter(parse.getContent()));
    }
}
