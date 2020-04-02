package com.qintingfm.web.jpa;

import com.qintingfm.web.jpa.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 博客分类
 *
 * @author guliuzhong
 */
@Component
public interface CategoryJpa extends JpaRepository<Category, String> {
    /**
     * 查找指定类型的分类
     *
     * @param titleList
     * @return
     */
    List<Category> findAllByTitleIn(List<String> titleList);
}
