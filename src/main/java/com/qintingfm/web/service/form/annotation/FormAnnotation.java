package com.qintingfm.web.service.form.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author guliuzhong
 */
@Target({ ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FormAnnotation {
    @AliasFor("title")
    String value() default "";
    @AliasFor("value")
    String title()   default "";
    String method()   default "get";
    String message()   default "";
    boolean hideSubmit() default false;
}
