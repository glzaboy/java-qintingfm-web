package com.qintingfm.web.service.form;

import com.qintingfm.web.service.form.annotation.FormItem;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 表单
 * @author guliuzhong
 */
@Data
@Builder
public class Form implements Serializable {
    List<FormItem> formItems;
    String title;
    String message;
    String method;
    Boolean hideSubmit;
}
