package com.qintingfm.web.jpa.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.UUID;

/**
 * 用户注册信息
 * @author guliuzhong
 */
@Entity
@Data
@Table(uniqueConstraints = {@UniqueConstraint(columnNames ={"userName"})})
public class UserRegister {
    @Id
    @SequenceGenerator(sequenceName = "user_register_id_seq", name = "genUserRegisterId", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genUserRegisterId")
    Long registerId;
    String userName;
    @Email(message = "邮件不合法")
    String email;
    @NotBlank(message = "电话不允许为空")
    String tel;
    @Column(length = 40, nullable = false)
    UUID activeKey;
    Date createDate;
    Date activeDate;
    Boolean isActive;
}