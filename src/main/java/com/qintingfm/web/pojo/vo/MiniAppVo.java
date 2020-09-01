package com.qintingfm.web.pojo.vo;

import com.qintingfm.web.pojo.vo.BaseVo;
import com.qintingfm.web.service.form.annotation.FieldAnnotation;
import com.qintingfm.web.service.form.annotation.FormAnnotation;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类别
 * @author guliuzhong
 */
@FormAnnotation(method = "post",hideSubmit = true,value = "小程序应用配置")
@Data
@EqualsAndHashCode(callSuper = true)
public class MiniAppVo extends BaseVo {
    @FieldAnnotation(title = "appId",tip = "",order = 1,hide = false)
    Integer appId;
    @FieldAnnotation(title = "程序类型",tip = "",order = 2,listData ={"weChat","微信小程序"})
    String[] type;
    String appKey;
    String appSec;
}
