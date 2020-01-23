package com.qintingfm.web.service;

import com.qintingfm.web.service.xmlrpcconfig.RpcController;
import com.qintingfm.web.service.xmlrpcconfig.StreamConfig;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.common.TypeFactoryImpl;
import org.apache.xmlrpc.parser.XmlRpcRequestParser;
import org.apache.xmlrpc.serializer.DefaultXMLWriterFactory;
import org.apache.xmlrpc.serializer.XmlRpcWriter;
import org.apache.xmlrpc.serializer.XmlWriterFactory;
import org.apache.xmlrpc.util.SAXParsers;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * xml rpc简易服务端需要依赖xml-prc-common
 * 本类配置比较奇特不适合使用spring配置。
 */
public class XmlRpcServer {
    StreamConfig streamConfig;
    RpcController rpcController;

    public StreamConfig getStreamConfig() {
        return streamConfig;
    }

    public void setStreamConfig(StreamConfig streamConfig) {
        this.streamConfig = streamConfig;
    }

    public RpcController getRpcController() {
        return rpcController;
    }

    public void setRpcController(RpcController rpcController) {
        this.rpcController = rpcController;
    }

    public XmlRpcRequestParser getXmlRequestParser(InputStream inputStream) throws XmlRpcException, IOException, SAXException {

        rpcController.setXmlRpcConfig(streamConfig);
        TypeFactoryImpl typeFactory = new TypeFactoryImpl(rpcController);
        final XmlRpcRequestParser parser = new XmlRpcRequestParser(streamConfig, typeFactory);
        XMLReader xr = SAXParsers.newXMLReader();
        xr.setContentHandler(parser);
        InputSource inputSource = new InputSource(inputStream);
        xr.parse(inputSource);
        xr.setContentHandler(parser);
        return parser;
    }

    public void ResponseError(OutputStream outputStream, int pCode, String pMessage) throws XmlRpcException, SAXException {
        rpcController.setXmlRpcConfig(streamConfig);
        XmlWriterFactory writerFactory = new DefaultXMLWriterFactory();
        TypeFactoryImpl typeFactory = new TypeFactoryImpl(rpcController);
        ContentHandler w = writerFactory.getXmlWriter(streamConfig, outputStream);
        XmlRpcWriter xmlRpcWriter = new XmlRpcWriter(streamConfig, w, typeFactory);
        xmlRpcWriter.write(streamConfig, pCode, pMessage);
    }

    public void Response(OutputStream outputStream, Object pResult) throws XmlRpcException, SAXException {
        rpcController.setXmlRpcConfig(streamConfig);
        XmlWriterFactory writerFactory = new DefaultXMLWriterFactory();
        TypeFactoryImpl typeFactory = new TypeFactoryImpl(rpcController);
        ContentHandler w = writerFactory.getXmlWriter(streamConfig, outputStream);
        XmlRpcWriter xmlRpcWriter = new XmlRpcWriter(streamConfig, w, typeFactory);
        xmlRpcWriter.write(streamConfig, pResult);
    }
}
