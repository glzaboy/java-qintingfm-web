package com.qintingfm.web.spider;

import java.util.Collection;

/**
 * 推送搜索引擎
 *
 * @author guliuzhong
 */
public interface Spider {
    /**
     * 推送到spider
     *
     * @param url 推送的 url
     * @return 返回推送结果
     */
    String pushUrlToSpider(Collection<String> url);
}
