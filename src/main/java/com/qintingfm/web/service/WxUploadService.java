package com.qintingfm.web.service;

import com.qintingfm.web.jpa.WxUploadJpa;
import com.qintingfm.web.jpa.entity.WxUpload;
import com.qintingfm.web.storage.Manager;
import com.qintingfm.web.storage.ManagerException;
import com.qintingfm.web.storage.StorageObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Date;
import java.util.Optional;

/**
 * 微信等小程序上传文件接口及处理接口
 * @author guliuzhong
 */
@Service
public class WxUploadService extends BaseService{
    WxUploadJpa wxUploadJpa;
    Manager manager;

    @Autowired
    public void setWxUploadJpa(WxUploadJpa wxUploadJpa) {
        this.wxUploadJpa = wxUploadJpa;
    }

    @Autowired
    @Qualifier(value = "wxUpload")
    public void setManager(Manager manager) {
        this.manager = manager;
    }

    /**
     *
     * @param pageIndex
     * @param sort
     * @param pageSize
     * @return
     */
    public Page<WxUpload> getList(Integer pageIndex, Sort sort, Integer pageSize){
        pageIndex = (pageIndex == null) ? 1 : pageIndex;
        if (sort == null) {
            sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "id"));
        }
        PageRequest request = PageRequest.of(pageIndex - 1, pageSize, sort);
        return wxUploadJpa.findAll(request);
    }

    /**
     * 上传文件到远程
     * @param inputStream 输入流
     * @param objName 远程文件名称
     * @return StorageObject
     * @throws ManagerException
     */
    public StorageObject uploadFile(InputStream inputStream, String objName) throws ManagerException {
        return manager.put(inputStream,objName);

    }

    /**
     * 上传文件到远程
     * @param bytes 文件字节码
     * @param objName 远程文件名称
     * @return StorageObject
     * @throws ManagerException
     */
    public StorageObject uploadFile(byte[] bytes, String objName) throws ManagerException{
        return manager.put(bytes,objName);
    }
    @Transactional(rollbackFor = {})
    public WxUpload upload(String appId,String url,String fileName){
        WxUpload wxUpload = new WxUpload();
        wxUpload.setAppId(appId);
        wxUpload.setUrl(url);
        wxUpload.setFileName(fileName);
        wxUpload.setCreateDate(new Date());
        return wxUploadJpa.save(wxUpload);
    }
    @Transactional(rollbackFor = {},readOnly = true)
    public Optional<WxUpload> findById(Long id){
        return wxUploadJpa.findById(id);
    }
    @Transactional(rollbackFor = {})
    public WxUpload process(WxUpload wxUpload,String actionType,String status,String processText){
        wxUpload.setActionType(actionType);
        wxUpload.setProcessStatus(status);
        wxUpload.setProcessText(processText);
        return wxUploadJpa.save(wxUpload);
    }

}
