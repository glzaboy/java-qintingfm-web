package com.qintingfm.web;

import com.qintingfm.web.common.exception.Business;
import com.qintingfm.web.common.exception.BusinessException;
import com.qintingfm.web.pojo.request.BlogPojo;
import com.qintingfm.web.pojo.request.UserRegisterPojo;
import com.qintingfm.web.service.BlogService;
import com.qintingfm.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Transient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@Slf4j
public class UserServiceTests {
    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Test
    @Rollback(value = false)
    @Transactional(rollbackFor = {Exception.class})
    public void userRegister() throws MessagingException {
        UserRegisterPojo.UserRegisterPojoBuilder builder = UserRegisterPojo.builder();
        builder.tel("123456");
        builder.userName("123456");
        builder.email("glzaboy@163.com");
        userService.register(builder.build());

    }
}
