package com.qintingfm.web.storage;


import java.io.InputStream;
import java.net.URL;

/**
 * 应用存储
 * @author guliuzhong
 */
public interface Oss {
    /**
     * 保存内容到存储
     * @param data 存储的内容
     * @param objName 存储名称
     * @return 存储信息
     * @throws ManagerException 失败信息
     */
    StorageObject put(byte[] data, String objName) throws ManagerException;

    /**
     * 保存指定url的内容到存储
     * @param url URL地址
     * @param objName 存储名称
     * @return 存储信息
     * @throws ManagerException 失败信息
     */
    StorageObject put(URL url,  String objName) throws ManagerException;

    /**
     * 将inputStream内容保存到存储
     * @param inputStream inputStream
     * @param objName 存储名称
     * @return 存储信息
     * @throws ManagerException 失败信息
     */
    StorageObject put(InputStream inputStream,  String objName)throws ManagerException;

    /**
     * 删除存储
     * @param objName 存储名称
     * @return 是否成功
     * @throws ManagerException 失败信息
     */
    Boolean delete( String objName) throws ManagerException;
}
