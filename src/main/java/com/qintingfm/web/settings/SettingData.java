package com.qintingfm.web.settings;

import lombok.Data;

import java.io.Serializable;

/**
 * @author guliuzhong
 */
@Data
public class SettingData implements Serializable {

    @SettingField(value = "配置名称")
    String settingName;
}
