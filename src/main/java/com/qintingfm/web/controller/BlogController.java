package com.qintingfm.web.controller;

import com.qintingfm.web.jpa.entity.Blog;
import com.qintingfm.web.service.BlogService;
import com.qintingfm.web.service.Category;
import com.qintingfm.web.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

/**
 * @author guliuzhong
 */
@Controller
@RequestMapping("/blog")
public class BlogController {
    Category category;

    BlogService blogServer;
    @Autowired
    public void setBlogServer(BlogService blogServer) {
        this.blogServer = blogServer;
    }

    CategoryService categoryService;
    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setCategory(Category category) {
        this.category = category;
    }

    @RequestMapping("/view/{postId}")
    public ModelAndView detail(ModelAndView modelAndView, @PathVariable("postId") Integer postId) {
        Optional<Blog> blog = blogServer.getBlog(postId);
        blog.ifPresent(item -> modelAndView.addObject("blogPost", item));
        modelAndView.setViewName("blog/view");
        return modelAndView;
    }

    @RequestMapping(value = {"/category", "/category/{page}"})
    public ModelAndView categoryList(ModelAndView modelAndView, @PathVariable(value = "page", required = false) Integer pageIndex) {
        Page<com.qintingfm.web.jpa.entity.Category> category = categoryService.getCategory(pageIndex, null, 10);
        modelAndView.addObject("allCategory", category.toList());
        modelAndView.setViewName("blog/category");
        return modelAndView;
    }
}
