package com.qintingfm.web.pojo.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 上图文件功能
 * @author guliuzhong
 */
@Data
@Builder
public class UploadImagePojo implements Serializable {
    Map<String,String> urls;
    UploadError error;
}
