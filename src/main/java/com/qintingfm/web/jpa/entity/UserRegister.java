package com.qintingfm.web.jpa.entity;

import lombok.Data;
import org.hibernate.annotations.Columns;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(uniqueConstraints = {@UniqueConstraint(columnNames ={"userName"})})
public class UserRegister {
    @Id
    @SequenceGenerator(sequenceName = "User_Register_id", name = "genUserRegisterId", allocationSize = 1)
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