package com.qintingfm.web.service;

import com.qintingfm.web.jpa.CategoryJpa;
import com.qintingfm.web.jpa.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类
 * @author guliuzhong
 */
@Service
public class CategoryService {
    CategoryJpa categoryJpa;
    @Autowired
    public void setCategoryJpa(CategoryJpa categoryJpa) {
        this.categoryJpa = categoryJpa;
    }

    public Page<Category> getCategory( Integer pageIndex, Sort sort, Integer pageSize){
        pageIndex = (pageIndex == null) ? 1 : pageIndex;
        if(sort==null){
            sort=Sort.by(new Sort.Order(Sort.Direction.DESC, "catId"));
        }
        PageRequest request = PageRequest.of(pageIndex - 1, pageSize, sort);
        return categoryJpa.findAll(request);
    }
}
