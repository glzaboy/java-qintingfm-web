package com.qintingfm.web.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * @author guliuzhong
 */
@Data
@Entity
@Table(name = "qt_blog")
@EqualsAndHashCode
public class Blog implements Serializable {
    @Id
    @SequenceGenerator(sequenceName="qt_blog_cat_id_seq",name = "genBlogid",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "genBlogid")
    private Integer postId;
    String title;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "contId",nullable = false)
    private BlogCont blogCont;
    private String shotCont;
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "qt_blog2cat",joinColumns = {@JoinColumn(name = "postId")},inverseJoinColumns = {@JoinColumn(name = "catId")})
    private Collection<Category> blogCategory;
    private Date dateCreated;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId",nullable = false)
    private User author;
}
