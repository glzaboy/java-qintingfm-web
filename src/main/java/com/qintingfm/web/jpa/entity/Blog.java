package com.qintingfm.web.jpa.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
    @NotBlank(message = "标题不能为空")
    @Length(min = 4,message = "标题至少长度为四个字符")
    String title;
    @Id
    @SequenceGenerator(sequenceName = "qt_blog_cat_id_seq", name = "genBlogid", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genBlogid")
    private Integer postId;
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "contId", nullable = false)
    @Valid
    private BlogCont blogCont;
    private String shotCont;
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinTable(name = "qt_blog2cat", joinColumns = {@JoinColumn(name = "postId")}, inverseJoinColumns = {@JoinColumn(name = "catId")})
    private List<Category> blogCategory;
    private Date dateCreated;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId", nullable = true)
    private User author;
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY,mappedBy = "blog")
    private List<BlogComment> Comment;

}
