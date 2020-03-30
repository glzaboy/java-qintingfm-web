package com.qintingfm.web.service;

import com.qintingfm.web.controller.BlogController;
import com.qintingfm.web.jpa.BlogCommentJpa;
import com.qintingfm.web.jpa.BlogJpa;
import com.qintingfm.web.jpa.entity.Blog;
import com.qintingfm.web.jpa.entity.BlogComment;
import com.qintingfm.web.jpa.entity.Category;
import com.qintingfm.web.spider.BaiduSpider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author guliuzhong
 */
@Service
@Slf4j
public class BlogService extends BaseService {
    final int shortContLen = 100;
    BlogJpa blogJpa;

    BaiduSpider baiduSpider;

    BlogCommentJpa blogCommentJpa;
    HtmlService htmlService;

    @Autowired
    public void setBaiduSpider(BaiduSpider baiduSpider) {
        this.baiduSpider = baiduSpider;
    }

    @Autowired
    public void setBlogJpa(BlogJpa blogJpa) {
        this.blogJpa = blogJpa;
    }

    @Autowired
    public void setBlogCommentJpa(BlogCommentJpa blogCommentJpa) {
        this.blogCommentJpa = blogCommentJpa;
    }

    @Autowired
    public void setHtmlService(HtmlService htmlService) {
        this.htmlService = htmlService;
    }


    public Page<Blog> getBlogList(Integer catId, Integer pageIndex, Sort sort, Integer pageSize) {
        pageIndex = (pageIndex == null) ? 1 : pageIndex;
        if (sort == null) {
            sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "postId"));
        }
        PageRequest request = PageRequest.of(pageIndex - 1, pageSize, sort);
        return blogJpa.findAll(request);
    }

    public Page<Blog> getCatBlogList(List<Category> categories, Integer pageIndex, Sort sort, Integer pageSize) {
        pageIndex = (pageIndex == null) ? 1 : pageIndex;
        if (sort == null) {
            sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "postId"));
        }
        PageRequest request = PageRequest.of(pageIndex - 1, pageSize, sort);
        return blogJpa.findAllByBlogCategoryIn(categories,request);
    }

    public void deleteBlog(Integer postId) {
        Optional<Blog> byId = blogJpa.findById(postId);
        byId.ifPresent(blog -> blogJpa.delete(blog));
    }
    public Optional<Blog> getBlog(Integer postId) {
        return blogJpa.findById(postId);
    }

    public Blog save(Blog blog) {
        String contentText = htmlService.filterNone(blog.getBlogCont().getCont());
        if (contentText.length() > shortContLen) {
            blog.setShotCont(contentText.substring(0, shortContLen));
        } else {
            blog.setShotCont(contentText);
        }
        if (blog.getDateCreated() == null) {
            blog.setDateCreated(new Date());
        }
        Collection<String> pushUrl = new ArrayList<>();
        Blog save = blogJpa.save(blog);
        try {
            Method detail = BlogController.class.getDeclaredMethod("detail", ModelAndView.class, Integer.class, Integer.class);
            String s = MvcUriComponentsBuilder.fromMethod(BlogController.class, detail, null, Integer.valueOf(save.getPostId()), null).build().toString();
            pushUrl.add(s);
            baiduSpider.pushUrlToSpider(pushUrl);
        } catch (NoSuchMethodException e) {
            log.error("找不到博客文档的url");
        }
        return save;
    }

    public Page<BlogComment> getBlogComment(Blog blog, Integer pageIndex, Sort sort, Integer pageSize) {
        pageIndex = (pageIndex == null) ? 1 : pageIndex;
        if (sort == null) {
            sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "id"));
        }
        PageRequest request = PageRequest.of(pageIndex - 1, pageSize, sort);
        return blogCommentJpa.findByBlog(blog, request);
    }
    public BlogComment saveComment(BlogComment blogComment) {
        String contentText = htmlService.filterSimpleText(blogComment.getCont());
        blogComment.setCont(contentText);
        if (blogComment.getCreateDate() == null) {
            blogComment.setCreateDate(new Date());
        }
        return blogCommentJpa.save(blogComment);
    }
}
