package com.qintingfm.web.service;

import com.qintingfm.web.jpa.BlogJpa;
import com.qintingfm.web.jpa.entity.Blog;
import com.qintingfm.web.util.HtmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * @author guliuzhong
 */
@Service
public class BlogServer {
    BlogJpa blogJpa;

    @Autowired
    public void setBlogJpa(BlogJpa blogJpa) {
        this.blogJpa = blogJpa;
    }

    public Blog saveBlog(Blog blog){
        if(blog.getDateCreated()==null){
            blog.setDateCreated(new Date());
        }
        String cont = blog.getBlogCont().getCont();
        blog.setShotCont(HtmlUtil.delHtmlTags(cont).trim().substring(0,300));
        return blogJpa.save(blog);
    }

    public Page<Blog> getBlogList(Integer catId, Integer pageIndex, Sort sort,Integer pageSize){
        pageIndex = (pageIndex == null) ? 1 : pageIndex;
        if(sort==null){
            sort=Sort.by(new Sort.Order(Sort.Direction.DESC, "postId"));
        }
        PageRequest request = PageRequest.of(pageIndex - 1, pageSize, sort);
        Page<Blog> all = blogJpa.findAll(request);
        return all;
    }

    public Optional<Blog> getBlog(Integer postId){
        return blogJpa.findById(postId);
    }
}
