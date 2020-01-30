package com.qintingfm.web.service;

import com.qintingfm.web.jpa.BlogJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Blog {
    BlogJpa blogJpa;
    @Autowired
    public BlogJpa getBlogJpa() {
        return blogJpa;
    }
}
