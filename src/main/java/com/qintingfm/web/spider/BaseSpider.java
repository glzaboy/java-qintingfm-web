package com.qintingfm.web.spider;

import com.qintingfm.web.jpa.entity.SettingItem;
import com.qintingfm.web.settings.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

/**
 * @author guliuzhong
 */
public abstract class BaseSpider implements Spider {
    SettingService settingService;

    @Autowired
    public void setSettingService(SettingService settingService) {
        this.settingService = settingService;
    }

    @Override
    @Transactional(readOnly = true,rollbackFor = {Exception.class})
    public Stream<SettingItem> getSpiderSettings(String spiderName) {
        return settingService.getSettings(spiderName);
    }


}
