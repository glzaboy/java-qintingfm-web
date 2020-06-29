package com.qintingfm.web.controller;

import com.qintingfm.web.common.AjaxDto;
import com.qintingfm.web.common.exception.ResourceNotFoundException;
import com.qintingfm.web.jpa.entity.*;
import com.qintingfm.web.pojo.request.BlogPojo;
import com.qintingfm.web.pojo.request.UploadError;
import com.qintingfm.web.pojo.request.UploadImagePojo;
import com.qintingfm.web.service.BlogService;
import com.qintingfm.web.service.CategoryService;
import com.qintingfm.web.service.HtmlService;
import com.qintingfm.web.storage.Manager;
import com.qintingfm.web.storage.ManagerException;
import com.qintingfm.web.storage.StorageObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author guliuzhong
 */
@Controller
@RequestMapping("/blog")
@Slf4j
public class BlogController extends BaseController{
    BlogService blogServer;

    Manager manager;
    CategoryService categoryService;
    HtmlService htmlService;

    @Autowired
    public void setBlogServer(BlogService blogServer) {
        this.blogServer = blogServer;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setHtmlService(HtmlService htmlService) {
        this.htmlService = htmlService;
    }

    @Autowired
    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @RequestMapping(value = {"/view/{postId}", "/view/{postId}/{pageIndex}"})
    public ModelAndView detail(ModelAndView modelAndView, @PathVariable("postId") Integer postId, @PathVariable(value = "pageIndex", required = false) Integer pageIndex)  throws ResourceNotFoundException {
        Optional<Blog> blog = blogServer.getBlog(postId);
        blog.ifPresent(item -> {
                    item.setTitle(htmlService.decodeEntityHtml(item.getTitle()));
                    modelAndView.addObject("blogPost", item);
                    modelAndView.addObject("blogComment", blogServer.getBlogComment(item, pageIndex, null, 10));
                }
        );
        blog.orElseThrow(()-> new ResourceNotFoundException("您要查看的内容已经不存在，请查看其它内容。"));
        modelAndView.setViewName("blog/view");
        modelAndView.addObject("site", getSiteSetting());
        return modelAndView;
    }

    @RequestMapping(value = {"/postComment/{postId}"}, method = {RequestMethod.POST})
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxDto postComment(@PathVariable("postId") Integer postId, @RequestParam("cont") String cont) {
        AjaxDto ajaxDto = new AjaxDto();
        Optional<User> loginUser = getLoginUser();
        if (!loginUser.isPresent()) {
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
                    User user = loginUser.get();
                    blogComment.setAuthor(user);
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
        Optional<Blog> blogDb = blogServer.getBlog(postId);
        Blog blogtmp=new Blog();
        BlogCont blogCont=new BlogCont();
        blogCont.setCont("");
        blogtmp.setBlogCont(blogCont);
        blogtmp.setBlogCategory(new ArrayList<>());
        blogtmp.setTitle("");
        Blog blog = blogDb.orElse(blogtmp);
        blog.setTitle(htmlService.decodeEntityHtml(blog.getTitle()));
        modelAndView.addObject("blog", blog);
        List<Integer> collect = blog.getBlogCategory().stream().map(Category::getCatId).collect(Collectors.toList());
        modelAndView.addObject("blogCatList", collect);
        Page<Category> allCategory = categoryService.getAllCategory(1, 10000);
        modelAndView.addObject("allCategory", allCategory);
        modelAndView.setViewName("blog/post");
        modelAndView.addObject("site", getSiteSetting());
        return modelAndView;
    }

    @RequestMapping(value = {"/post/{postId}"}, method = {RequestMethod.POST})
    @ResponseBody
    @Transactional(rollbackFor = {Exception.class})
    public AjaxDto post(@PathVariable(value = "postId", required = false) Integer postId, @RequestParam("cont") String cont, @RequestParam("title") String title, @RequestParam(value = "catNames", required = false) List<String> catNames, @RequestParam(value = "state", required = false) String state) {
        AjaxDto ajaxDto = new AjaxDto();
        Optional<User> loginUser = getLoginUser();
        if (!loginUser.isPresent()) {
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
        builder.postId(postId>0?postId:null);
        builder.title(title);
        builder.cont(cont);
        builder.catNames(catNames);
        builder.authorId(loginUser.get().getId());
        if (state != null) {
            builder.state("publish");
        } else {
            builder.state("draft");
        }
        Blog save = blogServer.save(builder.build());
        ajaxDto.setMessage("操作成功");
        try {
            Method detail = BlogController.class.getDeclaredMethod("detail", ModelAndView.class, Integer.class, Integer.class);
            String s = MvcUriComponentsBuilder.fromMethod(BlogController.class, detail, null, save.getPostId(), null).build().toString();
            ajaxDto.setLink(s);
        } catch (NoSuchMethodException e) {
            log.error("找不到博客文档的url");
        }
        return ajaxDto;
    }


    @RequestMapping(value = {"/category"}, method = {RequestMethod.GET})
    public ModelAndView categoryList(ModelAndView modelAndView) {
        Page<com.qintingfm.web.jpa.entity.Category> category = categoryService.getCategory(null, null, 100);
        modelAndView.addObject("allCategory", category.toList());
        modelAndView.setViewName("blog/category");
        modelAndView.addObject("site", getSiteSetting());
        return modelAndView;
    }

    @RequestMapping(value = {"/category/{catName}", "/category/{catName}/{page}"})
    public ModelAndView categoryArticleList(ModelAndView modelAndView, @PathVariable(value = "page", required = false) Integer pageIndex, @PathVariable(value = "catName") String catName) {
        ArrayList<String> catNames = new ArrayList<>();
        catNames.add(catName);
        List<Category> category = categoryService.getCategory(catNames);
        Page<Blog> catBlogList = blogServer.getCatBlogList(category, pageIndex, null, 30);
        modelAndView.addObject("catBlog", catBlogList);
        modelAndView.setViewName("blog/categoryArticleList");
        modelAndView.addObject("site", getSiteSetting());
        return modelAndView;
    }
    @RequestMapping(value = {"/uploadImage"},method = {RequestMethod.POST})
    @ResponseBody
    public UploadImagePojo uploadImage(@RequestParam("upload") MultipartFile multipartFile) {
        UploadImagePojo.UploadImagePojoBuilder builder = UploadImagePojo.builder();
        UploadError.UploadErrorBuilder uploadErrorBuilder = UploadError.builder();
        Optional<User> loginUser = getLoginUser();
        if (!loginUser.isPresent()) {
            uploadErrorBuilder.message("用户没有登录");
            builder.error(uploadErrorBuilder.build());
            return builder.build();
        }
        try {
            Map<String,String> returnMap=new HashMap<>(8);
            byte[] bytes = multipartFile.getBytes();
            String s = DigestUtils.md5DigestAsHex(bytes);
            StorageObject put = manager.put(bytes, s);
            String url = put.getUrl();
            returnMap.put("default",url);
            builder.urls(returnMap);
            return builder.build();
        } catch (IOException e) {
            log.error("上传文件出错{},文件读写出错",e.getMessage());
            uploadErrorBuilder.message("上传文件出错"+e.getMessage());
            builder.error(uploadErrorBuilder.build());
        } catch (ManagerException e) {
            log.error("上传文件出错{},上传到远程出错",e.getMessage());
            uploadErrorBuilder.message("上传文件出错，上传到远程出错"+e.getMessage());
            builder.error(uploadErrorBuilder.build());
        }
        return builder.build();
    }
}
