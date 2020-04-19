package com.qintingfm.web.common.exception;

import lombok.Builder;
import lombok.Data;

/**
 * @author guliuzhong
 */
@Data
@Builder
public class Business {
    /**
     * 错误字段
     */
    String field;
    /**
     * 错误消息
     */
    String message;
}
