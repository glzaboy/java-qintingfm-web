package com.qintingfm.web.service;

import com.qintingfm.web.service.form.Form;
import com.qintingfm.web.service.form.FormSelect;
import com.qintingfm.web.service.form.annotation.FieldAnnotation;
import com.qintingfm.web.service.form.annotation.FieldSelectAnnotation;
import com.qintingfm.web.service.form.annotation.FormAnnotation;
import com.qintingfm.web.pojo.vo.BaseVo;
import com.qintingfm.web.service.form.FormItem;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 表单生成器
 * @author guliuzhong
 */
@Data
@Slf4j
@Service
public class FormGenerateService implements ApplicationContextAware {
    ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    public <T extends BaseVo> Form generalFormData(T classData){
        return generalForm(null,classData);
    }
    public Form generalForm(Class classic){
        return generalForm(classic,null);
    }
    public Form generalForm(Class classic,Object classDate){
        Form.FormBuilder builder1 = Form.builder();
        List<FormItem> fromFields=new ArrayList<>();
        Class<?> tmpClass=classic!=null?classic:classDate.getClass();
        FormAnnotation classAnnotation = AnnotationUtils.getAnnotation(tmpClass, FormAnnotation.class);
        if(classAnnotation!=null){
            builder1.title(classAnnotation.title()).hideSubmit(classAnnotation.hideSubmit());
            builder1.message(classAnnotation.message()).method(classAnnotation.method());
        }
        while (tmpClass!=null){

            Field[] declaredFields = tmpClass.getDeclaredFields();
            FormItem.FormItemBuilder builder = FormItem.builder();
            for (Field field:declaredFields) {
                builder.fieldName(field.getName());
                builder.className(field.getType().getSimpleName());
                FieldAnnotation fieldAnnotation = AnnotationUtils.getAnnotation(field, FieldAnnotation.class);
                if (fieldAnnotation!=null){
                    builder.title(fieldAnnotation.title()).tip(fieldAnnotation.tip());
                    builder.order(fieldAnnotation.order()).hide(fieldAnnotation.hide());
                    builder.largeText(fieldAnnotation.largeText()).useHtml(fieldAnnotation.useHtml());
                    builder.uploadFile(fieldAnnotation.uploadFile());
                }
                FieldSelectAnnotation fieldSelectAnnotation = AnnotationUtils.getAnnotation(field, FieldSelectAnnotation.class);
                if(fieldSelectAnnotation!=null){
                    String[] value = fieldSelectAnnotation.value();

                    if(value.length>1 && value.length%2==0){
                        /**
                         * 使用value初始化列表
                         */
                        Set<FormSelect> formSelectSet=new HashSet<>();
                        for(int i=0;i<value.length/2;i++){
                            FormSelect.FormSelectBuilder formSelectBuilder = FormSelect.builder();
                            formSelectBuilder.key(value[i*2]).value(value[i*2+1]);
                            formSelectSet.add(formSelectBuilder.build());
                        }
                        builder.formSelectSet(formSelectSet);
                    }else{
                        Object bean = applicationContext.getBean(fieldSelectAnnotation.bean());
                        Class<?> aClass = bean.getClass();
                        Method method1 = ReflectionUtils.findMethod(aClass, fieldSelectAnnotation.method());
                        Set<FormSelect> formSelects =(Set<FormSelect>)ReflectionUtils.invokeMethod(method1,bean);
                        builder.formSelectSet(formSelects);
                    }

                }
                try {
                    if(classDate !=null){
                        @SuppressWarnings("deprecation")
                        boolean accessible = field.isAccessible();
                        if(!accessible){
                            field.setAccessible(true);
                        }
                        if(field.getType()==Boolean.class){
                            Boolean aBoolean = (Boolean) field.get(classDate);
                            if(aBoolean!=null && aBoolean){
                                builder.value(boolTrue());
                            }else{
                                builder.value(boolFalse());
                            }
                        } else if(field.getType()==String.class){
                            builder.value( (String) field.get(classDate));
                        }else if(field.getType()==Integer.class){
                            builder.value( String.valueOf(field.get(classDate)));
                        }else if(field.getType()==Long.class){
                            builder.value( String.valueOf(field.get(classDate)));
                        }else if(field.getType()==Float.class){
                            builder.value( String.valueOf(field.get(classDate)));
                        }else if(field.getType()==Double.class){
                            builder.value( String.valueOf(field.get(classDate)));
                        }else if(field.getType()==Character.class){
                            builder.value( String.valueOf(field.get(classDate)));
                        }else if(field.getType()==Byte.class){
                            throw new IllegalAccessException("不能处理Byte类型");
                        }
                        if(!accessible){
                            field.setAccessible(false);
                        }
                    }
                } catch (IllegalAccessException e) {
                    log.warn("获取表单数据值出错{}",field.getName());
                }
                fromFields.add(builder.build());
            }
            if (tmpClass == BaseVo.class) {
                break;
            }
            tmpClass=tmpClass.getSuperclass();
        }
        Collections.sort(fromFields);
        builder1.formItems(fromFields);
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
