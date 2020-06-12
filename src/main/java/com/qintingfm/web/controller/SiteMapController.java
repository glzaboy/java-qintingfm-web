package com.qintingfm.web.controller;

import com.qintingfm.web.pojo.SiteMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SiteMapController {
    @GetMapping("sitemap.xml")
    SiteMap siteMap(){
        return new SiteMap();
    }
}
