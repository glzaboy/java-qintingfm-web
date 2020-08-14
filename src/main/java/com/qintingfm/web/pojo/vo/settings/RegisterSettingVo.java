package com.qintingfm.web.pojo.vo.settings;

import com.qintingfm.web.service.form.annotation.FieldAnnotation;
import com.qintingfm.web.service.form.annotation.FormAnnotation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author guliuzhong
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FormAnnotation(title = "注册设置",method = "post")
public class RegisterSettingVo extends SettingDataVo implements Serializable {
    @FieldAnnotation(value = "启用",order = 1)
    Boolean enable;
    @FieldAnnotation(value = "禁止注册提示",order = 2,largeText = true,useHtml = true)
    String disableRegisterTip;
}
