package com.qintingfm.web.settings;

import com.qintingfm.web.jpa.SettingJpa;
import com.qintingfm.web.jpa.entity.SettingItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
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

    public Stream<SettingItem> getSettings(String name) {
        return settingJpa.findByName(name);
    }

    public <T extends SettingData> T getConfig(String settingName, Class<T> classzz) {
        Stream<SettingItem> settings = getSettings(settingName);
        Constructor declaredConstructor = null;
        try {
            declaredConstructor = classzz.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            T t = (T) declaredConstructor.newInstance();
//            Field[] declaredFields = classzz.getDeclaredFields();
            Map<String, String> collect = settings.collect(Collectors.toMap(SettingItem::getKey, SettingItem::getValue, (v1, v2) -> v2));

//            for (int i = 0; i < declaredFields.length; i++) {
//                Field declaredField = declaredFields[i];
//                boolean accessible = declaredField.isAccessible();
//                if (!accessible) {
//                    declaredField.setAccessible(true);
//                }
//                declaredField.set(t, collect.get(declaredField.getName()));
//                if (!accessible) {
//                    declaredField.setAccessible(false);
//                }
//            }
            Class<? super T> superclass = classzz;
            while (superclass != null) {
                Field[] declaredFields2 = superclass.getDeclaredFields();
                for (int i = 0; i < declaredFields2.length; i++) {
                    Field declaredField2 = declaredFields2[i];
                    boolean accessible = declaredField2.isAccessible();
                    if (!accessible) {
                        declaredField2.setAccessible(true);
                    }
                    if (declaredField2.getName().equalsIgnoreCase(ENABLE)) {
                        boolean b = YES.equalsIgnoreCase(collect.get(declaredField2.getName())) || Y.equalsIgnoreCase(collect.get(declaredField2.getName())) || TRUE.equalsIgnoreCase(collect.get(declaredField2.getName())) || NUM_Y.equalsIgnoreCase(collect.get(declaredField2.getName()));
                        if (b) {
                            declaredField2.set(t, true);
                        }else {
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
                superclass= superclass.getSuperclass();
            }
//            for (Iterator<SettingItem> it = settings.iterator(); it.hasNext(); ) {
//                SettingItem settingItem = it.next();
//                Field declaredField = T.getDeclaredField(settingItem.getKey());
//                declaredField.set(t,settingItem.getValue());
//            }
            return (T) t;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isEnable(Map<String, String> settingItems) {
        Optional<Map.Entry<String, String>> first = settingItems.entrySet().stream().filter(settingItem ->
                settingItem.getKey().equalsIgnoreCase(ENABLE)
        ).findFirst();
        AtomicBoolean ret = new AtomicBoolean(false);
        ret.set(false);
        first.ifPresent(item -> {
            if (NUM_Y.equals(item.getValue()) || TRUE.equalsIgnoreCase(item.getValue()) || Y.equalsIgnoreCase(item.getValue()) || YES.equalsIgnoreCase(item.getValue())) {
                ret.set(true);
            }
        });
        return ret.get();
    }
}
