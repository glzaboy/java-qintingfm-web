package com.qintingfm.web.spider;

import com.qintingfm.web.settings.SettingData;
import com.qintingfm.web.settings.SettingField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 百度搜索配置
 *
 * @author guliuzhong
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SettingField("百度推送")
public class BaiduSpiderSetting extends SettingData {
    @SettingField(value = "站点名称")
    String site;
    @SettingField(value = "推送KEY")
    String token;
}
