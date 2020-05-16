package com.qintingfm.web.controller.admin;

import com.qintingfm.web.common.AjaxDto;
import com.qintingfm.web.common.exception.BusinessException;
import com.qintingfm.web.settings.Form;
import com.qintingfm.web.settings.SettingData;
import com.qintingfm.web.settings.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

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

    @RequestMapping(value = "/edit/{name}", method = {RequestMethod.GET})
    public ModelAndView edit(ModelAndView modelAndView, @PathVariable(value = "name") String name) {
        modelAndView.setViewName("admin/setting");
        modelAndView.addObject("settingName",name);
        Form formBySettingName = settingService.getFormBySettingName(name);
        modelAndView.addObject("form", formBySettingName);
        return modelAndView;
    }

    @RequestMapping(value = "/save/{name}", method = {RequestMethod.POST})
    @ResponseBody
    @Transactional(rollbackFor = {BusinessException.class, Exception.class})
    public AjaxDto save(@PathVariable(value = "name") String name, @Autowired HttpServletRequest request) {
        AjaxDto ajaxDto = new AjaxDto();
        Map<String, String[]> parameterMap = request.getParameterMap();
        Class<? extends SettingData> settingClass = settingService.getSettingClass(name);
        Optional<? extends SettingData> settingBean = settingService.getSettingBean(name, settingClass);
        SettingData settingData = settingBean.orElseThrow(() -> new BusinessException("读取原配置出错"));
        Class<?> superclass = settingClass;
        try {
            while (superclass != null) {
                Field[] declaredFields2 = superclass.getDeclaredFields();
                for (Field declaredField2 : declaredFields2) {
                    @SuppressWarnings("deprecation")
                    boolean accessible = declaredField2.isAccessible();
                    if (!accessible) {
                        declaredField2.setAccessible(true);
                    }
                    if (declaredField2.getName().equalsIgnoreCase("settingName")) {
                        declaredField2.set(settingData, name);
                    } else {
                        String[] strings = parameterMap.get(declaredField2.getName());
                        if (declaredField2.getType() == Boolean.class) {
                            if (strings != null && strings.length > 0) {
                                declaredField2.set(settingData, settingService.value2Boolean(strings[0]));
                            } else {
                                declaredField2.set(settingData, false);
                            }
                        } else {
                            if (strings != null && strings.length > 0) {
                                declaredField2.set(settingData, strings[strings.length - 1]);
                            }
                        }
                    }
                    if (!accessible) {
                        declaredField2.setAccessible(false);
                    }
                }
                if (superclass == SettingData.class) {
                    break;
                }
                superclass = superclass.getSuperclass();
            }
        } catch (IllegalAccessException e) {
            throw new BusinessException("保存配置出错！");
        }
        settingService.saveSettingBean(name, settingData);
        return ajaxDto;
    }
}
