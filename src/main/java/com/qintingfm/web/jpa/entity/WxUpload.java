package com.qintingfm.web.jpa.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * 微信上传文件
 * @author guliuzhong
 */
@Data
@Entity
public class WxUpload {
    @Id
    @SequenceGenerator(sequenceName = "wx_upload_id_seq", name = "genWxUploadId", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genWxUploadId")
    private Long id;
    String url;
    String fileName;
    String appId;
    String actionType;
    String processStatus;
    @Lob
    @Type(type="org.hibernate.type.TextType")
    String processText;
    Date createDate;
}
