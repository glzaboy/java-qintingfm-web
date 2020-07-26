package com.qintingfm.web.form.Annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author guliuzhong
 */
@Target({ ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FormAnnotation {
    @AliasFor("value")
    String action() default "";
    @AliasFor("action")
    String value() default "";
    String method()   default "get";
    String title()   default "";
    String message()   default "";
}
