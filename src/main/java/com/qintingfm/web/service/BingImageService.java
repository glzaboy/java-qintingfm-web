package com.qintingfm.web.service;

import com.qintingfm.web.pojo.BingBGImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BingImageService {
    @Autowired
    NetClient netClient;

    public BingBGImage getImage(){
        netClient.setUrl("http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1");
        BingBGImage bingBGImage = netClient.requestToOjbect(BingBGImage.class);
        return bingBGImage;
    }
}
