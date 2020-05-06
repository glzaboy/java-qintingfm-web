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
     * @param url
     * @return
     */
    String pushUrlToSpider(Collection<String> url);
}
