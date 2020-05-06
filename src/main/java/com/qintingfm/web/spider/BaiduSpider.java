package com.qintingfm.web.spider;

import com.qintingfm.web.service.NetClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author guliuzhong
 */
@Service
@Slf4j
public class BaiduSpider extends BaseSpider {
    final String SpiderName = "baidu";
    NetClient netClient;

    @Autowired
    public void setNetClient(NetClient netClient) {
        this.netClient = netClient;
    }

    @Override
    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public String pushUrlToSpider(Collection<String> url) {
        Optional<BaiduSpiderSetting> settingBean = settingService.getSettingBean(SpiderName, BaiduSpiderSetting.class);
        if(settingBean.isPresent()){
            BaiduSpiderSetting baiduSpiderSetting = settingBean.get();
            Boolean enable = baiduSpiderSetting.getEnable();
            if (enable) {
                ConcurrentHashMap<String,String> concurrentHashMap=new ConcurrentHashMap<>(4);
                concurrentHashMap.put("token",baiduSpiderSetting.getToken());
                concurrentHashMap.put("site",baiduSpiderSetting.getSite());
                netClient.setUrl("http://data.zz.baidu.com/urls", concurrentHashMap);
                StringBuilder postData = new StringBuilder();
                url.forEach(item -> postData.append(item).append("\n"));
                Map<String, String> headerMap = new HashMap<>(4);
                headerMap.put("Content-Type", "text/plain");
                netClient.setHeaderMap(headerMap);
                netClient.setBin("text/plain", postData.toString().getBytes());
                String s = netClient.requestToString();
                log.info("baidu推送结果{}", s);
                return s;
            } else {
                log.info("baidu不推送");
                return "baidu不推送";
            }
        }else{
            log.info("baidu不推送");
            return "baidu不推送";
        }
    }
}
