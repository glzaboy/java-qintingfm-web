package com.qintingfm.web.controller;

import com.qintingfm.web.jpa.BlogJpa;
import com.qintingfm.web.service.Category;
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
    BlogJpa blogJpa;
    Category category;


    @Autowired
    public void setBlogJpa(BlogJpa blogJpa) {
        this.blogJpa = blogJpa;
    }

    @Autowired
    public void setCategory(Category category) {
        this.category = category;
    }

    @RequestMapping("/view/{postId}")
    public ModelAndView detail(ModelAndView modelAndView, @PathVariable("postId") Integer postId) {
        Optional<com.qintingfm.web.jpa.entity.Blog> byId = blogJpa.findById(postId);
        byId.ifPresent(blog -> modelAndView.addObject("blogPost", blog));
        modelAndView.setViewName("blog/view");
        return modelAndView;
    }

    @RequestMapping(value = {"/category", "/category/{page}"})
    public ModelAndView categoryList(ModelAndView modelAndView, @PathVariable(value = "page", required = false) Integer pageIndex) {
        pageIndex = pageIndex == null ? 1 : pageIndex;
        Page<com.qintingfm.web.jpa.entity.Category> allCategory = category.getAllCategory(pageIndex, 10);
        modelAndView.addObject("allCategory", allCategory.toList());
        modelAndView.setViewName("blog/category");
        return modelAndView;
    }
}
