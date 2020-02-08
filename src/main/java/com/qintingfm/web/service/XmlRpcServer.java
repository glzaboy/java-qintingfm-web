package com.qintingfm.web.service;

import com.qintingfm.web.service.xmlrpc.RpcController;
import com.qintingfm.web.service.xmlrpc.StreamConfig;
import com.qintingfm.web.service.xmlrpc.TypeFactoryImpl;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.parser.XmlRpcRequestParser;
import org.apache.xmlrpc.serializer.DefaultXMLWriterFactory;
import org.apache.xmlrpc.serializer.XmlRpcWriter;
import org.apache.xmlrpc.serializer.XmlWriterFactory;
import org.apache.xmlrpc.util.SAXParsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * xml rpc简易服务端需要依赖xml-prc-common
 * 本类配置比较奇特不适合使用spring配置。
 * @author guliuzhong
 */
public class XmlRpcServer {
    StreamConfig streamConfig;
    RpcController rpcController;
    Logger logger=LoggerFactory.getLogger(XmlRpcServer.class);

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

    public void responseError(OutputStream outputStream, int pCode, String pMessage)  {
        rpcController.setXmlRpcConfig(streamConfig);
        XmlWriterFactory writerFactory = new DefaultXMLWriterFactory();
        TypeFactoryImpl typeFactory = new TypeFactoryImpl(rpcController);
        ContentHandler w = null;
        try {
            w = writerFactory.getXmlWriter(streamConfig, outputStream);
            XmlRpcWriter xmlRpcWriter = new XmlRpcWriter(streamConfig, w, typeFactory);
            xmlRpcWriter.write(streamConfig, pCode, pMessage);
        } catch (XmlRpcException e) {
            logger.error("responseError XmlRpcException {}",e.getMessage());
        } catch (SAXException e) {
            logger.error("responseError SAXException {}",e.getMessage());
        }

    }

    public void response(OutputStream outputStream, Object pResult) {
        rpcController.setXmlRpcConfig(streamConfig);
        XmlWriterFactory writerFactory = new DefaultXMLWriterFactory();
        TypeFactoryImpl typeFactory = new TypeFactoryImpl(rpcController);
        ContentHandler w = null;
        try {
            w = writerFactory.getXmlWriter(streamConfig, outputStream);
            XmlRpcWriter xmlRpcWriter = new XmlRpcWriter(streamConfig, w, typeFactory);
            xmlRpcWriter.write(streamConfig, pResult);
        } catch (XmlRpcException e) {
            logger.error("response XmlRpcException {}",e.getMessage());
        } catch (SAXException e) {
            logger.error("response SAXException {}",e.getMessage());
        }

    }
}
