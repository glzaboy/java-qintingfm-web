package com.qintingfm.web.service;

import com.qintingfm.web.service.form.Form;
import com.qintingfm.web.service.form.FormOption;
import com.qintingfm.web.service.form.annotation.FieldAnnotation;
import com.qintingfm.web.service.form.annotation.FormAnnotation;
import com.qintingfm.web.pojo.vo.BaseVo;
import com.qintingfm.web.service.form.FormItem;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 表单生成器
 *
 * @author guliuzhong
 */
@Data
@Slf4j
@Service
public class FormGenerateService implements ApplicationContextAware {
    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public <T extends BaseVo> Form generalFormData(T classData) {
        return generalForm(null, classData);
    }

    public Form generalForm(Class classic) {
        return generalForm(classic, null);
    }

    public Form generalForm(Class classic, Object classDate) {
        Form.FormBuilder builder1 = Form.builder();
        List<FormItem> fromFields = new ArrayList<>();
        Class<?> tmpClass = classic != null ? classic : classDate.getClass();
        FormAnnotation classAnnotation = AnnotationUtils.getAnnotation(tmpClass, FormAnnotation.class);
        if (classAnnotation != null) {
            builder1.title(classAnnotation.title()).hideSubmit(classAnnotation.hideSubmit());
            builder1.message(classAnnotation.message()).method(classAnnotation.method());
        }
        while (tmpClass != null) {
            Field[] declaredFields = tmpClass.getDeclaredFields();
            for (Field field : declaredFields) {
                FormItem.FormItemBuilder builder = FormItem.builder();
                builder.fieldName(field.getName());
                builder.className(field.getType().getSimpleName());
                FieldAnnotation fieldAnnotation = AnnotationUtils.getAnnotation(field, FieldAnnotation.class);
                if (fieldAnnotation != null) {
                    builder.title(fieldAnnotation.title()).tip(fieldAnnotation.tip());
                    builder.order(fieldAnnotation.order()).hide(fieldAnnotation.hide());
                    builder.element("input");
                    if (fieldAnnotation.largeText()) {
                        builder.element("textarea");
                        builder.htmlEditor(fieldAnnotation.htmlEditor());
                        builder.htmlEditUpload(fieldAnnotation.htmlEditorUpload());
                    }
                    if (field.getType().getTypeName().equalsIgnoreCase(String[].class.getTypeName())
                            || field.getType().getTypeName().equalsIgnoreCase(Integer[].class.getTypeName())
                            || field.getType().getTypeName().equalsIgnoreCase(Long[].class.getTypeName())
                            || field.getType().getTypeName().equalsIgnoreCase(Float[].class.getTypeName())
                            || field.getType().getTypeName().equalsIgnoreCase(Double[].class.getTypeName())
                            || field.getType().getTypeName().equalsIgnoreCase(Character[].class.getTypeName())
                    ) {
                        String[] strings = fieldAnnotation.listData();
                        builder.multiple(fieldAnnotation.multiple()).element("select");
                        if (strings.length > 1) {
                            if (strings.length > 1 && strings.length % 2 == 0) {
                                /**
                                 * 使用value初始化列表
                                 */
                                Set<FormOption> formOptionSet = new HashSet<>();
                                for (int i = 0; i < strings.length; i++) {
                                    FormOption.FormOptionBuilder formOptionBuilder = FormOption.builder();
                                    formOptionBuilder.id(strings[i ]).text(strings[i +1]);
                                    formOptionSet.add(formOptionBuilder.build());
                                    i++;
                                }
                                builder.formOption(formOptionSet);
                            } else {
                                log.error("生成表单{}错误未找到列表{}的数据源错误，数据名值需要配对，数量应为偶数", classAnnotation.title(), fieldAnnotation.listData());
                            }
                        } else {
                            String listBeanName = fieldAnnotation.listBeanName();
                            String listMethod = fieldAnnotation.listMethod();
                            if (!listBeanName.isEmpty() && !listMethod.isEmpty()) {
                                Object bean = applicationContext.getBean(listBeanName);
                                Method method = ReflectionUtils.findMethod(bean.getClass(), listMethod);
                                if ("java.util.Set<com.qintingfm.web.service.form.FormOption>".equalsIgnoreCase(method.getGenericReturnType().getTypeName())) {
                                    Set<FormOption> formOptions = (Set<FormOption>) ReflectionUtils.invokeMethod(method, bean);
                                    builder.formOption(formOptions);
                                } else {
                                    log.error("生成表单{}错误未找到列表{}的数据源,bean{},method{}", classAnnotation.title(), fieldAnnotation.title(), fieldAnnotation.listBeanName(), fieldAnnotation.listMethod());
                                }
                            }
                        }
                    }
                }
                try {
                    if (classDate != null) {
                        @SuppressWarnings("deprecation")
                        boolean accessible = field.isAccessible();
                        if (!accessible) {
                            field.setAccessible(true);
                        }
                        Object fieldData = field.get(classDate);
                        if (fieldData != null) {
                            if (field.getType() == Boolean.class) {
                                Boolean aBoolean = (Boolean) field.get(classDate);
                                if (aBoolean != null && aBoolean) {
                                    builder.value(boolTrue());
                                } else {
                                    builder.value(boolFalse());
                                }
                            } else if (field.getType() == String.class) {
                                builder.value((String) field.get(classDate));
                            } else if (field.getType() == Integer.class) {
                                builder.value(String.valueOf(field.get(classDate)));
                            } else if (field.getType() == Long.class) {
                                builder.value(String.valueOf(field.get(classDate)));
                            } else if (field.getType() == Float.class) {
                                builder.value(String.valueOf(field.get(classDate)));
                            } else if (field.getType() == Double.class) {
                                builder.value(String.valueOf(field.get(classDate)));
                            } else if (field.getType() == Character.class) {
                                builder.value(String.valueOf(field.get(classDate)));
                            } else if (field.getType() == Date.class) {
                                builder.element("date");
                                Date date = (Date) field.get(classDate);
                                if(date!=null){
                                    DateTimeFormat dateTimeFormatAnnotation = AnnotationUtils.getAnnotation(field, DateTimeFormat.class);
                                    if (dateTimeFormatAnnotation.pattern()!=null){
                                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(dateTimeFormatAnnotation.pattern());
                                        builder.value(simpleDateFormat.format(date));
                                        if(dateTimeFormatAnnotation.pattern().contains("HH:mm:ss")){
                                            builder.format("ymdTime");
                                        }else{
                                            builder.format("ymd");
                                        }
                                    }else{
                                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        builder.value(simpleDateFormat.format(date));
                                        builder.format("ymdTime");
                                    }
                                }
                            } else if (field.getType().getTypeName().equalsIgnoreCase(String[].class.getTypeName())) {
                                String[] strings = (String[]) field.get(classDate);
                                List<String> collect = Stream.of(strings).collect(Collectors.toList());
                                builder.listValue(strings).listKey(collect);
                            } else if (field.getType().getTypeName().equalsIgnoreCase(Integer[].class.getTypeName())) {
                                String[] strings = Stream.of((Integer[]) field.get(classDate)).map(item -> item.toString()).collect(Collectors.toList()).toArray(new String[0]);
                                List<String> collect = Stream.of(strings).map(item -> item.toString()).collect(Collectors.toList());
                                builder.listValue(strings).listKey(collect);
                            } else if (field.getType().getTypeName().equalsIgnoreCase(Long[].class.getTypeName())) {
                                String[] strings = Stream.of((Long[]) field.get(classDate)).map(item -> item.toString()).collect(Collectors.toList()).toArray(new String[0]);
                                List<String> collect = Stream.of(strings).collect(Collectors.toList());
                                builder.listValue(strings).listKey(collect);
                            } else if (field.getType().getTypeName().equalsIgnoreCase(Float[].class.getTypeName())) {
                                String[] strings = Stream.of((Float[]) field.get(classDate)).map(item -> item.toString()).collect(Collectors.toList()).toArray(new String[0]);
                                List<String> collect = Stream.of(strings).collect(Collectors.toList());
                                builder.listValue(strings).listKey(collect);
                            } else if (field.getType().getTypeName().equalsIgnoreCase(Double[].class.getTypeName())) {
                                String[] strings = Stream.of((Double[]) field.get(classDate)).map(item -> item.toString()).collect(Collectors.toList()).toArray(new String[0]);
                                List<String> collect = Stream.of(strings).collect(Collectors.toList());
                                builder.listValue(strings).listKey(collect);
                            } else if (field.getType().getTypeName().equalsIgnoreCase(Character[].class.getTypeName())) {
                                String[] strings = Stream.of((Character[]) field.get(classDate)).map(item -> item.toString()).collect(Collectors.toList()).toArray(new String[0]);
                                List<String> collect = Stream.of(strings).collect(Collectors.toList());
                                builder.listValue(strings).listKey(collect);
                            } else if (field.getType() == Byte.class) {
                                throw new IllegalAccessException("不能处理Byte类型");
                            }
                            if (!accessible) {
                                field.setAccessible(false);
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    log.warn("获取表单数据值出错{}", field.getName());
                }
                fromFields.add(builder.build());
            }
            if (tmpClass == BaseVo.class) {
                break;
            }
            tmpClass = tmpClass.getSuperclass();
        }
        Collections.sort(fromFields);
        builder1.formItems(fromFields);
        return builder1.build();
    }

    public Boolean value2Boolean(String value) {
        String stringTrue = "TRUE";
        String yes = "yes";
        String num1 = "1";
        String y = "Y";
        return yes.equalsIgnoreCase(value) || y.equalsIgnoreCase(value) || stringTrue.equalsIgnoreCase(value) || num1.equalsIgnoreCase(value);
    }

    public String boolTrue() {
        return "TRUE";
    }

    public String boolFalse() {
        return "FALSE";
    }
}
