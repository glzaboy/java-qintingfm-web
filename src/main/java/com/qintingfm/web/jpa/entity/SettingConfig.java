package com.qintingfm.web.jpa.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "qt_SettingConfig",indexes = {@Index(name = "idx_settconfig_name",columnList = "name")})
public class SettingConfig {
    @Id
    @SequenceGenerator(sequenceName = "setting_config_id", name = "genSettingConfigId", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genSettingConfigId")
    Integer Id;
    @Column(length = 10)
    String name;
    @Column(length = 20)
    String key;
    @Lob
    String defaultValue;
    @Column(length = 10)
    String keyType;
    @Column(length = 64)
    String keyFiledName;
    Integer sortNum;
}
