package com.qintingfm.web.htmlsucker;

import lombok.Data;

import java.util.Collection;
import java.util.Date;

/**
 * 文章对象
 *
 * @author Winter Lau (javayou@gmail.com)
 */
@Data
public class Article {
    private String title;
    private Collection<String> keywords;
    private String description;
    private String content;
    private String author;
    private Date date;
    private String image;
}