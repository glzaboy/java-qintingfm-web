package com.qintingfm.web.service;

import com.qintingfm.web.jpa.MiniAppJpa;
import com.qintingfm.web.jpa.entity.Category;
import com.qintingfm.web.jpa.entity.MiniApp;
import com.qintingfm.web.pojo.vo.MiniAppVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.TimeZone;

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

    public Page<MiniApp> getList(Integer pageIndex, Sort sort, Integer pageSize) {
        pageIndex = (pageIndex == null) ? 1 : pageIndex;
        if (sort == null) {
            sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "id"));
        }
        PageRequest request = PageRequest.of(pageIndex - 1, pageSize, sort);
        return miniAppJpa.findAll(request);
    }

    public Optional<MiniApp> getMiniApp(Integer id){
        TimeZone.getDefault().toString();
        return miniAppJpa.findById(id);
    }
    public MiniApp save(MiniApp miniApp){
        return miniAppJpa.save(miniApp);
    }

    public MiniAppVo toVo(MiniApp miniApp) {
        return copyFunction(miniApp,MiniAppVo.class);
    }
    public MiniApp toJpa(MiniAppVo miniAppVo,MiniApp miniApp) {
        return copyFunction(miniAppVo,(item)->{
            BeanUtils.copyProperties(item,miniApp,"id");
            if(miniAppVo.getType()!=null){
                miniApp.setType(miniAppVo.getType()[0]);
            }
            return miniApp;
        });
    }
}
