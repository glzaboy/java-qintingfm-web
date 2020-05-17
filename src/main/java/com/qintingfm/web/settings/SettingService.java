package com.qintingfm.web.settings;

import com.qintingfm.web.common.exception.BusinessException;
import com.qintingfm.web.jpa.SettingInfoJpa;
import com.qintingfm.web.jpa.SettingJpa;
import com.qintingfm.web.jpa.entity.SettingInfo;
import com.qintingfm.web.jpa.entity.SettingItem;
import com.qintingfm.web.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
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
    private final String stringTrue = "TRUE";
    SettingJpa settingJpa;
    SettingInfoJpa settingInfoJpa;

    @Autowired
    public void setSettingJpa(SettingJpa settingJpa) {
        this.settingJpa = settingJpa;
    }

    @Autowired
    public void setSettingInfoJpa(SettingInfoJpa settingInfoJpa) {
        this.settingInfoJpa = settingInfoJpa;
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
                            settingItem.setValue(boolTrue());
                        }else{
                            settingItem.setValue(boolFalse());
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
        Optional<SettingInfo> byId = settingInfoJpa.findById(settingName);
        SettingInfo settingInfo = new SettingInfo();
        settingInfo.setName(settingName);
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
                    if (declaredField2.getType()==Boolean.class) {

                        String stringValue = collect.get(declaredField2.getName());
                        declaredField2.set(t, value2Boolean(stringValue));
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
    @Transactional(readOnly = true)
    public Class<? extends SettingData> getSettingClass(String settingName){
        Optional<SettingInfo> byId = settingInfoJpa.findById(settingName);
        byId.orElseThrow(()-> new BusinessException("没有获取到配置"));
        SettingInfo settingInfo = byId.get();
        try {
            return (Class<? extends SettingData>)this.getClass().getClassLoader().loadClass(settingInfo.getClassName());
        } catch (ClassNotFoundException e) {
            throw new BusinessException("没有获取配置相关数据定义！");
        }
    }
    @Transactional(readOnly = true)
    public Form getFormBySettingName(String settingName){
        Class<? extends SettingData> settingClass = getSettingClass(settingName);
        Optional<? extends SettingData> settingBean = getSettingBean(settingName, settingClass);
        return getForm(settingBean,settingName);
    }
    private  Form getForm(Optional<? extends SettingData> settingDataClass, String settingName){
        Form.FormBuilder builder1 = Form.builder();
        List<FormField> fromFields=new ArrayList<>();

        SettingData settingData = settingDataClass.orElse(null);
        Class<?> tmpClass=settingData.getClass();

        SettingField classAnnotation = AnnotationUtils.getAnnotation(tmpClass, SettingField.class);
        if(classAnnotation==null){
            builder1.title(settingName);
            builder1.settingName(settingName);
        }else{
            builder1.title(classAnnotation.title());
            builder1.settingName(settingData.settingName != null ?settingData.settingName:settingName);
        }
        while (tmpClass!=null){

            Field[] declaredFields = tmpClass.getDeclaredFields();
            FormField.FormFieldBuilder builder = FormField.builder();
            for (Field field:declaredFields) {
                builder.fieldName(field.getName());
                builder.className(field.getType().getSimpleName());
                SettingField annotation1 = AnnotationUtils.getAnnotation(field, SettingField.class);
                if (annotation1!=null){
                    builder.title(annotation1.title());
                }
                if(settingData!=null){
                    try {
                        field.setAccessible(true);
                        if(field.getType()==Boolean.class){
                            Boolean o = (Boolean)field.get(settingData);
                            if(o){
                                builder.value(boolTrue());
                            }else{
                                builder.value(boolFalse());
                            }
                        }else {
                            builder.value((String) field.get(settingData));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                fromFields.add(builder.build());
            }


            if (tmpClass == SettingData.class) {
                break;
            }
            tmpClass=tmpClass.getSuperclass();
        }
        builder1.formFields(fromFields);
        return builder1.build();
    }
    public Boolean value2Boolean(String value){
        String stringTrue = "TRUE";
        String yes = "yes";
        String num1 = "1";
        String y = "Y";
        return yes.equalsIgnoreCase(value) || y.equalsIgnoreCase(value) || stringTrue.equalsIgnoreCase(value) || num1.equalsIgnoreCase(value);
    }
    public String boolTrue(){
        return "TRUE";
    }
    public String boolFalse(){
        return "FALSE";
    }
}
