package com.qintingfm.web.settings;

import com.qintingfm.web.jpa.SettingJpa;
import com.qintingfm.web.jpa.entity.SettingItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SettingService {
    private final String stringTrue = "TRUE";
    SettingJpa settingJpa;

    @Autowired
    public void setSettingJpa(SettingJpa settingJpa) {
        this.settingJpa = settingJpa;
    }
    /**
     * 保存对象到设置数据库
     *
     * @param settingName 对象名称
     * @param bean  需要保存的对象要求此对象继承自 SettingData或其子类
     * @param <T> 类型限定为 SettingData 的子类
     * @return 返回设置的对象值
     */
    public synchronized  <T extends SettingData> T saveSettingBean(String settingName,T bean) {
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
                            settingItem.setValue(stringTrue);
                        }else{
                            String stringFalse = "FALSE";
                            settingItem.setValue(stringFalse);
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
            if (superclass == SettingData.class) {
                break;
            }
            superclass=superclass.getSuperclass();
        }
        settingJpa.deleteInBatch(settingJpa.findByName(settingName).collect(Collectors.toList()));
        settingJpa.saveAll(settingItems);
        settingJpa.flush();
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
    public synchronized <T extends SettingData> Optional<T> getSettingBean(String settingName, Class<T> classic) {
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
                    String enable = "ENABLE";
                    if (declaredField2.getName().equalsIgnoreCase(enable)) {
                        String yes = "yes";
                        String num1 = "1";
                        String y = "Y";
                        String stringValue = collect.get(declaredField2.getName());

                        boolean b = yes.equalsIgnoreCase(stringValue) || y.equalsIgnoreCase(stringValue) || stringTrue.equalsIgnoreCase(stringValue) || num1.equalsIgnoreCase(stringValue);
                        if (b) {
                            declaredField2.set(t, true);
                        } else {
                            declaredField2.set(t, false);
                        }
                    } else {
                        declaredField2.set(t, collect.get(declaredField2.getName()));
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
}
