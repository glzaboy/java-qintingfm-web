package com.qintingfm.web.storage;

import com.qiniu.common.AutoRegion;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FetchRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author guliuzhong
 */
public class QiniuOssImpl implements Oss{
    Config config;

    public void setConfig(Config config) {
        this.config = config;
    }

    private Auth getAuth(){
        return Auth.create(config.getAccesskey(), config.getSecretKey());
    }
    private String getToken(){
        return getAuth().uploadToken(config.getBucket());
    }
    @Override
    public StorageObject put(byte[] data, String objName) throws ManagerException {
        UploadManager uploadManager = new UploadManager(new Configuration(AutoRegion.autoRegion()));
        StorageObject storageObject=new StorageObject();
        try {
            Response r = uploadManager.put(data, objName, getToken());
            StringMap stringMap = r.jsonToMap();
            String key = (String)stringMap.get("key");
            storageObject.setObjectName(key);
            storageObject.setUrl(config.getRootpath()+key);
        } catch (QiniuException e) {
            throw new ManagerException(e.getMessage(),e.getCause());
        }
        return storageObject;
    }

    @Override
    public StorageObject put(URL url,  String objName)  throws ManagerException {
        BucketManager bucketManager=new BucketManager(getAuth(),new Configuration(AutoRegion.autoRegion()));

        StorageObject storageObject=new StorageObject();
        try {
            FetchRet fetch = bucketManager.fetch(url.toString(), config.getBucket(), objName);
            storageObject.setObjectName(fetch.key);
            storageObject.setUrl(config.getRootpath()+fetch.key);
        } catch (QiniuException e) {
            throw new ManagerException(e.getMessage(),e.getCause());
        }
        return storageObject;
    }

    @Override
    public StorageObject put(InputStream inputStream, String objName) throws ManagerException {
        UploadManager uploadManager = new UploadManager(new Configuration(AutoRegion.autoRegion()));
        StorageObject storageObject=new StorageObject();
        try {
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            byte[] bytes=new byte[1000];
            int readnum;
            do{
                readnum = inputStream.read(bytes, 0, 1000);
                if(readnum>0){
                    byteArrayOutputStream.write(bytes,0,readnum);
                }
            }while (readnum>0);
            Response r = uploadManager.put(byteArrayOutputStream.toByteArray(), objName, getToken());
            StringMap stringMap = r.jsonToMap();
            String key = (String)stringMap.get("key");
            storageObject.setObjectName(key);
            storageObject.setUrl(config.getRootpath()+key);
        } catch (QiniuException e) {
            throw new ManagerException(e.getMessage(),e.getCause());
        } catch (IOException e) {
            throw new ManagerException(e.getMessage(),e.getCause());
        }
        return storageObject;
    }

    @Override
    public Boolean delete(String objName) throws ManagerException{
        UploadManager uploadManager = new UploadManager(new Configuration(AutoRegion.autoRegion()));
        StorageObject storageObject=new StorageObject();
        try {
            BucketManager bucketManager=new BucketManager(getAuth(),new Configuration(AutoRegion.autoRegion()));
            Response delete = bucketManager.delete(config.getBucket(), objName);
        } catch (QiniuException e) {
            throw new ManagerException(e.getMessage(),e.getCause());
        }
        return true;
    }
}
