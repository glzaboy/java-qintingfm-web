package com.qintingfm.web.service.form.annotation;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 表单元素项目
 *
 * @author guliuzhong
 */
@Data
@Builder
public class FormItem implements Serializable, Comparable<FormItem> {
    String title;
    String fieldName;
    String value;
    String className;
    String tip;
    Integer order;
    boolean hide;
    boolean largeText;
    boolean useHtml;
    boolean uploadFile;


    @Override

    public int compareTo(FormItem o) {
        if (this.order == null) {
            return -1;
        }
        if (o.order == null) {
            return 1;
        }
        return this.order.compareTo(o.order);
    }
}
