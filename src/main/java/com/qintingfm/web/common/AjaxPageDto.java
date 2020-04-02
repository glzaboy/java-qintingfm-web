package com.qintingfm.web.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

/**
 * 网页版本ajax请求列表带分页
 *
 * @param <T>
 * @author guliuzhong
 */
@Data
public class AjaxPageDto<T extends Serializable> extends AjaxDto {
    /**
     * 当前页数
     */
    private long page;
    /**
     * 数据内容
     */
    private Collection<T> data;
    /**
     * 总条数
     */
    private long total;
    /**
     * 总页数
     */
    private long totalPage;


    public AjaxPageDto(Collection<T> data, long page, long total, long totalPage) {
        this.page = page;
        this.data = data;
        this.total = total;
        this.totalPage = totalPage;
    }

    public AjaxPageDto(Collection<T> data) {
        this(data, 0, 0, 0);
    }
}
