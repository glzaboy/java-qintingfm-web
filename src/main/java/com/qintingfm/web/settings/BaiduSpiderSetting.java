package com.qintingfm.web.settings;

import lombok.Data;
import lombok.ToString;

/**
 * @author guliuzhong
 */
@Data
@ToString(callSuper = true)
public class BaiduSpiderSetting  extends SettingData{
    String site;
    String token;
}
