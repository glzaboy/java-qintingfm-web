package com.qintingfm.web.settings.repo;

import com.qintingfm.web.settings.SettingData;
import com.qintingfm.web.settings.SettingField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * 关键词设置
 * @author guliuzhong
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SettingField("站点功能")
public class SiteSetting extends SettingData implements Serializable {
    @SettingField(value = "站点标题")
    String title;
    @SettingField(value = "固定关键词",tip = "使用逗号进行分隔")
    String fixKeyWord;
    @SettingField(value = "固定描述")
    String fixDescription;
    @SettingField(value = "站点内容URL前缀",tip = "填写您希望对外公布的URL地址，最后结尾不需要带/")
    String mainUrl;
    @SettingField(value = "开启MetaWebLog服务",tip = "开启后可以使用标准的xmlrpc服务发布内容")
    Boolean enableMetaWebLog;

}
