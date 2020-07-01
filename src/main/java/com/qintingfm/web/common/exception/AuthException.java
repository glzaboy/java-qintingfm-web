package com.qintingfm.web.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author guliuzhong
 */
public class AuthException extends AuthenticationException {


    public AuthException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthException(String msg) {
        super(msg);
    }
}
