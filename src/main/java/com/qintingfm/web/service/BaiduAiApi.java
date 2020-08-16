package com.qintingfm.web.service;

import com.qintingfm.web.common.exception.BusinessException;
import com.qintingfm.web.pojo.vo.settings.BaiduAiSettingVo;
import com.qintingfm.web.service.baiduai.BaiduApiToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class BaiduAiApi extends BaseService{
    private boolean isEnable=false;
    @Autowired
    NetClient netClient;

    private BaiduApiToken baiduApiToken = null;

    private void updateToken() {
        if (baiduApiToken != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(baiduApiToken.getCreateData());
            c.add(Calendar.DATE, 4);

            if (new Date().before(c.getTime())) {
                return;
            }
        }
        String apiUrl = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id={client_id}&&client_secret={client_secret}";
        Optional<BaiduAiSettingVo> baiduyun = settingService.getSettingBean("baiduyun", BaiduAiSettingVo.class);
        if (!baiduyun.isPresent()){
            isEnable=false;
            return;
        }
        isEnable=true;
        BaiduAiSettingVo baiduAiSettingVo = baiduyun.get();
        HashMap<String, String> hashMap = new HashMap<>(4);
        hashMap.put("client_id", baiduAiSettingVo.getClientId());
        hashMap.put("client_secret", baiduAiSettingVo.getClientSecret());
        netClient.setUrl(apiUrl, hashMap);
        baiduApiToken = netClient.requestToObject(BaiduApiToken.class);
        this.baiduApiToken.setCreateData(new Date());
    }

    private String getToken() {
        if (baiduApiToken != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(baiduApiToken.getCreateData());
            c.add(Calendar.DATE, 4);

            if (new Date().before(c.getTime())) {
                updateToken();
                if(isEnable){
                    return baiduApiToken.getAccessToken();
                }
                return null;
            }
        }
        updateToken();
        if(isEnable){
            return baiduApiToken.getAccessToken();
        }
        return null;
    }
    /**
     * 图片文字识别
     *
     * @param file 文件路径
     * @return 图片识别结果
     */
    public String getPicInfo(String file) {
        String apiUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/general?access_token={access_token}";
        HashMap<String, String> hashMap = new HashMap<>(4);
        hashMap.put("access_token", getToken());
        if(isEnable==false){
            throw new BusinessException("Baidu ai接口异常，获取token异常");
        }
        netClient.setUrl(apiUrl, hashMap);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Map<String, String> postMap = new HashMap<>(4);
        try {
            BufferedImage image = ImageIO.read(new File(file));
            int width = image.getWidth();
            int height = image.getHeight();
            /**
             * 图片转成灰度，不降低识别
             */
            BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            Graphics graphics = grayImage.getGraphics();
            graphics.drawImage(image,0,0,width,height,null);

            ImageIO.write(grayImage, "jpg", byteArrayOutputStream);
            postMap.put("image",String.valueOf(Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray())));
        } catch (IOException e) {
            log.warn("读取用户图片出错");
            return "{\"error\":\"读取用户图片出错\"}";
        }
        netClient.setPostMap(postMap);
        return netClient.requestToString();
    }
    /**
     * 植物识别
     *
     * @param file
     * @return
     */
    public String plant(String file,int baikeNum) {
        String apiUrl = "https://aip.baidubce.com/rest/2.0/image-classify/v1/plant?access_token={access_token}";
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("access_token", getToken());
        if(isEnable==false){
            throw new BusinessException("Baidu ai接口异常，获取token异常");
        }
        netClient.setUrl(apiUrl, hashMap);
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(Paths.get(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> postMap = new HashMap<>();
        postMap.put("image", String.valueOf(Base64.getEncoder().encodeToString(bytes)));
        postMap.put("baike_num", String.valueOf(baikeNum));
        netClient.setPostMap(postMap);
        return netClient.requestToString();
    }

}
