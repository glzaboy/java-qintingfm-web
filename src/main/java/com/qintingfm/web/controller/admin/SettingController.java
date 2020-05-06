package com.qintingfm.web.controller.admin;

import com.qintingfm.web.jpa.entity.SettingItem;
import com.qintingfm.web.settings.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Stream;

/**
 * @author guliuzhong
 */
@Controller
@RequestMapping("/admin/setting")
public class SettingController {

    SettingService settingService;

    @Autowired
    public void setSettingService(SettingService settingService) {
        this.settingService = settingService;
    }

    @RequestMapping("/viewSetting/{name}")
    public ModelAndView viewSetting(ModelAndView modelAndView, @PathVariable(value = "name") String name) {
        modelAndView.setViewName("admin/setting");
        modelAndView.addObject("name", name);
//        Stream<SettingItem> settings = settingService.getSettings(name);
//        modelAndView.addObject("settings", settings);
        return modelAndView;
    }
}
