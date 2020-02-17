package com.qintingfm.web;

import com.qintingfm.web.jpa.entity.Blog;
import com.qintingfm.web.service.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Optional;

@SpringBootTest
public class BlogServiceTests {
    BlogService blogService;
    @Autowired
    public void setBlogService(BlogService blogService) {
        this.blogService = blogService;
    }

    @Test
    void contextLoads() {
    }

    @Test
    void save() {
        Optional<Blog> blog = blogService.getBlog(26);
        Assert.notNull(blog.get());
        blogService.save(blog.get());
    }
}
