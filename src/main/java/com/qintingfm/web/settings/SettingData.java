package com.qintingfm.web.settings;

import com.qintingfm.web.form.annotation.FieldAnnotation;
import com.qintingfm.web.pojo.vo.BaseVo;
import lombok.Data;


/**
 * @author guliuzhong
 */
@Data
public class SettingData extends BaseVo {
    @FieldAnnotation(value = "配置名称",order = 0)
    String settingName;
}
