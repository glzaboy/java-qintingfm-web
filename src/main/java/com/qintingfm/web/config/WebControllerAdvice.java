package com.qintingfm.web.config;

import com.qintingfm.web.common.AjaxDto;
import com.qintingfm.web.common.exception.Business;
import com.qintingfm.web.common.exception.BusinessException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 增加全局控制器切面
 *
 * @author guliuzhong
 */
@ControllerAdvice
public class WebControllerAdvice {
    @ExceptionHandler(TransactionSystemException.class)
    @ResponseBody
    public AjaxDto transactionSystemException(final TransactionSystemException ex) {
        Throwable t = ex.getCause();
        AjaxDto ajaxDto = new AjaxDto();
        do {
            if (t instanceof ConstraintViolationException) {
                ajaxDto=constraintViolationException((ConstraintViolationException)t);
            }
            if (t instanceof BusinessException) {
                ajaxDto=businessException( (BusinessException) t );
            }
        } while ((t = t.getCause()) != null);
        return ajaxDto;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public AjaxDto constraintViolationException(final ConstraintViolationException ex) {
        AjaxDto ajaxDto = new AjaxDto();
        if (ex != null) {
            Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
            Map<String, String> collect = constraintViolations.stream().collect(Collectors.toMap(k -> k.getPropertyPath().toString(), ConstraintViolation::getMessage, (v1, v2) -> v1 + "," + v2));
            ajaxDto.setError(collect);
            ajaxDto.setAutoHide("3");
            ajaxDto.setMessage("您的操作出错，请正确填写表单内容");
        }
        return ajaxDto;
    }
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public AjaxDto businessException(final BusinessException ex) {
        AjaxDto ajaxDto = new AjaxDto();
        if (ex != null) {
            Set<Business> businesses = ex.getBusinesses();
            Map<String, String> collect = businesses.stream().collect(Collectors.toMap(Business::getField, Business::getMessage, (v1, v2) -> v1 + "," + v2));
            ajaxDto.setError(collect);
            ajaxDto.setAutoHide("3");
            if(ex.getMessage()!=null){
                ajaxDto.setMessage(ex.getMessage());
            }else {
                ajaxDto.setMessage("您的操作出错，请正确填写表单内容");
            }

        }
        return ajaxDto;
    }
}
