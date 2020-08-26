package com.qintingfm.web.service.form.annotation;

import com.qintingfm.web.service.form.FormSelect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author guliuzhong
 */
@Target({ ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(Qualifier.class)
@Documented
public @interface FieldSelectAnnotation {
    /**
     * 字段标题
     */
    String[] value() default "";
    String bean();
    String method();
}
