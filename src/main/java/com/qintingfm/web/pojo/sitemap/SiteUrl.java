package com.qintingfm.web.pojo.sitemap;

import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

public class SiteUrl {
    @XmlElement
    public String loc;
    @XmlElement
    public Date lastmod;
    @XmlElement
    public String changefreq;
    @XmlElement
    public String priority;
}
