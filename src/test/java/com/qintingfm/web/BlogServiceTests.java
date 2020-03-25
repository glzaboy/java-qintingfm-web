package com.qintingfm.web;

import com.qintingfm.web.jpa.entity.Blog;
import com.qintingfm.web.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
@Slf4j
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
