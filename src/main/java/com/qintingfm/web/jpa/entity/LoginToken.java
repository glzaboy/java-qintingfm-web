package com.qintingfm.web.jpa.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author guliuzhong
 */
@Entity
@Data
public class LoginToken implements Serializable {
    String username;
    @Id
    String series;
    String token;
    Date lastUsed;
}
