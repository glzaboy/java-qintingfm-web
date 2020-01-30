package com.qintingfm.web.controller;

import com.qintingfm.web.jpa.BlogJpa;
import com.qintingfm.web.jpa.entity.Blog;
import com.qintingfm.web.jpa.entity.BlogCont;
import com.qintingfm.web.util.HtmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author guliuzhong
 */
@Controller
@Slf4j
public class IndexController {
    BlogJpa blogJpa;

    @Autowired
    public void setBlogJpa(BlogJpa blogJpa) {
        this.blogJpa = blogJpa;
    }

    @RequestMapping(value = {"/page/{pageIndex}", "/"})
    public ModelAndView index(ModelAndView view, @PathVariable(value = "pageIndex", required = false) Integer pageIndex) {
        pageIndex = pageIndex == null ? 1 : pageIndex;
        view.addObject("title", "钦听知天下");
        PageRequest postId = PageRequest.of(pageIndex - 1, 10, Sort.by(new Sort.Order(Sort.Direction.DESC, "postId")));
        Page<Blog> all = blogJpa.findAll(postId);
        view.addObject("blogList", all.map(item->{
            BlogCont blogCont = item.getBlogCont();
            blogCont.setCont(HtmlUtil.delHtmlTags(blogCont.getCont()));
            item.setBlogCont(blogCont);
            return item;
        }).toList());
        view.addObject("pageIndex", all.getPageable().getPageNumber() + 1);
        view.addObject("totalPages", all.getTotalPages());
        view.addObject("total", all.getTotalElements());
        view.setViewName("index");
        return view;
    }
}
