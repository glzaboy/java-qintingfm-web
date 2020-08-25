package com.qintingfm.web.pojo.vo.settings;

import com.qintingfm.web.service.form.annotation.FieldAnnotation;
import com.qintingfm.web.pojo.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author guliuzhong
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SettingDataVo extends BaseVo {
    @FieldAnnotation(value = "配置名称",order = 0)
    String settingName;
}
