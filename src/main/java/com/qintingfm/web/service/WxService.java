package com.qintingfm.web.service;

import com.qintingfm.web.service.form.FormSelect;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 微信小程序接口
 * @author guliuzhong
 */
@Service
public class WxService extends BaseService{
    public Set<FormSelect> getSelect(){
        Set<FormSelect> formSelects=new HashSet<>();
        formSelects.add(FormSelect.builder().key("1").value("wxService1").build());
        formSelects.add(FormSelect.builder().key("2").value("wxService2").build());
        formSelects.add(FormSelect.builder().key("3").value("wxService3").build());
        formSelects.add(FormSelect.builder().key("4").value("wxService4").build());
        return formSelects;
    }
}
