package com.qintingfm.web.controller;

import com.qintingfm.web.jpa.entity.WxUpload;
import com.qintingfm.web.pojo.vo.WxResponse;
import com.qintingfm.web.service.BaiduAiApiService;
import com.qintingfm.web.service.WxUploadService;
import com.qintingfm.web.storage.Manager;
import com.qintingfm.web.storage.ManagerException;
import com.qintingfm.web.storage.StorageObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

/**
 * 微信相关接口
 *
 * @author guliuzhong
 */
@Controller
@RequestMapping("/wxapi")
@Slf4j
public class WxApiController extends BaseController {
    Manager manager;
    WxUploadService wxUploadService;
    BaiduAiApiService baiduAiApiService;

    @Autowired
    @Qualifier(value = "wxUpload")
    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @Autowired
    public void setBaiduAiApiService(BaiduAiApiService baiduAiApiService) {
        this.baiduAiApiService = baiduAiApiService;
    }

    @Autowired
    public void setWxUploadService(WxUploadService wxUploadService) {
        this.wxUploadService = wxUploadService;
    }

    @PostMapping(value = "/{appID}/upload", produces = "application/json;charset=utf-8")
    @ResponseBody
    public WxResponse upload(@PathVariable("appID") String appId, @RequestParam("file") MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        originalFilename = originalFilename.replaceAll(":", "").replaceAll("\\//", "").replaceAll("\\\\", "");
        WxResponse.WxResponseBuilder builder = WxResponse.builder();
        try {
            byte[] bytes = file.getBytes();
            String s = DigestUtils.md5DigestAsHex(bytes);
            StorageObject storageObject = wxUploadService.uploadFile(bytes, s);
            WxUpload upload = wxUploadService.upload(appId, storageObject.getUrl(), storageObject.getObjectName());
            builder.data(String.valueOf(upload.getId())).isSuccess(true);
        } catch (IOException | ManagerException e) {
            log.error("图片上传出错原因"+e.getMessage());
            builder.isSuccess(false).message("图片上传出错原因：服务器出错");
        }
        return builder.build();
    }
    @RequestMapping(value = "/{appID}/getPlant", produces = "application/json;charset=utf-8")
    @ResponseBody
    @Transactional
    public WxResponse getPlant( @PathVariable("appID") String appId,@RequestParam("id") Long id, @RequestParam("baike_num") Integer baikeNum) {
        WxResponse.WxResponseBuilder builder = WxResponse.builder();
        Optional<WxUpload> byId = wxUploadService.findById(id);
        if (!byId.isPresent()) {
            return builder.message("未找到上传的文件").isSuccess(false).build();
        } else {
            WxUpload wxUpload = byId.get();
            if(wxUpload.getProcessText()!=null){
                return builder.data(wxUpload.getProcessText()).isSuccess(true).build();
            }
            String bodySeg = null;
            try {
                bodySeg = baiduAiApiService.plant(URI.create(wxUpload.getUrl()),baikeNum);
                wxUploadService.process(wxUpload,"plant","true",bodySeg);
                return builder.data(bodySeg).isSuccess(true).build();
            } catch (IOException e) {
                return builder.message("图片识别出错").isSuccess(false).build();
            }
        }
    }

}
