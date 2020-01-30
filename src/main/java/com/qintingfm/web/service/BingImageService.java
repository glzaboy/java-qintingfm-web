package com.qintingfm.web.service;

import com.qintingfm.web.pojo.BingBgImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * 必应背景图片服务
 * @author guliuzhong
 */
@Service
@Slf4j
public class BingImageService {
    private NetClient netClient;
    @Autowired
    public void setNetClient(NetClient netClient) {
        this.netClient = netClient;
    }
    private Date lastUpdateDate;
    private BingBgImage bgImage;

    public BingBgImage getImage(){
        netClient.setUrl("http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1");
        if(lastUpdateDate!=null && bgImage !=null){
            Calendar instance = Calendar.getInstance();
            instance.setTime(lastUpdateDate);
            Calendar instanceNow = Calendar.getInstance();
            instanceNow.setTime(new Date());
            if(instance.get(Calendar.DAY_OF_MONTH)==instanceNow.get(Calendar.DAY_OF_MONTH)){
                return bgImage;
            }
        }
        bgImage = netClient.requestToOjbect(BingBgImage.class);
        lastUpdateDate=new Date();
        return bgImage;
    }
}
