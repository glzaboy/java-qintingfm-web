package com.qintingfm.web.jpa.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 设置的名称和关系映射
 * @author guliuzhong
 */
@Data
@Entity
public class SettingInfo {
    @Id
    String name;
    String className;
}