package com.qintingfm.web.controller;

import com.qintingfm.web.service.BaseService;
import com.qintingfm.web.settings.SettingService;
import com.qintingfm.web.settings.repo.SiteSetting;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * 服务基础类
 *
 * @author guliuzhong
 */
public class BaseController extends BaseService {
    public SettingService settingService;
    @Autowired
    public void setSettingService(SettingService settingService) {
        this.settingService = settingService;
    }

    public SiteSetting getSiteSetting(){
        Optional<SiteSetting> site = settingService.getSettingBean("site", SiteSetting.class);
        SiteSetting siteSetting=new SiteSetting();
        return site.orElse(siteSetting);
    }

}
