package com.qintingfm.web.pojo.vo.settings;

import com.qintingfm.web.service.form.annotation.FieldAnnotation;
import com.qintingfm.web.service.form.annotation.FormAnnotation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * 敏感关键词
 * @author guliuzhong
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FormAnnotation(title = "敏感关键词", method = "post")
public class SensitiveWordVo extends SettingDataVo implements Serializable {
    @FieldAnnotation(value = "敏感关键词",order = 1,largeText = true)
    String sensitiveWord;
}
