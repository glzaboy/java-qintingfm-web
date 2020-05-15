package com.qintingfm.web.controller.admin;

import com.qintingfm.web.settings.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
        modelAndView.addObject("name", name);
//        Class<?> aClass;
//        try {
//            aClass = Class.forName(name);
//            Optional<T> settingBean = settingService.getSettingBean(name, aClass);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }

        return modelAndView;
    }
}
