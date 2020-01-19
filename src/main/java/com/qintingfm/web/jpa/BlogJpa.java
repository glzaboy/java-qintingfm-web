package com.qintingfm.web.jpa;

import com.qintingfm.web.jpa.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface BlogJpa extends JpaRepository<Blog,Integer> {
}
