package com.qintingfm.web.service;

import com.qintingfm.web.pojo.BingBGImage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
@Slf4j
public class BingImageService {
    @Autowired
    NetClient netClient;
    Date lastUpdateDate;
    BingBGImage bingBGImage;

    public BingBGImage getImage(){
        netClient.setUrl("http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1");
        if(lastUpdateDate!=null && bingBGImage!=null){
            Calendar instance = Calendar.getInstance();
            instance.setTime(lastUpdateDate);
            Calendar instanceNow = Calendar.getInstance();
            instanceNow.setTime(new Date());
            if(instance.get(Calendar.DAY_OF_MONTH)==instanceNow.get(Calendar.DAY_OF_MONTH)){
                return bingBGImage;
            }
        }
        bingBGImage = netClient.requestToOjbect(BingBGImage.class);
        lastUpdateDate=new Date();
        return bingBGImage;
    }
}
