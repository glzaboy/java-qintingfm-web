package com.qintingfm.web.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author guliuzhong
 */
@Data
@Entity
@Table(name = "qt_blog_comment")
public class BlogComment {
    @Id
    @SequenceGenerator(sequenceName="qt_blog_comment_id_seq",name = "genCommentId",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "genCommentId")
    Long id;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId",nullable = true)
    private User author;
    @Column(length = 1048576,nullable = false)
    private String cont;
    private Date createDate;
}
