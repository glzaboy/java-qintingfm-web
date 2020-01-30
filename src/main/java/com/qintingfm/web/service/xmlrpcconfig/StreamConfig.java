package com.qintingfm.web.service.xmlrpcconfig;

import java.util.TimeZone;

/**
 * xmlRpc 输出配置
 * @author guliuzhong
 */
public class StreamConfig implements org.apache.xmlrpc.common.XmlRpcStreamRequestConfig {
    public StreamConfig() {
    }

    @Override
    public boolean isGzipCompressing() {
        return false;
    }

    @Override
    public boolean isGzipRequesting() {
        return false;
    }

    @Override
    public boolean isEnabledForExceptions() {
        return true;
    }

    @Override
    public String getEncoding() {
        return null;
    }

    @Override
    public boolean isEnabledForExtensions() {
        return false;
    }

    @Override
    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }
}
