package com.qintingfm.web.service;

import org.apache.xmlrpc.XmlRpcConfig;
import org.apache.xmlrpc.XmlRpcConfigImpl;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.common.TypeFactoryImpl;
import org.apache.xmlrpc.common.XmlRpcController;
import org.apache.xmlrpc.common.XmlRpcStreamConfig;
import org.apache.xmlrpc.common.XmlRpcWorkerFactory;
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
import java.util.TimeZone;

public class XmlRpcServer {
    public XmlRpcRequestParser getXmlRequestParser(InputStream inputStream) throws XmlRpcException, IOException, SAXException {

        XmlRpcStreamConfig x = new XmlRpcStreamConfig();
        XmlRpcController wXmlRpcConfig=new XmlRpcController();
        wXmlRpcConfig.setXmlRpcConfig(x);
        TypeFactoryImpl typeFactory = new TypeFactoryImpl(wXmlRpcConfig);
        final XmlRpcRequestParser parser = new XmlRpcRequestParser(x, typeFactory);
        XMLReader xr = SAXParsers.newXMLReader();
        xr.setContentHandler(parser);
        InputSource inputSource = new InputSource(inputStream);
        xr.parse(inputSource);
        xr.setContentHandler(parser);
        return parser;
    }

    public void ResponseError(OutputStream outputStream, int pCode, String pMessage) throws XmlRpcException, SAXException {
        XmlRpcStreamConfig x = new XmlRpcStreamConfig();
        XmlWriterFactory writerFactory = new DefaultXMLWriterFactory();
        TypeFactoryImpl typeFactory = new TypeFactoryImpl(null);
        ContentHandler w = writerFactory.getXmlWriter(x, outputStream);
        XmlRpcWriter xmlRpcWriter = new XmlRpcWriter(x, w, typeFactory);
        xmlRpcWriter.write(x, pCode, pMessage);
    }

    public void Response(OutputStream outputStream, Object pResult) throws XmlRpcException, SAXException {
        XmlRpcStreamConfig x = new XmlRpcStreamConfig();
        XmlWriterFactory writerFactory = new DefaultXMLWriterFactory();
        TypeFactoryImpl typeFactory = new TypeFactoryImpl(null);
        ContentHandler w = writerFactory.getXmlWriter(x, outputStream);
        XmlRpcWriter xmlRpcWriter = new XmlRpcWriter(x, w, typeFactory);
        xmlRpcWriter.write(x, pResult);
    }

    static class XmlRpcStreamConfig implements org.apache.xmlrpc.common.XmlRpcStreamRequestConfig {
        public XmlRpcStreamConfig() {
        }

        public boolean isGzipCompressing() {
            return false;
        }

        public boolean isGzipRequesting() {
            return false;
        }

        public boolean isEnabledForExceptions() {
            return false;
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
    static  class XmlRpcController extends org.apache.xmlrpc.common.XmlRpcController {
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
}
