package com.qintingfm.web.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

/**
 * 业务异常类
 * @author guliuzhong
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException{
    private final Set<Business> businesses;

    public BusinessException(Set<Business> businesses) {
        this.businesses = businesses;
    }
    public BusinessException(String message) {
        super(message);
        this.businesses = new HashSet<>();
    }
    public BusinessException(String message, Set<Business> businesses) {
        super(message);
        this.businesses = businesses;
    }

    public BusinessException(String message, Throwable cause, Set<Business> businesses) {
        super(message, cause);
        this.businesses = businesses;
    }

    public BusinessException(Throwable cause, Set<Business> businesses) {
        super(cause);
        this.businesses = businesses;
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Set<Business> businesses) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.businesses = businesses;
    }
}
