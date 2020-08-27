package com.qintingfm.web.service;

import com.qintingfm.web.service.form.FormOption;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 微信小程序接口
 * @author guliuzhong
 */
@Service
public class WxService extends BaseService{
    public Set<FormOption> getSelect(){
        Set<FormOption> formOptions =new HashSet<>();
        formOptions.add(FormOption.builder().id("1").text("wxService1").build());
        formOptions.add(FormOption.builder().id("2").text("wxService2").build());
        formOptions.add(FormOption.builder().id("3").text("wxService3").build());
        formOptions.add(FormOption.builder().id("4").text("wxService4").build());
        return formOptions;
    }
}
