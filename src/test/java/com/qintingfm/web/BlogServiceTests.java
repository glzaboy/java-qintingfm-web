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
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
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
}
