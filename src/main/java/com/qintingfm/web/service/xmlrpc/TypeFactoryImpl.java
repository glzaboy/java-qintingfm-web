package com.qintingfm.web.service.xmlrpc;

import org.apache.xmlrpc.common.XmlRpcController;
import org.apache.xmlrpc.common.XmlRpcExtensionException;
import org.apache.xmlrpc.common.XmlRpcStreamConfig;
import org.apache.xmlrpc.serializer.*;
import org.apache.xmlrpc.util.XmlRpcDateTimeDateFormat;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class TypeFactoryImpl extends org.apache.xmlrpc.common.TypeFactoryImpl {
    private static final TypeSerializer STRING_SERIALIZER = new StringSerializer();
    private static final TypeSerializer NULL_SERIALIZER = new NullSerializer();
    private static final TypeSerializer I4_SERIALIZER = new I4Serializer();
    private static final TypeSerializer BOOLEAN_SERIALIZER = new BooleanSerializer();
    private static final TypeSerializer DOUBLE_SERIALIZER = new DoubleSerializer();
    private static final TypeSerializer BYTE_SERIALIZER = new I1Serializer();
    private static final TypeSerializer SHORT_SERIALIZER = new I2Serializer();
    private static final TypeSerializer LONG_SERIALIZER = new I8Serializer();
    private static final TypeSerializer FLOAT_SERIALIZER = new FloatSerializer();
    private static final TypeSerializer NODE_SERIALIZER = new NodeSerializer();
    private static final TypeSerializer SERIALIZABLE_SERIALIZER = new SerializableSerializer();
    private static final TypeSerializer BIGDECIMAL_SERIALIZER = new BigDecimalSerializer();
    private static final TypeSerializer BIGINTEGER_SERIALIZER = new BigIntegerSerializer();
    private static final TypeSerializer CALENDAR_SERIALIZER = new CalendarSerializer();

    private final XmlRpcController controller;
    private DateSerializer dateSerializer;


    /**
     * Creates a new instance.
     *
     * @param pController The controller, which operates the type factory.
     */
    public TypeFactoryImpl(XmlRpcController pController) {
        super(pController);
        controller=pController;
    }
    @Override
    public TypeSerializer getSerializer(XmlRpcStreamConfig pConfig, Object pObject) throws SAXException {
        if (pObject == null) {
            if (pConfig.isEnabledForExtensions()) {
                return NULL_SERIALIZER;
            } else {
                throw new SAXException(new XmlRpcExtensionException("Null values aren't supported, if isEnabledForExtensions() == false"));
            }
        } else if (pObject instanceof String) {
            return STRING_SERIALIZER;
        } else if (pObject instanceof Byte) {
            if (pConfig.isEnabledForExtensions()) {
                return BYTE_SERIALIZER;
            } else {
                throw new SAXException(new XmlRpcExtensionException("Byte values aren't supported, if isEnabledForExtensions() == false"));
            }
        } else if (pObject instanceof Short) {
            if (pConfig.isEnabledForExtensions()) {
                return SHORT_SERIALIZER;
            } else {
                throw new SAXException(new XmlRpcExtensionException("Short values aren't supported, if isEnabledForExtensions() == false"));
            }
        } else if (pObject instanceof Integer) {
            return I4_SERIALIZER;
        } else if (pObject instanceof Long) {
            if (pConfig.isEnabledForExtensions()) {
                return LONG_SERIALIZER;
            } else {
                throw new SAXException(new XmlRpcExtensionException("Long values aren't supported, if isEnabledForExtensions() == false"));
            }
        } else if (pObject instanceof Boolean) {
            return BOOLEAN_SERIALIZER;
        } else if (pObject instanceof Float) {
            if (pConfig.isEnabledForExtensions()) {
                return FLOAT_SERIALIZER;
            } else {
                throw new SAXException(new XmlRpcExtensionException("Float values aren't supported, if isEnabledForExtensions() == false"));
            }
        } else if (pObject instanceof Double) {
            return DOUBLE_SERIALIZER;
        } else if (pObject instanceof Calendar) {
            if (pConfig.isEnabledForExtensions()) {
                return CALENDAR_SERIALIZER;
            } else {
                throw new SAXException(new XmlRpcExtensionException("Calendar values aren't supported, if isEnabledForExtensions() == false"));
            }
        } else if (pObject instanceof Date) {
            if (dateSerializer == null) {
                dateSerializer = new DateSerializer(new XmlRpcDateTimeDateFormat(){
                    private static final long serialVersionUID = 24345909123324234L;
                    protected TimeZone getTimeZone() {
                        return controller.getConfig().getTimeZone();
                    }
                });
            }
            return dateSerializer;
        } else if (pObject instanceof byte[]) {
            return new ByteArraySerializer();
        } else if (pObject instanceof Object[]) {
            return new ObjectArraySerializer(this, pConfig);
        } else if (pObject instanceof List) {
            return new ListSerializer(this, pConfig);
        } else if (pObject instanceof Map) {
            return new MapSerializer(this, pConfig);
        } else if (pObject instanceof Node) {
            if (pConfig.isEnabledForExtensions()) {
                return NODE_SERIALIZER;
            } else {
                throw new SAXException(new XmlRpcExtensionException("DOM nodes aren't supported, if isEnabledForExtensions() == false"));
            }
        } else if (pObject instanceof BigInteger) {
            if (pConfig.isEnabledForExtensions()) {
                return BIGINTEGER_SERIALIZER;
            } else {
                throw new SAXException(new XmlRpcExtensionException("BigInteger values aren't supported, if isEnabledForExtensions() == false"));
            }
        } else if (pObject instanceof BigDecimal) {
            if (pConfig.isEnabledForExtensions()) {
                return BIGDECIMAL_SERIALIZER;
            } else {
                throw new SAXException(new XmlRpcExtensionException("BigDecimal values aren't supported, if isEnabledForExtensions() == false"));
            }
        } else if (pObject instanceof Serializable) {
            if (pConfig.isEnabledForExtensions()) {
                return SERIALIZABLE_SERIALIZER;
            } else {
                throw new SAXException(new XmlRpcExtensionException("Serializable objects aren't supported, if isEnabledForExtensions() == false"));
            }
        } else {
            return null;
        }
    }
}
