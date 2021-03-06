package com.qintingfm.web.pojo;

import com.qintingfm.web.service.form.annotation.FieldAnnotation;
import com.qintingfm.web.service.form.annotation.FormAnnotation;
import com.qintingfm.web.pojo.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类别
 * @author guliuzhong
 */
@FormAnnotation(method = "post",hideSubmit = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryVo extends BaseVo {
    @FieldAnnotation(title = "类别ID",tip = "",order = 0,hide = true)
    Integer catId;
    @FieldAnnotation(title = "名称",tip = "",order = 1)
    String title;
    @FieldAnnotation(title = "描述",tip = "",order = 2)
    String description;
}
