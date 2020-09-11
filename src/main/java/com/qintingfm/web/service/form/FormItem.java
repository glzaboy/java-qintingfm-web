package com.qintingfm.web.service.form;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * 表单元素项目
 *
 * @author guliuzhong
 */
@Data
@Builder
public class FormItem implements Serializable, Comparable<FormItem> {
    /**
     * 表单标题
     */
    String title;
    /**
     * 字段名称
     */
    String fieldName;
    /**
     * 字段值
     */
    String value;
    /**
     * 数据格式
     */
    String format;
    /**
     * java类型
     */
    String className;
    /**
     * 表单提示
     */
    String tip;
    /**
     * 表单排序
     */
    Integer order;
    /**
     * 表单元素
     */
    String element;
    /**
     * 是否隐藏
     */
    boolean hide;
    /**
     * html编辑器
     */
    boolean htmlEditor;
    /**
     * html编辑器带上传
     */
    boolean htmlEditUpload;
    /**
     * select选择器列表
     */
    Set<FormOption> formOption;
    /**
     * select 选择器值多选
     */
    String[] listValue;
    /**
     * select 选择器值多选
     */
    Collection<String> listKey;
    boolean multiple;


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
