package com.qintingfm.web.common;

import java.io.Serializable;

/**
 * 网页版本ajax请求带数据
 * @param <T>
 */
public class AjaxDataDto<T extends Serializable> extends AjaxDto{
    public T data;
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public AjaxDataDto(T data) {
        this.data = data;
        this.setAutoHide(".load");
    }
}
