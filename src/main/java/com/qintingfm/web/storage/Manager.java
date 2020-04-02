package com.qintingfm.web.storage;

import java.io.InputStream;
import java.net.URL;

/**
 * @author guliuzhong
 */
public class Manager {
    Oss oss;

    public void setOss(Oss oss) {
        this.oss = oss;
    }

    public StorageObject put(byte[] data, String token) throws ManagerException {
        return oss.put(data, token);
    }

    public StorageObject put(URL url, String objName) throws ManagerException {
        return oss.put(url, objName);
    }

    public StorageObject put(InputStream inputStream, String objName) throws ManagerException {
        return oss.put(inputStream, objName);
    }

    public Boolean delete(String objName) throws ManagerException {
        return oss.delete(objName);
    }
}
