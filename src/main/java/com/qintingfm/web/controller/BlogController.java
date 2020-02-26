package com.qintingfm.web.controller;

import com.qintingfm.web.common.AjaxDto;
import com.qintingfm.web.jpa.entity.Blog;
import com.qintingfm.web.jpa.entity.BlogComment;
import com.qintingfm.web.service.BlogService;
import com.qintingfm.web.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author guliuzhong
 */
@Controller
@RequestMapping("/blog")
@Slf4j
public class BlogController {
    CategoryService category;

    BlogService blogServer;
    CategoryService categoryService;

    @Autowired
    public void setBlogServer(BlogService blogServer) {
        this.blogServer = blogServer;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setCategory(CategoryService category) {
        this.category = category;
    }

    @RequestMapping(value = {"/view/{postId}", "/view/{postId}/{pageIndex}"})
    public ModelAndView detail(ModelAndView modelAndView, @PathVariable("postId") Integer postId, @PathVariable(value = "pageIndex", required = false) Integer pageIndex) {
        Optional<Blog> blog = blogServer.getBlog(postId);
        blog.ifPresent(item -> {
                modelAndView.addObject("blogPost", item);
                modelAndView.addObject("blogComment", blogServer.getBlogComment(item, pageIndex, null, 10));
            }
        );
        modelAndView.setViewName("blog/view");
        return modelAndView;
    }

    @RequestMapping(value = {"/postComment/{postId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public AjaxDto postComment(@PathVariable("postId") Integer postId, @PathVariable(value = "pageIndex", required = false) Integer pageIndex, @RequestParam("comment") String comment) {
        AjaxDto ajaxDto = new AjaxDto();
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            ajaxDto.setMessage("用户没有登录");
            try {
                Method loginPage = UserController.class.getMethod("loginPage", ModelAndView.class);
                ModelAndView modelAndView = new ModelAndView();
                ajaxDto.setLink(MvcUriComponentsBuilder.fromMethod(UserController.class, loginPage, modelAndView).build().toUriString());
                ajaxDto.setAutoJump(1);
            } catch (NoSuchMethodException e) {
                log.error(e.getMessage());
            }
            return ajaxDto;
        }
        Optional<Blog> blog = blogServer.getBlog(postId);
        blog.ifPresent(item -> {
                    BlogComment blogComment = new BlogComment();
                    Object details = authentication.getDetails();

//            blogComment.setAuthor(authentication.getName());
                    blogComment.setCont(comment);
                    blogServer.saveComment(blogComment, item);
                }
        );
        ajaxDto.setAutoJump(1);
        return ajaxDto;
    }

    @RequestMapping(value = {"/category", "/category/{page}"})
    public ModelAndView categoryList(ModelAndView modelAndView, @PathVariable(value = "page", required = false) Integer pageIndex) {
        Page<com.qintingfm.web.jpa.entity.Category> category = categoryService.getCategory(pageIndex, null, 10);
        modelAndView.addObject("allCategory", category.toList());
        modelAndView.setViewName("blog/category");
        return modelAndView;
    }
    @RequestMapping(value = {"/post/{postId}"})
    public ModelAndView post(ModelAndView modelAndView, @PathVariable(value = "postId", required = false) Integer postId) {
        Optional<Blog> blog = blogServer.getBlog(postId);
        blog.ifPresent(item->{
            modelAndView.addObject("blog", item);
        });
        modelAndView.setViewName("blog/post");
        return modelAndView;
    }
}
