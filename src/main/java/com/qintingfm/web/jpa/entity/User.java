package com.qintingfm.web.jpa.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Entity
@Table(name = "user",schema = "public")
public class User   implements Serializable {
    @Id
    @SequenceGenerator(sequenceName="user_id",name = "genid",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "genid")
    private Integer id;
    private String password;
    private String username;
    boolean isAccountNonExpired;
    boolean isAccountNonLocked;
    boolean isCredentialsNonExpired;
    boolean isEnabled;
}
