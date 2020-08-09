package com.qintingfm.web.service;

import com.qintingfm.web.common.exception.BusinessException;
import com.qintingfm.web.form.FormGenerateService;
import com.qintingfm.web.jpa.SettingInfoJpa;
import com.qintingfm.web.jpa.SettingJpa;
import com.qintingfm.web.jpa.entity.SettingInfo;
import com.qintingfm.web.jpa.entity.SettingItem;
import com.qintingfm.web.pojo.vo.settings.SettingDataVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 系统设置
 *
 * @author guliuzhong
 */
@Service
@Slf4j
public class SettingService extends BaseService {
    SettingJpa settingJpa;
    SettingInfoJpa settingInfoJpa;
    FormGenerateService formGenerateService;

    @Autowired
    public void setSettingJpa(SettingJpa settingJpa) {
        this.settingJpa = settingJpa;
    }

    @Autowired
    public void setSettingInfoJpa(SettingInfoJpa settingInfoJpa) {
        this.settingInfoJpa = settingInfoJpa;
    }

    @Autowired
    public void setFormGenerateService(FormGenerateService formGenerateService) {
        this.formGenerateService = formGenerateService;
    }

    /**
     * 保存对象到设置数据库
     *
     * @param settingName 对象名称
     * @param bean  需要保存的对象要求此对象继承自 SettingData或其子类
     * @param <T> 类型限定为 SettingData 的子类
     * @return 返回设置的对象值
     */
    @CacheEvict(value = {"settings.bean","settings.form","settings.form"},key = "#settingName")
    public synchronized  <T extends SettingDataVo> T saveSettingBean(String settingName, T bean) {
        Class<?> superclass = bean.getClass();
        ArrayList<SettingItem> settingItems=new ArrayList<>();
        while (superclass!=null){
            Field[] declaredFields = superclass.getDeclaredFields();
            for (Field field:declaredFields){
                SettingItem settingItem=new SettingItem();
                settingItem.setName(settingName);
                field.setAccessible(true);
                try {
                    Object o = field.get(bean);
                    if(o instanceof Boolean){
                        if ((Boolean) o){
                            settingItem.setValue(formGenerateService.boolTrue());
                        }else{
                            settingItem.setValue(formGenerateService.boolFalse());
                        }
                    }else{
                        settingItem.setValue((String) field.get(bean));
                    }
                    settingItem.setKey(field.getName());
                    settingItems.add(settingItem);
                } catch (IllegalAccessException e) {
                    log.warn("保存设置{}失败，字段不可读{},跳过些字段保存。",settingName,field.getName());
                }
            }
            if (superclass == SettingDataVo.class) {
                break;
            }
            superclass=superclass.getSuperclass();
        }
        settingJpa.deleteInBatch(settingJpa.findByName(settingName).collect(Collectors.toList()));
        settingJpa.saveAll(settingItems);
        settingJpa.flush();
        Optional<SettingInfo> byId = settingInfoJpa.findById(settingName);
        SettingInfo settingInfo = new SettingInfo();
        settingInfo.setName(settingName);
        assert bean.getClass() != null;
        settingInfo.setClassName(bean.getClass().getName());
        SettingInfo settingInfo1 = byId.orElse(settingInfo);
        settingInfoJpa.saveAndFlush(settingInfo1);
        return bean;
    }

    /**
     * 读取设置的对象到类
     *
     * @param settingName 对象名称
     * @param classic 读取后返回的对象类型 SettingData或其子类
     * @param <T> 类型限定为 SettingData 的子类
     * @return  读取后返回的对象
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "settings.bean",key = "#settingName")
    public synchronized <T extends SettingDataVo> Optional<T> getSettingBean(String settingName, Class<T> classic) {
        Stream<SettingItem> settings = settingJpa.findByName(settingName);
        try {
            Constructor<T> declaredConstructor = classic.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            T t = declaredConstructor.newInstance();
            Map<String, String> collect = settings.collect(Collectors.toMap(SettingItem::getKey, item-> item.getValue()!=null?item.getValue():"", (v1, v2) -> v2));
            Class<? super T> superclass = classic;
            while (superclass != null) {
                Field[] declaredFields2 = superclass.getDeclaredFields();
                for (Field declaredField2 : declaredFields2) {
                    @SuppressWarnings("deprecation")
                    boolean accessible = declaredField2.isAccessible();
                    if (!accessible) {
                        declaredField2.setAccessible(true);
                    }
                    if (declaredField2.getType()==Boolean.class) {
                        String stringValue = collect.get(declaredField2.getName());
                        declaredField2.set(t, formGenerateService.value2Boolean(stringValue));
                    } else {
                        declaredField2.set(t, collect.get(declaredField2.getName()));
                    }
                    if (!accessible) {
                        declaredField2.setAccessible(false);
                    }
                }
                if (superclass == SettingDataVo.class) {
                    break;
                }
                superclass = superclass.getSuperclass();
            }
            if(t.getSettingName()==null){
                t.setSettingName(settingName);
            }
            return Optional.of(t);
        } catch (NoSuchMethodException e) {
            log.error("创建Bean 实例出错{}",e.getMessage());
        } catch (IllegalAccessException e) {
            log.error("设置Bean 属性出错{}",e.getMessage());
        } catch (InstantiationException e) {
            log.error("创建Bean 实例出错1{}",e.getMessage());
        } catch (InvocationTargetException e) {
            log.error("创建Bean 实例出错,反射异常{}",e.getMessage());
        }
        return Optional.empty();
    }
    @Transactional(readOnly = true)
    public Class<? extends SettingDataVo> getSettingClass(String settingName){
        Optional<SettingInfo> byId = settingInfoJpa.findById(settingName);
        byId.orElseThrow(()-> new BusinessException("没有获取到配置"));
        SettingInfo settingInfo = byId.get();
        try {
            @SuppressWarnings({ "unchecked" })
            Class<? extends SettingDataVo> aClass = (Class<? extends SettingDataVo>) this.getClass().getClassLoader().loadClass(settingInfo.getClassName());
            return aClass;
        } catch (ClassNotFoundException e) {
            throw new BusinessException("没有获取配置相关数据定义！");
        }
    }
    @Transactional(readOnly = true)
    @Cacheable(value = "settings.form",key = "#settingName")
    public com.qintingfm.web.form.Form getFormBySettingName(String settingName){
        Class<? extends SettingDataVo> settingClass = getSettingClass(settingName);
        Optional<? extends SettingDataVo> settingBean = getSettingBean(settingName, settingClass);
        return formGenerateService.generalForm(settingClass,settingBean.orElse(null));
    }
    public Boolean value2Boolean(String value){
        return formGenerateService.value2Boolean(value);
    }
}
