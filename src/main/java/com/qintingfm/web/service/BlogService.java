package com.qintingfm.web.service;

import com.hankcs.hanlp.HanLP;
import com.qintingfm.web.jpa.BlogCommentJpa;
import com.qintingfm.web.jpa.BlogJpa;
import com.qintingfm.web.jpa.entity.Blog;
import com.qintingfm.web.jpa.entity.BlogComment;
import com.qintingfm.web.jpa.entity.BlogCont;
import com.qintingfm.web.jpa.entity.Category;
import com.qintingfm.web.pojo.request.BlogPojo;
import com.qintingfm.web.pojo.vo.settings.SiteSettingVo;
import com.qintingfm.web.service.form.FormOption;
import com.qintingfm.web.spider.BaiduSpider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

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

    UserService userService;

    CategoryService categoryService;

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

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
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
        return blogJpa.findAllByBlogCategoryIn(categories, request);
    }

    public void deleteBlog(Integer postId) {
        Optional<Blog> byId = blogJpa.findById(postId);
        byId.ifPresent(blog -> {
            blog.getComment().forEach(item -> item.setAuthor(null));
            blog.setBlogCategory(null);
            blog.setAuthor(null);
            blogJpa.delete(blog);
        });
    }

    public Optional<Blog> getBlog(Integer postId) {
        if (postId == null) {
            return Optional.empty();
        }
        return blogJpa.findById(postId);
    }

    public Blog save(BlogPojo blogPojo) throws ConstraintViolationException {
        validatePojoAndThrow(blogPojo);
        String contentText = htmlService.filterNone(blogPojo.getCont());
        List<String> keywords = HanLP.extractKeyword(contentText, 15);
        String summary = HanLP.getSummary(contentText, 255);
        if (contentText.length() > shortContLen) {
            contentText = contentText.substring(0, shortContLen);
        }
        Optional<Blog> blogOptional = getBlog(blogPojo.getPostId());
        Blog blog = blogOptional.orElseGet(Blog::new);
        blog.setTitle(blogPojo.getTitle());
        if (blog.getBlogCont() == null) {
            BlogCont blogCont = new BlogCont();
            blogCont.setCont(blogPojo.getCont());
            blog.setBlogCont(blogCont);
        } else {
            blog.getBlogCont().setCont(blogPojo.getCont());
        }
        if (blogPojo.getCreateDate() == null) {
            blog.setDateCreated(new Date());
        } else {
            blog.setDateCreated(blogPojo.getCreateDate());
        }
        blog.setShotCont(contentText);
        blog.setAuthor(userService.getUser(blogPojo.getAuthorId()));
        blog.setBlogCategory(categoryService.getCategory(Arrays.stream(blogPojo.getCatNames()).collect(Collectors.toSet())));
        blog.setState(blogPojo.getState()?"publish":"draft");
        blog.setSummary(summary);
        blog.setKeywords(keywords.stream().collect(Collectors.joining(",")).toString());
        Blog save = blogJpa.save(blog);
        if (save.getState() != null && "publish".equalsIgnoreCase(save.getState())) {
            pushToBaidu(save);
        }
        return save;
    }

    public void pushToBaidu(Blog blog) {
        SiteSettingVo siteSetting = getSiteSetting();
        Collection<String> pushUrl = new ArrayList<>();
        String link=siteSetting.getMainUrl()+"/blog/view/"+blog.getPostId();
        log.info("当前推送地址{}",link);
        pushUrl.add(link);
        baiduSpider.pushUrlToSpider(pushUrl);
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
