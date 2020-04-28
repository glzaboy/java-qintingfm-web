package com.qintingfm.web.pojo.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 用户注册表单
 * @author guliuzhong
 */
@Data
@Builder
public class UserRegisterPojo {
    @NotBlank(message = "用户名必须填写")
    String userName;
    @Email(message = "邮件不合法")
    @NotBlank(message = "邮件必须填写")
    String email;
    @NotBlank(message = "电话不允许为空")
    String tel;

}
