package com.qintingfm.web.service.xmlrpc;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class StringSerializer extends org.apache.xmlrpc.serializer.StringSerializer{
    public StringSerializer() {
        super();
    }

    @Override
    public void write(ContentHandler pHandler, Object pObject) throws SAXException {
        write(pHandler, STRING_TAG, pObject.toString());
    }
}
