package com.qintingfm.web.service.form;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 表单元素项目
 *
 * @author guliuzhong
 */
@Data
@Builder
public class FormOption {
    String id;
    String text;
}
