package com.qintingfm.web.controller.admin;

import com.qintingfm.web.jpa.BlogJpa;
import com.qintingfm.web.service.CategoryService;
import com.qintingfm.web.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author guliuzhong
 */
@Controller
@RequestMapping("/admin/blog")
public class AdminBlogController {
    BlogJpa blogJpa;
    CategoryService category;

    SettingService settingService;

    @Autowired
    public void setSettingService(SettingService settingService) {
        this.settingService = settingService;
    }

    @Autowired
    public void setBlogJpa(BlogJpa blogJpa) {
        this.blogJpa = blogJpa;
    }

    @Autowired
    public void setCategory(CategoryService category) {
        this.category = category;
    }

    @RequestMapping("/setting/pushBaidu")
    public ModelAndView settingPushBaidu(ModelAndView modelAndView, @PathVariable(value = "postId", required = false) Integer postId) {
        return modelAndView;
    }
}
