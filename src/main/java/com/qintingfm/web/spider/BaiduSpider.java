package com.qintingfm.web.spider;

import com.qintingfm.web.jpa.entity.SettingItem;
import com.qintingfm.web.service.NetClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author guliuzhong
 */
@Service
@Slf4j
public class BaiduSpider extends BaseSpider {
    final String SpiderName = "baidu";
    List<String> postMap = Arrays.asList("site", "token");
    NetClient netClient;

    @Autowired
    public void setNetClient(NetClient netClient) {
        this.netClient = netClient;
    }

    @Override
    @Transactional(readOnly = true)
    public String pushUrlToSpider(Collection<String> url) {
        Stream<SettingItem> spiderSettings = getSpiderSettings(SpiderName);
        Map<String, String> collect1 = spiderSettings.collect(Collectors.toMap(SettingItem::getKey, SettingItem::getValue));
        boolean enable = settingService.isEnable(collect1);
        if (enable) {
            Map<String, String> collect = collect1.entrySet().stream().filter(item -> {
                return postMap.contains(item.getKey());
            }).collect(Collectors.toMap(stringStringEntry -> {
                return stringStringEntry.getKey();
            }, stringStringEntry -> {
                return stringStringEntry.getValue();
            }));
            netClient.setUrl("http://data.zz.baidu.com/urls", collect);
            StringBuffer postData = new StringBuffer();
            url.stream().forEach(item -> {
                postData.append(item).append("\n");
            });
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Content-Type", "text/plain");
            netClient.setHeaderMap(headerMap);
            netClient.setBin("text/plain", postData.toString().getBytes());
            String s = netClient.requestToString();
            log.info("baidu推送结果{}", s);
            return "baidu推送结果"+s;
        } else {
            log.info("baidu不推送");
            return "baidu不推送";
        }
    }
}
