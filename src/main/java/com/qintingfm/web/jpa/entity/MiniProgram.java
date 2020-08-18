package com.qintingfm.web.jpa.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.util.Date;

/**
 * 各类小程序配置
 * @author guliuzhong
 */
@Data
@Entity
public class MiniProgram {
    @Id
    @SequenceGenerator(sequenceName = "MiniProgram_id_seq", name = "genMiniProgramId", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genMiniProgramId")
    Integer id;
    String name;
    String type;
    String appId;
    String appKey;
    Boolean enable;
    Date createDate;
}
