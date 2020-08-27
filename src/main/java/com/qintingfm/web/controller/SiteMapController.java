package com.qintingfm.web.controller;

import com.qintingfm.web.jpa.entity.Blog;
import com.qintingfm.web.pojo.sitemap.SiteMap;
import com.qintingfm.web.pojo.sitemap.SiteUrl;
import com.qintingfm.web.service.BlogService;
import com.qintingfm.web.pojo.vo.settings.SiteSettingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * @author guliuzhong
 */
@RestController
public class SiteMapController extends BaseController{
    BlogService blogServer;

    @Autowired
    public void setBlogServer(BlogService blogServer) {
        this.blogServer = blogServer;
    }
    @GetMapping("sitemap.xml")
    SiteMap siteMap(){
        SiteSettingVo siteSetting = getSiteSetting();
        SiteMap siteMap=new SiteMap();
        ArrayList<SiteUrl> siteUrlList=new ArrayList<>();
        Page<Blog> blogList = blogServer.getBlogList(0, 1, null, 500);
        blogList.forEach(item->{
            SiteUrl siteUrl=new SiteUrl();
            siteUrl.loc=siteSetting.getMainUrl()+"/blog/view/"+item.getPostId();
            siteUrl.lastmod=item.getDateCreated();
            siteUrl.changefreq="daily";
            siteUrl.priority="1.0";
            siteUrlList.add(siteUrl);
        });
        siteMap.setUrl(siteUrlList);
        return siteMap;
    }
    @GetMapping("robots.txt")
    @ResponseBody
    String robots(){
        SiteSettingVo siteSetting = getSiteSetting();
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("User-agent: *").append("\n");
        stringBuffer.append("Disallow: /admin/").append("\n");
        stringBuffer.append("Sitemap : ").append(siteSetting.getMainUrl()).append("/sitemap.xml").append("\n");
        return stringBuffer.toString();
    }
}
