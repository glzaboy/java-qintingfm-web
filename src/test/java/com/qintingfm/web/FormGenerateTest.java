package com.qintingfm.web;

import com.qintingfm.web.pojo.vo.MiniAppVo;
import com.qintingfm.web.service.FormGenerateService;
import com.qintingfm.web.service.form.Form;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FormGenerateTest {
    @Autowired
    FormGenerateService formGenerateService;
    @Test
    public void testSelect(){
        Form form = formGenerateService.generalForm(MiniAppVo.class);
        System.out.println(form);
    }
}
