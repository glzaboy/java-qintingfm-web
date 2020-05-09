package com.qintingfm.web.service;

import com.qintingfm.web.common.exception.Business;
import com.qintingfm.web.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 服务基础类
 *
 * @author guliuzhong
 */
public class BaseService {
    private Validator validator;


    @Autowired
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> Set<ConstraintViolation<T>> validatePojo(T tPojo) {
        return validator.validate(tPojo);
    }

    public <T> void validatePojoAndThrow(T tPojo) throws ConstraintViolationException {
        Set<ConstraintViolation<T>> validate = validator.validate(tPojo);
        if (!validate.isEmpty()) {
            throw new ConstraintViolationException(validate);
        }
    }



    public void buildAndThrowBusinessException(Class<?> classzz,Set<Business> exceptionSet) {
        Set<Business> businessSet = buildBusiness(classzz, exceptionSet);
        if(businessSet!=null && businessSet.size()>0){
            throw new BusinessException(businessSet);
        }
    }
    public Set<Business> buildBusiness(Class<?> classzz,Set<Business> exceptionSet){
        List<String> collect = Stream.of(classzz.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
        Set<Business> businessSet=new HashSet<>();
        //ConcurrentHashMap<String,String> concurrentHashMap=new ConcurrentHashMap<>(7);
        exceptionSet.forEach((setItem)->{
            Optional<String> first = collect.stream().filter(item -> item.toLowerCase().equalsIgnoreCase(setItem.getField().toLowerCase())).findFirst();
            Business.BusinessBuilder builder = Business.builder();
            builder.message(setItem.getMessage());
            builder.field( first.orElseGet(setItem::getField));
            businessSet.add(builder.build());
        });
        return businessSet;
    }
}
