package com.qintingfm.web.pojo.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

/**
 * @author guliuzhong
 */
@Getter
@Setter
@Builder
public class BlogPojo {
    @NotBlank(message = "标题不能为空")
    @Length(min = 4, message = "标题至少长度为四个字符")
    String title;
    @NotBlank(message = "文章内容不能为空")
    String cont;
    @Pattern(regexp = "publish|draft", message = "发布状态只支持publish,draft", flags = {Pattern.Flag.CASE_INSENSITIVE})
    String state;
    @NotEmpty
    List<String> catNames;
    @NotNull(message = "作者不能为空")
    @Min(value = 1, message = "用户不合法")
    Long authorId;
    Integer postId;
    Date createDate;
    Date updateDate;
}