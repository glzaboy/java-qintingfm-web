package com.qintingfm.web.dao;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;

@Data
@Entity
public class Role implements GrantedAuthority {
    private Long id;
    private String authority;
    private  Integer userId;
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
