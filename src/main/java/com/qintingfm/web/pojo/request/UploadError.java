package com.qintingfm.web.pojo.request;

import lombok.Builder;
import lombok.Data;

/**
 * 上传文件出错信息
 * @author guliuzhong
 */
@Data
@Builder
public class UploadError {
    String message;
}
