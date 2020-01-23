package com.qintingfm.web.service.xmlrpcconfig;

import java.util.TimeZone;

/**
 * xmlrpc 输出配置
 */
public class StreamConfig implements org.apache.xmlrpc.common.XmlRpcStreamRequestConfig {
    public StreamConfig() {
    }

    public boolean isGzipCompressing() {
        return false;
    }

    public boolean isGzipRequesting() {
        return false;
    }

    public boolean isEnabledForExceptions() {
        return true;
    }

    public String getEncoding() {
        return null;
    }

    public boolean isEnabledForExtensions() {
        return false;
    }

    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }
}
