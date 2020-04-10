package com.qintingfm.web;

import com.qintingfm.web.pojo.request.BlogPojo;
import com.qintingfm.web.service.BlogService;
import com.qintingfm.web.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    public void validBlogPojo(){
        BlogPojo.BlogPojoBuilder builder = BlogPojo.builder();
        builder.title("test");
        builder.cont("这里是内容");
        builder.authorId(1l);
        builder.state("draft");
        builder.catNames(Stream.of("a").collect(Collectors.toList()));
        blogService.validatePojoAndThrow(builder.build());
    }
    @Test
    public void saveBlogPojo(){
        BlogPojo.BlogPojoBuilder builder = BlogPojo.builder();
        builder.title("test");
        builder.cont("这里是内容");
        builder.authorId(1l);
        builder.state("draft");
        builder.catNames(Stream.of("a").collect(Collectors.toList()));
        blogService.save(builder.build());
    }
}
