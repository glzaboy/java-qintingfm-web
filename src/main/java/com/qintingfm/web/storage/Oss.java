package com.qintingfm.web.storage;

import com.qiniu.common.QiniuException;

import java.io.InputStream;
import java.net.URL;

public interface Oss {
    StorageObject put(byte[] data, String objName) throws ManagerException;
    StorageObject put(URL url,  String objName) throws ManagerException;
    StorageObject put(InputStream inputStream,  String objName)throws ManagerException;
    Boolean delete( String objName) throws ManagerException;
}
