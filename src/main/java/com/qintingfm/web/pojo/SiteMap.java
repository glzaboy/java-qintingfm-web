package com.qintingfm.web.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import java.io.Serializable;

@XmlRootElement(name = "urlset")
@Setter
@XmlSchema(xmlns={@XmlNs(namespaceURI = "http://www.sitemaps.org/schemas/sitemap/0.9",prefix = "site")})
public class SiteMap implements Serializable {
    @XmlElement(namespace = "http://www.sitemaps.org/schemas/sitemap/0.9")
    String name;

}
