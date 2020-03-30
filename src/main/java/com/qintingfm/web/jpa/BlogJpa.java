package com.qintingfm.web.jpa;

import com.qintingfm.web.jpa.entity.Blog;
import com.qintingfm.web.jpa.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 博客内容jpa
 * @author guliuzhong
 */
@Component
public interface BlogJpa extends JpaRepository<Blog,Integer> {
    /**
     * 根据分类列出文章
     * @param categories 分类
     * @param pageable 分页
     * @return
     */
    Page<Blog> findAllByBlogCategoryIn(List<Category> categories, Pageable pageable);
}
