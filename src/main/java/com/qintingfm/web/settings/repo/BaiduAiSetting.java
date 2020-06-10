package com.qintingfm.web.settings.repo;

import com.qintingfm.web.settings.SettingData;
import com.qintingfm.web.settings.SettingField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SettingField("百度云应用设置")
public class BaiduAiSetting extends SettingData implements Serializable {
    @SettingField(value = "百度云应用的AK",tip = "小程序的应用需要调用百度云接口，官方帮助https://ai.baidu.com/ai-doc/REFERENCE/Ck3dwjgn3")
    String clientId;
    @SettingField(value = "百度云应用的SK")
    String clientSecret;
}
