package com.qintingfm.web.spider;

import com.qintingfm.web.settings.SettingData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 百度搜索配置
 * @author guliuzhong
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaiduSpiderSetting  extends SettingData {
    String site;
    String token;
}
