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
    @SequenceGenerator(sequenceName = "qt_categories_cat_id_seq", name = "genCatId", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genCatId")
    private Integer catId;
    private String title;
    private String description;

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
