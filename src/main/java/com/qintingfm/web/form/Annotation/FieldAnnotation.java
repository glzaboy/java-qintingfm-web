package com.qintingfm.web.form.Annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author guliuzhong
 */
@Target({ ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldAnnotation{
    @AliasFor("title")
    String value() default "";
    @AliasFor("value")
    String title() default "";
    String tip()   default "";
    int Order() default 0;
}
