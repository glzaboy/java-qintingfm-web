package com.qintingfm.web.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qintingfm.web.service.form.annotation.FieldAnnotation;
import com.qintingfm.web.service.form.annotation.FormAnnotation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.TimeZone;

/**
 * 类别
 * @author guliuzhong
 */
@FormAnnotation(method = "post",hideSubmit = false,value = "小程序应用配置")
@Data
@EqualsAndHashCode(callSuper = true)
public class MiniAppVo extends BaseVo {
    @FieldAnnotation(title = "appId",tip = "",order = 1,hide = true)
    Integer id;
    @FieldAnnotation(title = "程序类型",tip = "",order = 2,listData ={"weChat","微信小程序"})
    String[] type;
    @FieldAnnotation(title = "应用平台Id",tip = "",order = 3)
    @NotBlank(message = "应用平台Id，不能为空")
    String appId;
    @FieldAnnotation(title = "应用平台密钥",tip = "",order = 4)
    @NotBlank(message = "应用平台密钥，不能为空")
    String appSecret;
    @FieldAnnotation(title = "启用",tip = "",order = 5)
    Boolean enable=false;
    @FieldAnnotation(title = "创建日期",tip = "",order = 5)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    Date createDate;
}
