package com.qintingfm.web.pojo;

import com.qintingfm.web.pojo.vo.BaseVo;
import com.qintingfm.web.service.form.annotation.FieldAnnotation;
import com.qintingfm.web.service.form.annotation.FieldSelectAnnotation;
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
    @FieldAnnotation(title = "小程序名称",tip = "",order = 0,hide = true)
    Integer appId;
    @FieldAnnotation(title = "类型",tip = "",order = 1)
    @FieldSelectAnnotation(bean = "wxService",method = "getSelect")
    String type;
    @FieldAnnotation(title = "描述",tip = "",order = 2)
    String appkey;
    @FieldAnnotation(title = "描述",tip = "",order = 2)
    String seckey;
}
