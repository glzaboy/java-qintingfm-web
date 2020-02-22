package com.qintingfm.web.jpa;

import com.qintingfm.web.jpa.entity.Blog;
import com.qintingfm.web.jpa.entity.BlogComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
/**
 * 博客内容jpa
 *
 * @author guliuzhong
 */
@Component
public interface BlogCommentJpa extends JpaRepository<BlogComment,Integer>{
        /**
         * 根据blog标题查询评论
         *
         * @param blog
         * @param pageable
         * @return
         */
        Page<BlogComment> findByBlog(Blog blog, Pageable pageable);
}
