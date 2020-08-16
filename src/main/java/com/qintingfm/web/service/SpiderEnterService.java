package com.qintingfm.web.service;

import com.qintingfm.web.jpa.SpiderEnterJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpiderEnterService extends BaseService{
    SpiderEnterJpa spiderEnterJpa;

    @Autowired
    public void setSpiderEnterJpa(SpiderEnterJpa spiderEnterJpa) {
        this.spiderEnterJpa = spiderEnterJpa;
    }
}
