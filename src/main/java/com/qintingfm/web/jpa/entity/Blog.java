package com.qintingfm.web.jpa.entity;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author guliuzhong
 */
@Data
@Entity
@Table(name = "qt_blog")
public class Blog implements Serializable {
    @Id
    @SequenceGenerator(sequenceName = "qt_blog_cat_id_seq", name = "genBlogId", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genBlogId")
    private Integer postId;
    String title;
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "contId", nullable = false)
    private BlogCont blogCont;
    private String shotCont;
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinTable(name = "qt_blog2cat", joinColumns = {@JoinColumn(name = "postId")}, inverseJoinColumns = {@JoinColumn(name = "catId")})
    private List<Category> blogCategory;
    private Date dateCreated;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId")
    private User author;
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "blog")
    private List<BlogComment> comment;
    String state;

}
