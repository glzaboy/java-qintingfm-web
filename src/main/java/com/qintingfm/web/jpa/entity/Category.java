package com.qintingfm.web.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author guliuzhong
 */
@Data
@Entity
@Table(name = "qt_categoryies")
public class Category implements Serializable {
    @Id
    @SequenceGenerator(sequenceName = "categories_id_seq", name = "genCatId", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genCatId")
    public Integer catId;
    public String title;
    public String description;
}
