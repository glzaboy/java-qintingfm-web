package com.qintingfm.web.controller.admin;

import com.qintingfm.web.common.AjaxDto;
import com.qintingfm.web.controller.BaseController;
import com.qintingfm.web.service.form.Form;
import com.qintingfm.web.service.FormGenerateService;
import com.qintingfm.web.pojo.CategoryVo;
import com.qintingfm.web.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

/**
 * 分类
 * @author guliuzhong
 */
@Controller
@RequestMapping("/admin/category")
public class Category extends BaseController {
    FormGenerateService formGenerateService;
    CategoryService categoryService;
    @Autowired
    public void setFormGenerateService(FormGenerateService formGenerateService) {
        this.formGenerateService = formGenerateService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    ModelAndView list(ModelAndView modelAndView){
        Page<com.qintingfm.web.jpa.entity.Category> category = categoryService.getCategory(null, null, 100);
        List<com.qintingfm.web.jpa.entity.Category> categories = category.toList();
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("pageIndex", category.getPageable().getPageNumber() + 1);
        modelAndView.addObject("totalPages", category.getTotalPages());
        modelAndView.addObject("total", category.getTotalElements());
        modelAndView.addObject("site", getSiteSetting());
        return modelAndView;
    }
    @GetMapping("/edit/{catId}")
    ModelAndView edit(ModelAndView modelAndView, @PathVariable(value = "catId",required = false) Integer catId){
        modelAndView.addObject("site", getSiteSetting());
        Optional<com.qintingfm.web.jpa.entity.Category> category = categoryService.getCategory(catId);
        com.qintingfm.web.jpa.entity.Category category1 = category.orElse(new com.qintingfm.web.jpa.entity.Category());
        CategoryVo categoryVo=new CategoryVo();
        BeanUtils.copyProperties(category1,categoryVo);
        Form form =  formGenerateService.generalFormData(categoryVo);
        modelAndView.addObject("form", form);
        modelAndView.setViewName("admin/category/edit");
        return modelAndView;
    }
    @PostMapping("/edit")
    @ResponseBody
    AjaxDto edit(CategoryVo categoryVo){
        com.qintingfm.web.jpa.entity.Category category1;
        if(categoryVo.getCatId()!=null){
            Optional<com.qintingfm.web.jpa.entity.Category> category = categoryService.getCategory(categoryVo.getCatId());
            category1=category.orElse(new com.qintingfm.web.jpa.entity.Category());
        }else{
            category1=new com.qintingfm.web.jpa.entity.Category();
        }
        category1.setTitle(categoryVo.getTitle());
        category1.setDescription(categoryVo.getDescription());
        categoryService.save(category1);
        AjaxDto ajaxDto=new AjaxDto();
        ajaxDto.setMessage("保存成功");
        ajaxDto.setAutoJump(1);
        return ajaxDto;
    }
}
