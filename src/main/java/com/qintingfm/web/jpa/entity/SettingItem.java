package com.qintingfm.web.jpa.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author guliuzhong
 */
@Entity
@Table(name = "qt_setting", indexes = {@Index(name = "qt_setting_name", columnList = "name"), @Index(name = "qt_setting_name_key", columnList = "name,key", unique = true)})
@Data
public class SettingItem {
    @Id
    @SequenceGenerator(sequenceName = "setting_id_seq", name = "genSettingId", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genSettingId")
    Integer id;
    String name;
    String key;
    String value;
}
