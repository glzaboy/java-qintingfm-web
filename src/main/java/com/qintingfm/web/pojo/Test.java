package com.qintingfm.web.pojo;

import com.qintingfm.web.form.Annotation.FieldAnnotation;
import com.qintingfm.web.form.Annotation.FormAnnotation;
import com.qintingfm.web.pojo.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@FormAnnotation(title = "test",action = "localhost")
@Data
@EqualsAndHashCode(callSuper = true)
public class Test extends BaseVo {
    @FieldAnnotation(title = "at",tip = "thi si tip",Order = 2)
    String at;
    @FieldAnnotation(title = "at2",tip = "thi si tip  at2",Order = 1)
    String at2;
}
