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
    boolean useHtml() default false;
    /**
     * 上传文件仅HTML生效
     * @return
     */
    boolean uploadFile() default false;
}
