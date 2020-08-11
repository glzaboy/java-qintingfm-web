package com.qintingfm.web.pojo.vo.settings;

import com.qintingfm.web.form.annotation.FieldAnnotation;
import com.qintingfm.web.form.annotation.FormAnnotation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * 关键词设置
 *
 * @author guliuzhong
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FormAnnotation(title = "站点功能", method = "post")
public class SiteSettingVo extends SettingDataVo implements Serializable {
    @FieldAnnotation(value = "站点标题", order = 1)
    String title;
    @FieldAnnotation(value = "固定关键词", tip = "使用逗号进行分隔", order = 2)
    String fixKeyWord;
    @FieldAnnotation(value = "固定描述", order = 2)
    String fixDescription;
    @FieldAnnotation(value = "站点内容URL前缀", tip = "填写您希望对外公布的URL地址，最后结尾不需要带/", order = 3)
    String mainUrl;
    @FieldAnnotation(value = "开启MetaWebLog服务", tip = "开启后可以使用标准的xmlrpc服务发布内容", order = 4)
    Boolean enableMetaWebLog;
}
