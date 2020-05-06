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
    public final String ENABLE = "ENABLE";
    public final String YES = "YES";
    public final String Y = "Y";
    public final String TRUE = "TRUE";
    public final String NUM_Y = "1";
    SettingJpa settingJpa;

    @Autowired
    public void setSettingJpa(SettingJpa settingJpa) {
        this.settingJpa = settingJpa;
    }
    @Transactional(readOnly = true)
    public synchronized <T extends SettingData> Optional<T> getSettingBean(String settingName, Class<T> classic) {
        Stream<SettingItem> settings = settingJpa.findByName(settingName);
        try {
            Constructor declaredConstructor = classic.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            T t = (T) declaredConstructor.newInstance();
            Map<String, String> collect = settings.collect(Collectors.toMap(SettingItem::getKey, SettingItem::getValue, (v1, v2) -> v2));
            Class<? super T> superclass = classic;
            while (superclass != null) {
                Field[] declaredFields2 = superclass.getDeclaredFields();
                for (Field declaredField2 : declaredFields2) {
                    boolean accessible = declaredField2.isAccessible();
                    if (!accessible) {
                        declaredField2.setAccessible(true);
                    }
                    if (declaredField2.getName().equalsIgnoreCase(ENABLE)) {
                        boolean b = YES.equalsIgnoreCase(collect.get(declaredField2.getName())) || Y.equalsIgnoreCase(collect.get(declaredField2.getName())) || TRUE.equalsIgnoreCase(collect.get(declaredField2.getName())) || NUM_Y.equalsIgnoreCase(collect.get(declaredField2.getName()));
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
