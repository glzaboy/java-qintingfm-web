package com.qintingfm.web.common;

import lombok.Data;

import java.util.Map;

/**
 * 网页版本ajax请求
 *
 * @author guliuzhong
 */
@Data
public class AjaxDto {
    private String message;
    private String link;

    private String autoHide;

    private int autoJump;

    private Map<String, String> error;

}
