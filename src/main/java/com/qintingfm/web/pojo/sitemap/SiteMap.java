package com.qintingfm.web.pojo.sitemap;

import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * @author guliuzhong
 */
@XmlRootElement(name = "urlset")
@Setter
public class SiteMap implements Serializable {
    @XmlElement
    List<SiteUrl> url;

}
