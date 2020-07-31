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
@FormAnnotation(title = "百度搜索引擎推送",method = "post")
public class BaiduSpiderSetting extends SettingData implements Serializable {
    @FieldAnnotation(title = "启用",order = 1)
    Boolean enable;
    @FieldAnnotation(title = "站点名称",tip = "请填写需要推送的域名，不包含http://，如您是通过http://baidu.com/xx.com进行访问，请填写baidu.com",order = 2)
    String site;
    @FieldAnnotation(title = "推送KEY",order = 3)
    String token;

}
