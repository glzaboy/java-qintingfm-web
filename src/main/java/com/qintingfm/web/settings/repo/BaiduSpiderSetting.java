package com.qintingfm.web.settings.repo;

import com.qintingfm.web.form.Annotation.FieldAnnotation;
import com.qintingfm.web.form.Annotation.FormAnnotation;
import com.qintingfm.web.settings.SettingData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * 百度搜索配置
 *
 * @author guliuzhong
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FormAnnotation(title = "百度推送",method = "post")
public class BaiduSpiderSetting extends SettingData implements Serializable {
    @FieldAnnotation(value = "启用")
    Boolean enable;
    @FieldAnnotation(value = "站点名称",tip = "请填写需要推送的域名，不包含http://，如您是通过http://baidu.com/xx.com进行访问，请填写baidu.com")
    String site;
    @FieldAnnotation(value = "推送KEY")
    String token;

}
