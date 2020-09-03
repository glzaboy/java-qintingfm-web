package com.qintingfm.web.controller.admin;

import com.qintingfm.web.common.AjaxDto;
import com.qintingfm.web.controller.BaseController;
import com.qintingfm.web.pojo.vo.MiniAppVo;
import com.qintingfm.web.service.FormGenerateService;
import com.qintingfm.web.service.WxUploadService;
import com.qintingfm.web.service.form.Form;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;

/**
 * 小程序类功能上传文件管理
 * @author guliuzhong
 */
@Controller
@RequestMapping("/admin/miniApp")
public class MiniApp extends BaseController {
    FormGenerateService formGenerateService;
    WxUploadService wxUploadService;

    @Autowired
    public void setFormGenerateService(FormGenerateService formGenerateService) {
        this.formGenerateService = formGenerateService;
    }

    @Autowired
    public void setWxUploadService(WxUploadService wxUploadService) {
        this.wxUploadService = wxUploadService;
    }

    @RequestMapping(value = "/app/{appId}", method = {RequestMethod.GET})
    public ModelAndView app(ModelAndView modelAndView, @PathVariable(value = "appId") String name) {
        MiniAppVo miniAppVo=new MiniAppVo();
        miniAppVo.setAppId(Integer.valueOf("134"));
        miniAppVo.setType(new String[]{"1","2","3"});
        Form formBySettingName = formGenerateService.generalFormData(miniAppVo);
        modelAndView.addObject("form1", formBySettingName);
        modelAndView.addObject("site", getSiteSetting());
        modelAndView.addObject("name", name);
        modelAndView.setViewName("admin/app");
        return modelAndView;
    }
    @RequestMapping(value = "/saveapp", method = {RequestMethod.POST})
    @ResponseBody
    public AjaxDto app(MiniAppVo miniAppVo, ModelAndView modelAndView)  throws ConstraintViolationException {
        this.validatePojoAndThrow(miniAppVo);
        AjaxDto ajaxDto=new AjaxDto();
        ajaxDto.setAutoJump(0);
        ajaxDto.setMessage("设置成功");
        ajaxDto.setLink("http://baidu.com");
        return ajaxDto;
    }
}
