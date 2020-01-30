package com.qintingfm.web.service;

import com.qintingfm.web.jpa.CategoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author guliuzhong
 */
@Service
public class Category {
    private CategoryJpa categoryJpa;

    @Autowired
    public void setCategoryJpa(CategoryJpa categoryJpa) {
        this.categoryJpa = categoryJpa;
    }

    public Page<com.qintingfm.web.jpa.entity.Category> getAllCategory(int page, int size) {
        PageRequest postId = PageRequest.of(page-1, size, Sort.by(new Sort.Order(Sort.Direction.DESC, "catId")));
        return categoryJpa.findAll(postId);
    }
}
