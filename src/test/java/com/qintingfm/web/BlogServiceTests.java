package com.qintingfm.web;

import com.qintingfm.web.jpa.entity.Blog;
import com.qintingfm.web.pojo.request.BlogPojo;
import com.qintingfm.web.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@Slf4j
public class BlogServiceTests {
    BlogService blogService;

    @Autowired
    public void setBlogService(BlogService blogService) {
        this.blogService = blogService;
    }

    @Test
    @Transactional
    public void validBlogPojo() {
        BlogPojo blogPojo=new BlogPojo();
        blogPojo.setTitle("test");
        blogPojo.setCont("这里是内容");
        blogPojo.setAuthorId(1L);
        blogPojo.setState(false);
        blogPojo.setCatNames(Stream.of("a").toArray(String[]::new));
        blogService.validatePojoAndThrow(blogPojo);
    }

    @Test
    @Transactional
    public void saveBlogPojo() {
        BlogPojo blogPojo=new BlogPojo();
        blogPojo.setTitle("test");
        blogPojo.setCont("这里是内容");
        blogPojo.setAuthorId(1L);
        blogPojo.setState(false);
        blogPojo.setCatNames(Stream.of("a").toArray(String[]::new));
        blogService.save(blogPojo);
    }
}
