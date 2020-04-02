package com.qintingfm.web.jpa.entity;


import lombok.Data;

import javax.persistence.*;

/**
 * @author guliuzhong
 */
@Entity
@Table(name = "qt_setting", indexes = {@Index(name = "qt_setting_name", columnList = "name", unique = false), @Index(name = "qt_setting_name_key", columnList = "name,key", unique = true)})
@Data
public class SettingItem {
    @Id
    @SequenceGenerator(sequenceName = "setting_id", name = "genSettingId", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genSettingId")
    Integer id;
    String name;
    String fields;
    String tip;
    String key;
    String value;
}
