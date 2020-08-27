package com.qintingfm.web.pojo;

import com.qintingfm.web.pojo.vo.BaseVo;
import com.qintingfm.web.service.form.annotation.FieldAnnotation;
import com.qintingfm.web.service.form.annotation.FormAnnotation;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类别
 * @author guliuzhong
 */
@FormAnnotation(method = "post",hideSubmit = true,value = "小程序应用配置")
@Data
@EqualsAndHashCode(callSuper = true)
public class MiniAppVo extends BaseVo {
    @FieldAnnotation(title = "text",tip = "",order = 1,hide = false)
    Integer appId;
    @FieldAnnotation(title = "select",tip = "",order = 2,listBeanName = "wxService",listMethod = "getSelect",multiple = true)
    String[] type;
    @FieldAnnotation(title = "selecttypeInt",tip = "",order = 2,listBeanName = "wxService",listMethod = "getSelect",multiple = true)
    Integer[] typeInt;
    @FieldAnnotation(title = "selecttypeLog",tip = "",order = 2,listData = {"1","1L","2","2L"})
    Long[] typeLong;
    @FieldAnnotation(title = "selecttypeDouble",tip = "",order = 2,listData = {"1.0","1d","2.0","2d"})
    Double[] typeDouble;
    @FieldAnnotation(title = "selecttypeFloat",tip = "",order = 2,listData = {"1.0","1f","2.0","2f"})
    Float[] typeFloat;
    @FieldAnnotation(title = "selecttypeCharacter",tip = "",order = 2,listData = {"a","a","b","b","c","c"})
    Character[] typeCharacter;
    @FieldAnnotation(title = "largeText",tip = "",order = 2,largeText = true)
    String appkey;
    @FieldAnnotation(title = "html",tip = "",order = 2,largeText = true,htmlEditor = true)
    String seckey;
    @FieldAnnotation(title = "htmlupload",tip = "",order = 2,largeText = true,htmlEditor = true,htmlEditorUpload = true)
    String seckey2;
    @FieldAnnotation(title = "Boolean",tip = "",order = 2)
    Boolean myboolean;
}
