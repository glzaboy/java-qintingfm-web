package com.qintingfm.web.jpa.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "qt_blog_cont")
public class BlogCont {
    @Id
    @SequenceGenerator(sequenceName="qt_blog_cont_id_seq",name = "genid",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "genid")
    private Long contId;
    @Column(length = 1048576,nullable = false)
    private String cont;
}
