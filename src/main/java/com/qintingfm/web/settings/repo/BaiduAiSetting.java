package com.qintingfm.web.settings.repo;

import com.qintingfm.web.form.Annotation.FieldAnnotation;
import com.qintingfm.web.form.Annotation.FormAnnotation;
import com.qintingfm.web.settings.SettingData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FormAnnotation(title = "百度云应用设置",method = "post")
public class BaiduAiSetting extends SettingData implements Serializable {
    @FieldAnnotation(value = "百度云应用的AK",tip = "小程序的应用需要调用百度云接口，官方帮助https://ai.baidu.com/ai-doc/REFERENCE/Ck3dwjgn3",order=2)
    String clientId;
    @FieldAnnotation(value = "百度云应用的SK",order=3)
    String clientSecret;
}
