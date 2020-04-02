package com.qintingfm.web.spider;

import com.qintingfm.web.jpa.entity.SettingItem;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * 推送搜索引擎
 *
 * @author guliuzhong
 */
public interface Spider {
    /**
     * 获取设置
     *
     * @param spiderName
     * @return
     */
    Stream<SettingItem> getSpiderSettings(String spiderName);

    /**
     * 推送到spider
     *
     * @param url
     * @return
     */
    String pushUrlToSpider(Collection<String> url);
}
