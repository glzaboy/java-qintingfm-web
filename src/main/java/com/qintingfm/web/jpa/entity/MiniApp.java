package com.qintingfm.web.jpa.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.util.Date;

/**
 * @author guliuzhong
 */
@Entity
@Data
public class MiniApp {
    @Id
    @SequenceGenerator(sequenceName = "MiniApp_id_seq", name = "genMiniAppId", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genMiniAppId")
    Integer id;
    String name;
    String type;
    String appId;
    String appSecret;
    Boolean enable;
    Date createDate;
}
