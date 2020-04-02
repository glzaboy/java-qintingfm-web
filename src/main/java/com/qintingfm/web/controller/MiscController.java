package com.qintingfm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author guliuzhong
 */
@RestController
@RequestMapping("/misc")
public class MiscController {
    @RequestMapping(value = "/changeTheme")
    @ResponseBody
    public Map<String, String> changeTheme() {
        Map<String, String> model = new HashMap<>(8);
        model.put("status", "ok");
        return model;
    }
}
