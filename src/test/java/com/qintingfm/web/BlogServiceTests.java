package com.qintingfm.web;

import com.qintingfm.web.jpa.entity.Blog;
import com.qintingfm.web.jpa.entity.BlogCont;
import com.qintingfm.web.jpa.entity.Category;
import com.qintingfm.web.service.BlogService;
import com.qintingfm.web.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@Slf4j
public class BlogServiceTests {
    BlogService blogService;
    CategoryService categoryService;
    @Autowired
    public void setBlogService(BlogService blogService) {
        this.blogService = blogService;
    }
    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
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
    @Test
    @Transactional
    void testAdd() {
        Blog blog=new Blog();
        blog.setTitle("test");
        BlogCont blogCont=new BlogCont();
        blogCont.setCont("test");
        blog.setBlogCont(blogCont);
        List<String> 其它 = Stream.of("其它").map(item -> {
            return (String) item;
        }).collect(Collectors.toList());
        Collection<Category> category = categoryService.getCategory(其它);
        blog.setBlogCategory(category.stream().collect(Collectors.toList()));
        Blog blogOptional = blogService.save(blog);
        Assert.notNull(blogOptional.getPostId());
    }
}
