package com.qintingfm.web.service.xmlrpc;

import java.util.TimeZone;

/**
 * xmlRpc 输出配置
 *
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
        return true;
    }

    @Override
    public TimeZone getTimeZone() {
        return TimeZone.getTimeZone("Etc/UTC");
    }
//
//    public static void main(String[] args) {
//        String[] availableIDs = TimeZone.getAvailableIDs();
//        Arrays.asList(availableIDs).stream().forEach(System.out::println);
//    }
}
