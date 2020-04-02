package com.qintingfm.web.service.xmlrpc;

import org.apache.xmlrpc.XmlRpcConfig;
import org.apache.xmlrpc.common.XmlRpcWorkerFactory;

/**
 * xmlRpc配置
 *
 * @author guliuzhong
 */
public class RpcController extends org.apache.xmlrpc.common.XmlRpcController {
    XmlRpcConfig xmlRpcConfig;

    @Override
    protected XmlRpcWorkerFactory getDefaultXmlRpcWorkerFactory() {
        return null;
    }

    @Override
    public XmlRpcConfig getConfig() {
        return this.xmlRpcConfig;
    }

    public void setXmlRpcConfig(XmlRpcConfig xmlRpcConfig) {
        this.xmlRpcConfig = xmlRpcConfig;
    }
}