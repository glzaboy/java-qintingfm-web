package com.qintingfm.web.spider;

import com.qintingfm.web.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author guliuzhong
 */
public abstract class BaseSpider implements Spider {
    SettingService settingService;

    @Autowired
    public void setSettingService(SettingService settingService) {
        this.settingService = settingService;
    }
}
