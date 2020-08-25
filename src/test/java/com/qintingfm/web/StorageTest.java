package com.qintingfm.web;

import com.qintingfm.web.storage.Manager;
import com.qintingfm.web.storage.ManagerException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StorageTest {
    Manager manager;
    @Autowired
    @Qualifier(value = "wxUpload")
    public void setManager(Manager manager) {
        this.manager = manager;
    }
    @Test
    public void testPut() throws ManagerException {
        manager.put("abc".getBytes(),"test.txt");
    }
    @Test
    public void testDelete() throws ManagerException {
        manager.delete("test.txt");
    }
}
