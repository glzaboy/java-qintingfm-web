package com.qintingfm.web.service.xmlrpc;

import org.apache.xmlrpc.common.XmlRpcController;
import org.apache.xmlrpc.common.XmlRpcStreamConfig;
import org.apache.xmlrpc.serializer.TypeSerializer;
import org.xml.sax.SAXException;

/**
 * 本例的
 * @author guliuzhong
 */
public class TypeFactoryImpl extends org.apache.xmlrpc.common.TypeFactoryImpl {
    private static final TypeSerializer STRING_SERIALIZER = new StringSerializer();
    private final XmlRpcController controller;


    /**
     * Creates a new instance.
     *
     * @param pController The controller, which operates the type factory.
     */
    public TypeFactoryImpl(XmlRpcController pController) {
        super(pController);
        controller = pController;
    }

    @Override
    public TypeSerializer getSerializer(XmlRpcStreamConfig pConfig, Object pObject) throws SAXException {
        TypeSerializer serializer = super.getSerializer(pConfig, pObject);
        if (serializer instanceof org.apache.xmlrpc.serializer.StringSerializer) {
            serializer = STRING_SERIALIZER;
        }
        return serializer;
    }
}
