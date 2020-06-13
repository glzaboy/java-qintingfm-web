package com.qintingfm.web.pojo.sitemap;

import com.sun.xml.txw2.annotation.XmlNamespace;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "urlset")
@Setter
public class SiteMap implements Serializable {
    @XmlElement
    List<SiteUrl> url;

}
