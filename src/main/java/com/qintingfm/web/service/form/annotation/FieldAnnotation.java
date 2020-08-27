package com.qintingfm.web.service.form.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author guliuzhong
 */
@Target({ ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldAnnotation{
    /**
     * 字段标题
     */
    @AliasFor("title")
    String value() default "";
    /**
     * 字段标题
     */
    @AliasFor("value")
    String title() default "";
    /**
     * 提示信息
     * @return
     */
    String tip()   default "";

    /**
     * 字段排序
     * @return
     */
    int order() default 0;

    /**
     * 是否隐藏
     * @return
     */
    boolean hide() default false;
    /**
     * 是大段文本
     * @return
     */
    boolean largeText() default false;

    /**
     * 使用HTML编辑器
     * @return
     */
    boolean htmlEditor() default false;
    /**
     * 上传文件仅HTML生效
     * @return
     */
    boolean htmlEditorUpload() default false;
    /**
     * list类型数据列表
     */
    String[] listData() default "";

    /**
     * list读取的bean name
     * @return
     */
    String listBeanName() default "";
    /**
     * 使用bean 时读取数据方法，要求方法
     * 需要无参数且返回Set<FormSelect>数据
     * @return
     */
    String listMethod() default "";

    boolean multiple() default false;

}
