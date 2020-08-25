package com.qintingfm.web.controller.admin;

import com.qintingfm.web.controller.BaseController;
import com.qintingfm.web.service.WxUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 小程序类功能上传文件管理
 * @author guliuzhong
 */
@Controller
@RequestMapping("/admin/wxUploadDash")
public class WxUploadDash extends BaseController {
    WxUploadService wxUploadService;

    @Autowired
    public void setWxUploadService(WxUploadService wxUploadService) {
        this.wxUploadService = wxUploadService;
    }

}
