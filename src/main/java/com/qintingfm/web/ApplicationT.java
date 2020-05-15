package com.qintingfm.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 应用程序启动类
 *
 * @author guliuzhong
 */
public class ApplicationT {
    public static void main(String[] args) {
//        try {
//            Class<?> aClass = ApplicationT.class.getClassLoader().loadClass("com.qintingfm.web.jpa.entity.SettingInfo");
////            Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();
////            Object o = declaredConstructor.newInstance();
////            System.out.println(o);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
    }
}
