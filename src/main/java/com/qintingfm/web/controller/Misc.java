package com.qintingfm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/misc")
public class Misc {
    @RequestMapping(value = "/changeTheme")
    @ResponseBody
    public Map<String, String> Home()
    {
        Map<String,String> model=new HashMap<>();
        model.put("status","ok");
        return model;
    }
}
