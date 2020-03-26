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
        AjaxDto ajaxDto=new AjaxDto();
        do{
            if( t instanceof  ConstraintViolationException){
                Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) t).getConstraintViolations();
                Map<String, String> collect = constraintViolations.stream().collect(Collectors.toMap(k -> {
                    return k.getPropertyPath().toString();
                }, i -> i.getMessage(),(v1,v2)->v1+","+v2));
                ajaxDto.setError(collect);
                ajaxDto.setAutoHide("3");
                ajaxDto.setMessage("您的操作出错，请正确填写表单内容");
            }
        }while ((t=t.getCause())!=null);
        return ajaxDto;
    }
}
