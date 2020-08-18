package com.qintingfm.web.controller;

import com.qintingfm.web.jpa.WxUploadJpa;
import com.qintingfm.web.jpa.entity.WxUpload;
import com.qintingfm.web.pojo.vo.WxUploadVo;
import com.qintingfm.web.service.BaiduAiApi;
import com.qintingfm.web.storage.Manager;
import com.qintingfm.web.storage.ManagerException;
import com.qintingfm.web.storage.StorageObject;
import lombok.SneakyThrows;
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

import java.io.IOException;
import java.net.URI;
import java.util.Date;
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
    WxUploadJpa wxUploadJpa;
    BaiduAiApi baiduAiApi;

    @Autowired
    @Qualifier(value = "wxUpload")
    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @Autowired
    public void setWxUploadJpa(WxUploadJpa wxUploadJpa) {
        this.wxUploadJpa = wxUploadJpa;
    }

    @PostMapping(value = "/{appID}upload", produces = "application/json;charset=utf-8")
    @ResponseBody
    public WxUploadVo upload(@PathVariable("appID") String appId,@RequestParam("file") MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        originalFilename = originalFilename.replaceAll(":", "").replaceAll("\\//", "").replaceAll("\\\\", "");
        WxUploadVo wxUploadVo = new WxUploadVo();
        try {
            byte[] bytes = file.getBytes();
            String s = DigestUtils.md5DigestAsHex(bytes);
            StorageObject put = manager.put(bytes, s);
            WxUpload wxUpload = new WxUpload();
            wxUpload.setAppId(appId);
            wxUpload.setUrl(put.getUrl());
            wxUpload.setFileName(put.getObjectName());
            wxUpload.setCreate(new Date());
            wxUploadJpa.save(wxUpload);
            wxUploadVo.setId(String.valueOf(wxUpload.getId()));
        } catch (IOException | ManagerException e) {
            log.error("图片上传出错原因"+e.getMessage());
        }
        return wxUploadVo;
    }
    @RequestMapping(value = "/{appID}/getPlant", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getPlant( @PathVariable("appID") String appId,@RequestParam("id") Long id, @RequestParam("baike_num") Integer baikeNum) {
        Optional<WxUpload> byId = wxUploadJpa.findById(id);
        if (!byId.isPresent()) {
            return "{}";
        } else {
            WxUpload wxUpload = byId.get();
            String bodySeg = null;
            try {
                bodySeg = baiduAiApi.plant(URI.create(wxUpload.getFileName()),baikeNum);
                return bodySeg;
            } catch (IOException e) {
                log.error("图片识别出错"+e.getMessage());
            }
            return bodySeg;
        }
    }

}
