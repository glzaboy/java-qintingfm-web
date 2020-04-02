package com.qintingfm.web.storage;

import lombok.Data;

/**
 * 存储配置信息
 *
 * @author guliuzhong
 */
@Data
public class Config {
    String accesskey;
    String secretKey;
    String rootPath;
    String bucket;
}
