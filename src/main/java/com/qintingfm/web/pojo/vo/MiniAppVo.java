package com.qintingfm.web.pojo.vo;

import com.qintingfm.web.pojo.vo.BaseVo;
import com.qintingfm.web.service.form.annotation.FieldAnnotation;
import com.qintingfm.web.service.form.annotation.FormAnnotation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 类别
 * @author guliuzhong
 */
@FormAnnotation(method = "post",hideSubmit = false,value = "小程序应用配置")
@Data
@EqualsAndHashCode(callSuper = true)
public class MiniAppVo extends BaseVo {
    @FieldAnnotation(title = "appId",tip = "",order = 1,hide = false)
    @NotNull(message = "testasdfs")
    Integer appId;
    @FieldAnnotation(title = "程序类型",tip = "",order = 2,listData ={"weChat","微信小程序"})
    String[] type;
    @FieldAnnotation(title = "程序类型",tip = "",order = 2,largeText = true)
    String appKey;
    @FieldAnnotation(title = "程序类型",tip = "",order = 2,largeText = true,htmlEditorUpload = true)
    @NotBlank(message = "程序类型，不能为空")
    String appSec;
    @FieldAnnotation(title = "程序类型",tip = "",order = 2,largeText = true,htmlEditor = true)
    String appSec2;
    @FieldAnnotation(title = "程序类型",tip = "",order = 2,largeText = true)
    String appSec3;
}
