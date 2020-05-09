package com.qintingfm.web.jpa.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author guliuzhong
 */
@Data
@Entity
@Table(name = "user", schema = "public")
public class User implements Serializable {
    @Id
    @SequenceGenerator(sequenceName = "user_id", name = "genUserId", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genUserId")
    private Long id;
    private String password;
    private String username;
    boolean isAccountNonExpired;
    boolean isAccountNonLocked;
    boolean isCredentialsNonExpired;
    boolean isEnabled;
}
