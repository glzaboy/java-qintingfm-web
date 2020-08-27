package com.qintingfm.web.jpa.entity;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Indexed;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author guliuzhong
 */
@Entity
@Data
@Table(indexes = {@Index(name = "spider_imported",columnList = "isFetched"),@Index(name = "spider_fetched",columnList ="isFetched" )})
public class SpiderEnter {
    @Id
    @SequenceGenerator(sequenceName = "spider_enter_id_seq", name = "genSpiderEnterId", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genSpiderEnterId")
    Integer spiderId;
    String keyWord;
    String finalUrl;
    String title;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    String content;
    Boolean isFetched;
    Boolean isImported;

}
