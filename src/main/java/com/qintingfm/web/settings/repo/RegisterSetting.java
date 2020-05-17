package com.qintingfm.web.settings.repo;

import com.qintingfm.web.settings.SettingData;
import com.qintingfm.web.settings.SettingField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author guliuzhong
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SettingField("注册设置")
public class RegisterSetting extends SettingData {
    @SettingField(value = "启用")
    Boolean enable;
    @SettingField(value = "禁止注册提示")
    Boolean disableRegisterTip;
}
