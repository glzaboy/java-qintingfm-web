package com.qintingfm.web.controller;

import com.qintingfm.web.jpa.BlogJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.util.Optional;

@Controller
@Transactional
@RequestMapping("/blog")
public class Blog {
    @Autowired
    BlogJpa blogJpa;
    @RequestMapping("/view/{postId}")
    public ModelAndView detail(ModelAndView modelAndView, @PathVariable("postId") Integer postId){
        Optional<com.qintingfm.web.jpa.entity.Blog> byId = blogJpa.findById(postId);
        if(byId.isPresent()){
            modelAndView.addObject("blogPost",byId.get());
        }
        modelAndView.setViewName("blog/view");
        return modelAndView;
    }
}
