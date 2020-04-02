package com.qintingfm.web.jpa.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * @author guliuzhong
 */
@Data
@Entity
public class Role implements GrantedAuthority {
    @Id
    @SequenceGenerator(sequenceName = "role_id", name = "genRoleId", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genRoleId")
    private Long id;
    private String authority;
    private Long userId;

    @Override
    public String getAuthority() {
        return authority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
