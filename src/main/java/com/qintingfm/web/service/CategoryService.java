package com.qintingfm.web.service;

import com.qintingfm.web.jpa.CategoryJpa;
import com.qintingfm.web.jpa.entity.Category;
import com.qintingfm.web.service.form.FormOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类
 *
 * @author guliuzhong
 */
@Service
public class CategoryService {
    CategoryJpa categoryJpa;

    @Autowired
    public void setCategoryJpa(CategoryJpa categoryJpa) {
        this.categoryJpa = categoryJpa;
    }

    public Page<Category> getCategory(Integer pageIndex, Sort sort, Integer pageSize) {
        pageIndex = (pageIndex == null) ? 1 : pageIndex;
        if (sort == null) {
            sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "catId"));
        }
        PageRequest request = PageRequest.of(pageIndex - 1, pageSize, sort);
        return categoryJpa.findAll(request);
    }

    public Page<com.qintingfm.web.jpa.entity.Category> getAllCategory(int page, int size) {
        PageRequest postId = PageRequest.of(page - 1, size, Sort.by(new Sort.Order(Sort.Direction.DESC, "catId")));
        return categoryJpa.findAll(postId);
    }
    public Set<FormOption> getAllCategory() {
        PageRequest postId = PageRequest.of(0, 10000, Sort.by(new Sort.Order(Sort.Direction.DESC, "catId")));
        Page<Category> all = categoryJpa.findAll(postId);
        Set<FormOption> collect = all.stream().map(item -> {
            FormOption.FormOptionBuilder builder = FormOption.builder();
            return builder.id(item.title).text(item.title).build();
        }).collect(Collectors.toSet());
        return collect;
    }

    public List<Category> getCategory(Collection<String> categoryNameList) {
        return categoryJpa.findAllByTitleIn(new ArrayList<>(categoryNameList));
    }
    public Optional<Category> getCategory(Integer catId) {
        return categoryJpa.findById(catId);
    }
    public Category save(Category category) {
        return categoryJpa.save(category);
    }
}
