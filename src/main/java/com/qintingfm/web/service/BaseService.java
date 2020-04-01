package com.qintingfm.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Constants;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * 服务基础类
 * @author guliuzhong
 */
public class BaseService {
    private Validator validator;

    @Autowired
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    public <T> Set<ConstraintViolation<T>> validatePojo(T tPojo) {
        Set<ConstraintViolation<T>> validate = validator.validate(tPojo);
        return validate;
    }
    public <T>  void validatePojoAndThrow(T tPojo) throws Constants.ConstantException {
        Set<ConstraintViolation<T>> validate = validator.validate(tPojo);
        if (!validate.isEmpty()){
            throw new ConstraintViolationException(validate);
        }
    }
}
