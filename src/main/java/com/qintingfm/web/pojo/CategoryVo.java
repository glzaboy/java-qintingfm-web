package com.qintingfm.web.pojo;

import com.qintingfm.web.form.Annotation.FieldAnnotation;
import com.qintingfm.web.form.Annotation.FormAnnotation;
import lombok.Data;

@FormAnnotation(method = "post",action = "/acdefg",title = "类别设置")
@Data
public class CategoryVo {
    @FieldAnnotation(title = "类别ID",tip = "",Order = 1)
    public Integer catId;
    @FieldAnnotation(title = "名称",tip = "",Order = 1)
    public String title;
    @FieldAnnotation(title = "描述",tip = "",Order = 1)
    public String description;
}
