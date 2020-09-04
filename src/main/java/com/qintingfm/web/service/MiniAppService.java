package com.qintingfm.web.service;

import com.qintingfm.web.jpa.MiniAppJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author guliuzhong
 */
@Service
public class MiniAppService extends BaseService{
    MiniAppJpa miniAppJpa;

    @Autowired
    public void setMiniAppJpa(MiniAppJpa miniAppJpa) {
        this.miniAppJpa = miniAppJpa;
    }
}
