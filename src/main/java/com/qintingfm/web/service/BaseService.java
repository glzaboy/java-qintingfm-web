package com.qintingfm.web.service;

import com.qintingfm.web.common.exception.Business;
import com.qintingfm.web.common.exception.BusinessException;
import com.qintingfm.web.pojo.vo.BaseVo;
import com.qintingfm.web.pojo.vo.MiniAppVo;
import com.qintingfm.web.pojo.vo.settings.SiteSettingVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 服务基础类
 *
 * @author guliuzhong
 */
@Slf4j
public class BaseService {
    private Validator validator;
    public SettingService settingService;

    @Autowired
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    @Autowired
    public void setSettingService(SettingService settingService) {
        this.settingService = settingService;
    }

    public <T> Set<ConstraintViolation<T>> validatePojo(T tPojo) {
        return validator.validate(tPojo);
    }

    public <T> void validatePojoAndThrow(T tPojo) throws ConstraintViolationException {
        Set<ConstraintViolation<T>> validate = validatePojo(tPojo);
        if (!validate.isEmpty()) {
            throw new ConstraintViolationException(validate);
        }
    }


    public void buildAndThrowBusinessException(String exceptionString) {
        throw new BusinessException(exceptionString);
    }

    public void buildAndThrowBusinessException(Class<?> classzz, Set<Business> exceptionSet, String exceptionString) {
        Set<Business> businessSet = buildBusiness(classzz, exceptionSet);
        if (businessSet != null && businessSet.size() > 0) {
            throw new BusinessException(exceptionString, exceptionSet);
        }
    }

    public void buildAndThrowBusinessException(Class<?> classzz, Set<Business> exceptionSet) {
        Set<Business> businessSet = buildBusiness(classzz, exceptionSet);
        if (businessSet != null && businessSet.size() > 0) {
            throw new BusinessException(businessSet);
        }
    }

    public Set<Business> buildBusiness(Class<?> classzz, Set<Business> exceptionSet) {
        List<String> collect = Stream.of(classzz.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
        Set<Business> businessSet = new HashSet<>();
        exceptionSet.forEach((setItem) -> {
            Optional<String> first = collect.stream().filter(item -> item.toLowerCase().equalsIgnoreCase(setItem.getField().toLowerCase())).findFirst();
            Business.BusinessBuilder builder = Business.builder();
            builder.message(setItem.getMessage());
            builder.field(first.orElseGet(setItem::getField));
            businessSet.add(builder.build());
        });
        return businessSet;
    }

    /**
     * 获取站点配置类
     * @return 如果设置了配置返回配置否则返回一个未设置任何属性的对象
     */
    public SiteSettingVo getSiteSetting(){
        Optional<SiteSettingVo> site = settingService.getSettingBean("site", SiteSettingVo.class);
        SiteSettingVo siteSetting=new SiteSettingVo();
        return site.orElse(siteSetting);
    }
    public <T extends Object,R extends Object> R copyFunction(T t,Function<T,R> function){
        return function.apply(t);
    }
    public <T extends Object,R extends Object> R copyFunction(T t,Class<R> rxClass){
        Function<T,R> function=(item)->{
            R r = null;
            try {
                r = rxClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException e) {
                log.error("create object fail {}",e.getMessage());
            } catch (IllegalAccessException e) {
                log.error("create object fail {}",e.getMessage());
            } catch (InvocationTargetException e) {
                log.error("create object fail {}",e.getMessage());
            } catch (NoSuchMethodException e) {
                log.error("create object fail {}",e.getMessage());
            }
            BeanUtils.copyProperties(item,r);
            return r;
        };
        return copyFunction(t,function);
    }
}
