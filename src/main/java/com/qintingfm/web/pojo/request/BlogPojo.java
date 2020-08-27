package com.qintingfm.web.pojo.request;


import com.qintingfm.web.pojo.vo.BaseVo;
import com.qintingfm.web.service.form.annotation.FieldAnnotation;
import com.qintingfm.web.service.form.annotation.FormAnnotation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author guliuzhong
 */
@Getter
@Setter
@FormAnnotation(method = "post",value = "文章编辑")
@NoArgsConstructor
public class BlogPojo extends BaseVo {
    @NotBlank(message = "标题不能为空")
    @Length(min = 4, message = "标题至少长度为四个字符")
    @FieldAnnotation(title = "标题",tip = "",order = 1,hide = false)
    String title;
    @NotBlank(message = "文章内容不能为空")
    @FieldAnnotation(title = "内容",tip = "",order = 3,hide = false,htmlEditorUpload = true,largeText = true)
    String cont;
    @FieldAnnotation(title = "发布到前台",tip = "",order = 4)
    Boolean state=false;
    @NotEmpty
    @FieldAnnotation(title = "类别",tip = "",order = 2,hide = false,listBeanName = "categoryService",listMethod = "getAllCategory",multiple = true)
    String[] catNames;
    @NotNull(message = "作者不能为空")
    @Min(value = 1, message = "用户不合法")
    Long authorId;
    @FieldAnnotation(title = "文章ID",tip = "",order = 0,hide = true)
    Integer postId;
    Date createDate;
    Date updateDate;
}