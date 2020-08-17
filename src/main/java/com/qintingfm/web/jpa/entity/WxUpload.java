package com.qintingfm.web.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class WxUpload {
    @Id
    @SequenceGenerator(sequenceName = "wx_upload_id_seq", name = "genWxUploadId", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genWxUploadId")
    private Long id;
    String url;
    String fileName;
    Date create;
}
