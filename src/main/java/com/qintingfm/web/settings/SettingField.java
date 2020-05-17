package com.qintingfm.web.settings;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author guliuzhong
 */
@Target({ ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SettingField {
    @AliasFor("title")
    String value() default "";
    @AliasFor("value")
    String title() default "";
    String tip()   default "";
}
