package com.qintingfm.web.service.baiduai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * 百度api接口token
 * @author guliuzhong
 */
@Data
@JsonIgnoreProperties({"expires_in","session_key"})
public class BaiduApiToken {
    @JsonProperty("access_token")
    String accessToken;
    @JsonProperty(value = "refresh_token")
    String refreshToken;
    String scope;
    @JsonProperty(value = "session_secret")
    String sessionSecret;
    Date createData;
}
