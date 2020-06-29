package com.qintingfm.web.controller;

import com.qintingfm.web.jpa.BlogJpa;
import com.qintingfm.web.jpa.entity.Blog;
import com.qintingfm.web.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author guliuzhong
 */
@Controller
@Slf4j
public class IndexController extends BaseController {
    BlogService blogServer;

    @Autowired
    public void setBlogServer(BlogService blogServer) {
        this.blogServer = blogServer;
    }

    @RequestMapping(value = {"/page/{pageIndex}", "/"})
    @Transactional(rollbackFor = Exception.class)
    public ModelAndView index(ModelAndView view, @PathVariable(value = "pageIndex", required = false) Integer pageIndex) {
        view.addObject("title", "钦听知天下");
        Page<Blog> blogList = blogServer.getBlogList(0, pageIndex, null, 10);
        view.addObject("blogList", blogList.toList());
        view.addObject("pageIndex", blogList.getPageable().getPageNumber() + 1);
        view.addObject("totalPages", blogList.getTotalPages());
        view.addObject("total", blogList.getTotalElements());
        view.addObject("site", getSiteSetting());
        view.setViewName("index");
        return view;
    }
}
