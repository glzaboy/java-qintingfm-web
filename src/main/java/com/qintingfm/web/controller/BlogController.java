package com.qintingfm.web.controller;

import com.qintingfm.web.common.AjaxDto;
import com.qintingfm.web.jpa.entity.Blog;
import com.qintingfm.web.jpa.entity.BlogComment;
import com.qintingfm.web.jpa.entity.Category;
import com.qintingfm.web.pojo.WebUserDetails;
import com.qintingfm.web.pojo.request.BlogPojo;
import com.qintingfm.web.service.BlogService;
import com.qintingfm.web.service.CategoryService;
import com.qintingfm.web.service.HtmlService;
import com.qintingfm.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    HtmlService htmlService;

    UserService userService;

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

    @Autowired
    public void setHtmlService(HtmlService htmlService) {
        this.htmlService = htmlService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = {"/view/{postId}", "/view/{postId}/{pageIndex}"})
    public ModelAndView detail(ModelAndView modelAndView, @PathVariable("postId") Integer postId, @PathVariable(value = "pageIndex", required = false) Integer pageIndex) {
        Optional<Blog> blog = blogServer.getBlog(postId);
        blog.ifPresent(item -> {
                    item.setTitle(htmlService.decodeEntityHtml(item.getTitle()));
                    modelAndView.addObject("blogPost", item);
                    modelAndView.addObject("blogComment", blogServer.getBlogComment(item, pageIndex, null, 10));
                }
        );
        modelAndView.setViewName("blog/view");
        return modelAndView;
    }

    @RequestMapping(value = {"/postComment/{postId}"}, method = {RequestMethod.POST})
    @ResponseBody
    @Transactional()
    public AjaxDto postComment(@PathVariable("postId") Integer postId, @PathVariable(value = "pageIndex", required = false) Integer pageIndex, @RequestParam("cont") String cont) {
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
                    WebUserDetails principal = (WebUserDetails) authentication.getPrincipal();
                    blogComment.setAuthor(userService.getUser(principal.getUsername()));
                    blogComment.setCont(cont);
                    blogComment.setBlog(item);
                    blogServer.saveComment(blogComment);
                }
        );
        if (ajaxDto.getError() != null) {
            ajaxDto.setMessage("评论出错。");
        } else {
            ajaxDto.setAutoJump(1);
        }
        return ajaxDto;
    }


    @RequestMapping(value = {"/post/{postId}"}, method = {RequestMethod.GET})
    public ModelAndView postView(ModelAndView modelAndView, @PathVariable(value = "postId", required = false) Integer postId) {
        Optional<Blog> blog = blogServer.getBlog(postId);
        blog.ifPresent(item -> {
            item.setTitle(htmlService.decodeEntityHtml(item.getTitle()));
            modelAndView.addObject("blog", item);
            List<Integer> catIds = new ArrayList<>();
            List<Integer> collect = item.getBlogCategory().stream().map(category -> {
                return category.getCatId();
            }).collect(Collectors.toList());
            modelAndView.addObject("blogCatList", collect);
        });
        Page<Category> allCategory = category.getAllCategory(1, 10000);
        modelAndView.addObject("allCategory", allCategory);
        modelAndView.setViewName("blog/post");
        return modelAndView;
    }

    @RequestMapping(value = {"/post/{postId}"}, method = {RequestMethod.POST})
    @ResponseBody
    public AjaxDto post(@PathVariable(value = "postId", required = false) Integer postId, @RequestParam("cont") String cont, @RequestParam("title") String title, @RequestParam(value = "catNameList", required = false) List<String> catNameList) {
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
        BlogPojo.BlogPojoBuilder builder = BlogPojo.builder();
        builder.postId(postId);
        builder.title(title);
        builder.cont(cont);
        builder.catNames(catNameList);
        Blog save = blogServer.save(builder.build());
        ajaxDto.setMessage("操作成功");
        try {
            Method detail = BlogController.class.getDeclaredMethod("detail", ModelAndView.class, Integer.class, Integer.class);
            String s = MvcUriComponentsBuilder.fromMethod(BlogController.class, detail, null, Integer.valueOf(save.getPostId()), null).build().toString();
            ajaxDto.setLink(s);
        } catch (NoSuchMethodException e) {
            log.error("找不到博客文档的url");
        }

        return ajaxDto;
    }


    @RequestMapping(value = {"/category"}, method = {RequestMethod.GET})
    public ModelAndView categoryList(ModelAndView modelAndView, @PathVariable(value = "page", required = false) Integer pageIndex) {
        Page<com.qintingfm.web.jpa.entity.Category> category = categoryService.getCategory(pageIndex, null, 100);
        modelAndView.addObject("allCategory", category.toList());
        modelAndView.setViewName("blog/category");
        return modelAndView;
    }

    @RequestMapping(value = {"/category/{catName}", "/category/{catName}/{page}"})
    public ModelAndView categoryArticleList(ModelAndView modelAndView, @PathVariable(value = "page", required = false) Integer pageIndex, @PathVariable(value = "catName", required = true) String catName) {
        ArrayList<String> catNames = new ArrayList<>();
        catNames.add(catName);
        List<Category> category = categoryService.getCategory(catNames);
        Page<Blog> catBlogList = blogServer.getCatBlogList(category, pageIndex, null, 30);
        modelAndView.addObject("catBlog", catBlogList);
        modelAndView.setViewName("blog/categoryArticleList");
        return modelAndView;
    }
}
