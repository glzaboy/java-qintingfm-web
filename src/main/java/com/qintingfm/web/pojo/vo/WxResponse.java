package com.qintingfm.web.pojo.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信程序信息返回
 * @author guliuzhong
 */
@Builder
@Data
public class WxResponse implements Serializable {
    String data;
    String message;
    Boolean isSuccess;
}
