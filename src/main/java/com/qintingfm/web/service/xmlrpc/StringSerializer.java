package com.qintingfm.web.service.xmlrpc;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * apache的StringSerializer有一个bug对于字符串没有生成string标签，有一些解析器会出错
 *
 * @author guliuzhong
 */
public class StringSerializer extends org.apache.xmlrpc.serializer.StringSerializer {
    public StringSerializer() {
        super();
    }

    @Override
    public void write(ContentHandler pHandler, Object pObject) throws SAXException {
        write(pHandler, STRING_TAG, pObject.toString());
    }
}
