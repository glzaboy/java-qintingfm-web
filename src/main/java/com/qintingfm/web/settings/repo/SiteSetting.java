package com.qintingfm.web.settings.repo;

import com.qintingfm.web.settings.SettingData;
import com.qintingfm.web.settings.SettingField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 关键词设置
 * @author guliuzhong
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SettingField("站点功能")
public class SiteSetting extends SettingData {
    @SettingField(value = "站点标题")
    String title;
    @SettingField(value = "固定关闭词",tip = "使用逗号进行分隔")
    String fixKeyWord;
    @SettingField(value = "固定描述")
    String fixDescription;
}
