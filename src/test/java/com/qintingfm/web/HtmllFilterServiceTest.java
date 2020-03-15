package com.qintingfm.web;

import com.qintingfm.web.service.HtmlFilterService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class HtmllFilterServiceTest {
    @Autowired
    HtmlFilterService htmlFilterService;
    @Test
    public void testHtmlFilter(){
        String filter = htmlFilterService.filter("<img src=\"baidu.com\">");
        log.info(filter);
    }
}
