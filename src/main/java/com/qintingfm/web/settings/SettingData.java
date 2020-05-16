package com.qintingfm.web.settings;

import lombok.Data;

/**
 * @author guliuzhong
 */
@Data
public class SettingData {
    @SettingField(value = "启用")
    Boolean enable;
    @SettingField(value = "配置名称")
    String settingName;
}
