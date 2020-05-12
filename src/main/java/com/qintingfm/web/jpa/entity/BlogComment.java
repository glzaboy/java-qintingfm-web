package com.qintingfm.web.jpa.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author guliuzhong
 */
@Data
@Entity
@Table(name = "qt_blog_comment")
public class BlogComment {
    @Id
    @SequenceGenerator(sequenceName = "blog_comment_id_seq", name = "genCommentId", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genCommentId")
    Long id;
    @NotNull(message = "评论人不合法")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId")
    private User author;
    @Column(length = 1048576, nullable = false)
    @NotBlank(message = "评论不能为空")
    @Length(min = 10, message = "评论内容太短")
    private String cont;
    private Date createDate;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Blog blog;
}
