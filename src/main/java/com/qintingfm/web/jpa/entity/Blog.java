package com.qintingfm.web.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "qt_blog")

public class Blog implements Serializable {
    @Id
    @SequenceGenerator(sequenceName="qt_blog_cat_id_seq",name = "genBlogid",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "genBlogid")
    private Integer postId;
    String title;
    @OneToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name = "cont_id",nullable = false)
    private BlogCont blogCont;
    private Date dateCreated;
}
