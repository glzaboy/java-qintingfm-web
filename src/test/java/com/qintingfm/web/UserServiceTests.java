package com.qintingfm.web;

import com.qintingfm.web.pojo.request.UserRegisterPojo;
import com.qintingfm.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import java.util.Set;

@SpringBootTest
@Slf4j
public class UserServiceTests {
    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Test
    @Rollback()
    @Transactional(rollbackFor = {Exception.class})
    public void userRegister() {
        UserRegisterPojo.UserRegisterPojoBuilder builder = UserRegisterPojo.builder();
        builder.tel("123456");
        builder.userName("12345679");
        builder.email("glzaboy18@163.com");
        userService.register(builder.build());

    }
}
