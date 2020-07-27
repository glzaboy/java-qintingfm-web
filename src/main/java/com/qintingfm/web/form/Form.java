package com.qintingfm.web.form;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
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
