package com.qintingfm.web.jpa.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author guliuzhong
 */
@Data
@Entity
@Table(name = "qt_blog_cont")
public class BlogCont {
    @Id
    @SequenceGenerator(sequenceName = "blog_cont_id_seq", name = "genContId", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genContId")
    public Long contId;
    @Column(length = 1048576, nullable = false)
    public String cont;
}
