package com.qintingfm.web.controller.admin;

import com.qintingfm.web.common.AjaxDto;
import com.qintingfm.web.controller.BaseController;
import com.qintingfm.web.controller.UserController;
import com.qintingfm.web.jpa.entity.Category;
import com.qintingfm.web.jpa.entity.MiniApp;
import com.qintingfm.web.pojo.vo.MiniAppVo;
import com.qintingfm.web.service.FormGenerateService;
import com.qintingfm.web.service.MiniAppService;
import com.qintingfm.web.service.WxUploadService;
import com.qintingfm.web.service.form.Form;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * 小程序类功能上传文件管理
 * @author guliuzhong
 */
@Controller
@RequestMapping("/admin/miniApp")
@Slf4j
public class MiniAppController extends BaseController {
    FormGenerateService formGenerateService;
    WxUploadService wxUploadService;

    MiniAppService miniAppService;

    @Autowired
    public void setFormGenerateService(FormGenerateService formGenerateService) {
        this.formGenerateService = formGenerateService;
    }

    @Autowired
    public void setWxUploadService(WxUploadService wxUploadService) {
        this.wxUploadService = wxUploadService;
    }

    @Autowired
    public void setMiniAppService(MiniAppService miniAppService) {
        this.miniAppService = miniAppService;
    }

    @RequestMapping(value = "/list/{page}", method = {RequestMethod.GET})
    public ModelAndView list(ModelAndView modelAndView, @PathVariable(value = "page",required = false) Integer page) {
        Page<MiniApp> list = miniAppService.getList(page, null, 10);
        modelAndView.addObject("items", list.toList());
        modelAndView.addObject("pageIndex", list.getPageable().getPageNumber() + 1);
        modelAndView.addObject("totalPages", list.getTotalPages());
        modelAndView.addObject("total", list.getTotalElements());
        modelAndView.setViewName("admin/miniapp/list");
        modelAndView.addObject("site", getSiteSetting());
        return modelAndView;
    }
    @RequestMapping(value = "/edit/{appId}", method = {RequestMethod.GET})
    public ModelAndView app(ModelAndView modelAndView, @PathVariable(value = "appId",required = false) Integer appId) {
        Optional<MiniApp> miniAppOptional = miniAppService.getMiniApp(appId);
        MiniApp miniApp = miniAppOptional.orElse(new MiniApp());
        MiniAppVo miniAppVo = miniAppService.toVo(miniApp);
        Form formBySettingName = formGenerateService.generalFormData(miniAppVo);
        modelAndView.addObject("form1", formBySettingName);
        modelAndView.addObject("site", getSiteSetting());
        modelAndView.setViewName("admin/miniapp/app");
        return modelAndView;
    }
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public AjaxDto app(MiniAppVo miniAppVo, ModelAndView modelAndView)  throws ConstraintViolationException {
        this.validatePojoAndThrow(miniAppVo);
        MiniApp miniApp1;
        if(miniAppVo.getId()!=null){
            Optional<MiniApp> miniApp = miniAppService.getMiniApp(miniAppVo.getId());
            miniApp1 = miniApp.orElse(new MiniApp());
        }else{
            miniApp1=new MiniApp();
        }

        MiniApp miniApp = miniAppService.toJpa(miniAppVo, miniApp1);
        MiniApp save = miniAppService.save(miniApp);
        AjaxDto ajaxDto=new AjaxDto();
        ajaxDto.setAutoJump(0);
        Method loginPage = null;
        try {
            loginPage = UserController.class.getMethod("list", ModelAndView.class,Integer.class);
            ajaxDto.setLink(MvcUriComponentsBuilder.fromMethod(MiniAppController.class, loginPage, modelAndView,Integer.valueOf(1)).build().toUriString());
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage());
        }
        ajaxDto.setMessage("设置成功"+save.getAppId());
        return ajaxDto;
    }
}
