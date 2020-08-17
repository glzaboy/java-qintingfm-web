package com.qintingfm.web.jpa;

import com.qintingfm.web.jpa.entity.WxUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * 权限角色
 *
 * @author guliuzhong
 */
@Component
public interface WxUploadJpa extends JpaRepository<WxUpload, Long> {
}
