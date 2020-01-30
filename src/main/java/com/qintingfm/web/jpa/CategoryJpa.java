package com.qintingfm.web.jpa;

import com.qintingfm.web.jpa.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author guliuzhong
 */
@Component
public interface CategoryJpa  extends JpaRepository<Category,String> {
}
