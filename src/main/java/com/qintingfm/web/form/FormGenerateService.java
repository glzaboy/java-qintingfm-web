package com.qintingfm.web.form;

import com.qintingfm.web.form.annotation.FieldAnnotation;
import com.qintingfm.web.form.annotation.FormAnnotation;
import com.qintingfm.web.pojo.vo.BaseVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 表单生成器
 * @author guliuzhong
 */
@Data
@Slf4j
@Service
public class FormGenerateService {
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
