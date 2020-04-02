package com.qintingfm.web.service;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.*;
import java.util.Set;

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
        Set<ConstraintViolation<T>> validate = validator.validate(tPojo);
        return validate;
    }

    public <T> void validatePojoAndThrow(T tPojo) throws ConstraintViolationException {
        Set<ConstraintViolation<T>> validate = validator.validate(tPojo);
        if (!validate.isEmpty()) {
            throw new ConstraintViolationException(validate);
        }
    }
}
