package com.qintingfm.web.config;

import com.qintingfm.web.common.AjaxDto;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 增加全局控制器切面
 * @author guliuzhong
 */
@ControllerAdvice
public class WebControllerAdvice {
    @ExceptionHandler(TransactionSystemException.class)
    @ResponseBody
    public AjaxDto c(final TransactionSystemException ex){
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException)) {
            t = t.getCause();
        }
        AjaxDto ajaxDto=new AjaxDto();
        if (t instanceof  ConstraintViolationException){
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) t).getConstraintViolations();
            Map<String, String> collect = constraintViolations.stream().collect(Collectors.toMap(k -> {
                return k.getPropertyPath().toString();
            }, i -> i.getMessage()));
            ajaxDto.setError(collect);
        }

        ajaxDto.setMessage("请求出错");
        return ajaxDto;

    }
    @ExceptionHandler(RollbackException.class)
    public AjaxDto c(final RollbackException ex){
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException)) {
            t = t.getCause();
        }
        AjaxDto ajaxDto=new AjaxDto();
        if (t instanceof  ConstraintViolationException){
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) t).getConstraintViolations();
            Map<String, String> collect = constraintViolations.stream().collect(Collectors.toMap(k -> {
                return k.getPropertyPath().toString();
            }, i -> i.getMessage()));
            ajaxDto.setError(collect);
        }

        ajaxDto.setMessage("请求出错");
        return ajaxDto;

    }
}
