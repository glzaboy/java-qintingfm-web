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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * 百度云API封装
 * @author guliuzhong
 */
@Service
@Slf4j
public class BaiduAiApi extends BaseService {
    private final String SCHEME_FILE="file";
    private boolean isEnable = false;

    NetClient netClient;

    @Autowired
    public void setNetClient(NetClient netClient) {
        this.netClient = netClient;
    }

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
        if (!baiduyun.isPresent()) {
            isEnable = false;
            return;
        }
        isEnable = true;
        BaiduAiSettingVo baiduAiSettingVo = baiduyun.get();
        HashMap<String, String> hashMap = new HashMap<>(4);
        hashMap.put("client_id", baiduAiSettingVo.getClientId());
        hashMap.put("client_secret", baiduAiSettingVo.getClientSecret());
        baiduApiToken = netClient.newRequest().setUrl(apiUrl, hashMap).requestToObject(BaiduApiToken.class);
        this.baiduApiToken.setCreateData(new Date());
    }

    private String getToken() {
        if (baiduApiToken != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(baiduApiToken.getCreateData());
            c.add(Calendar.DATE, 4);

            if (new Date().before(c.getTime())) {
                updateToken();
                if (isEnable) {
                    return baiduApiToken.getAccessToken();
                }
                return null;
            }
        }
        updateToken();
        if (isEnable) {
            return baiduApiToken.getAccessToken();
        }
        return null;
    }

    /**
     * 图片文字识别
     *
     * 图片转成灰度，不降低识别
     * @param uri 文件路径
     * @return 图片识别结果
     */
    public String getPicInfo(URI uri) {
        String apiUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/general?access_token={access_token}";
        HashMap<String, String> hashMap = new HashMap<>(4);
        hashMap.put("access_token", getToken());
        if (!isEnable) {
            throw new BusinessException("Baidu ai接口异常，获取token异常");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Map<String, String> postMap = new HashMap<>(4);
        try {
            BufferedImage image;
            if(SCHEME_FILE.equalsIgnoreCase(uri.getScheme())){
                image = ImageIO.read(new File(uri.getPath()));
            }else{
                byte[] bytes = netClient.newRequest().setUrl(uri.toURL()).requestToBytes();
                image = ImageIO.read(new ByteArrayInputStream(bytes));
            }
            assert image!=null;
            int width = image.getWidth();
            int height = image.getHeight();
            BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            Graphics graphics = grayImage.getGraphics();
            graphics.drawImage(image, 0, 0, width, height, null);
            ImageIO.write(grayImage, "jpg", byteArrayOutputStream);
            postMap.put("image", String.valueOf(Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray())));
        } catch (IOException e) {
            log.warn("读取用户图片出错");
            return "{\"error\":\"读取用户图片出错\"}";
        }
        netClient.newRequest().setUrl(apiUrl, hashMap).setPostMap(postMap);
        return netClient.requestToString();
    }
    /**
     * 植物识别
     * 本地文件
     *
     * @param uri 远程或本地文件路径
     * @return 识别结果
     */
    public String plant(URI uri, int baikeNum) throws IOException {
        String apiUrl = "https://aip.baidubce.com/rest/2.0/image-classify/v1/plant?access_token={access_token}";
        HashMap<String, String> hashMap = new HashMap<>(2);
        hashMap.put("access_token", getToken());
        if (!isEnable) {
            throw new BusinessException("Baidu ai接口异常，获取token异常");
        }
        byte[] bytes;
        if (SCHEME_FILE.equalsIgnoreCase(uri.getScheme())){
            bytes = Files.readAllBytes(Paths.get(uri.getPath()));
        }else{
            bytes = netClient.newRequest().setUrl(uri.toURL(), null).requestToBytes();
        }
        netClient.newRequest().setUrl(apiUrl, hashMap);
        Map<String, String> postMap = new HashMap<>(2);
        postMap.put("image", String.valueOf(Base64.getEncoder().encodeToString(bytes)));
        postMap.put("baike_num", String.valueOf(baikeNum));
        netClient.setPostMap(postMap);
        return netClient.requestToString();
    }
}
