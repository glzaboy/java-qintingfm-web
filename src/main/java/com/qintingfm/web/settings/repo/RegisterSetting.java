package com.qintingfm.web.settings.repo;

import com.qintingfm.web.form.Annotation.FieldAnnotation;
import com.qintingfm.web.form.Annotation.FormAnnotation;
import com.qintingfm.web.settings.SettingData;
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
public class RegisterSetting extends SettingData implements Serializable {
    @FieldAnnotation(value = "启用",order = 1)
    Boolean enable;
    @FieldAnnotation(value = "禁止注册提示",order = 2)
    String disableRegisterTip;
}
