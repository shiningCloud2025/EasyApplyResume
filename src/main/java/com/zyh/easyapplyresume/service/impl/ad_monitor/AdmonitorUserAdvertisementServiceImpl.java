package com.zyh.easyapplyresume.service.impl.ad_monitor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorUserAdvertisementForm;
import com.zyh.easyapplyresume.model.query.ad_monitor.AdmonitorUserAdvertisementQuery;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorUserAdvertisementInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorUserAdvertisementPageVO;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorUserAdvertisementService;

import java.util.List;

public class AdmonitorUserAdvertisementServiceImpl implements AdmonitorUserAdvertisementService {
    @Override
    public Integer addAdmonitorUserAdvertisement(AdmonitorUserAdvertisementForm admonitorUserAdvertisementForm) {
        return 0;
    }

    @Override
    public Integer updateAdmonitorUserAdvertisement(AdmonitorUserAdvertisementForm admonitorUserAdvertisementForm) {
        return 0;
    }

    @Override
    public Integer deleteAdmonitorUserAdvertisement(Integer id) {
        return 0;
    }

    @Override
    public AdmonitorUserAdvertisementInfoVO findAdmonitorUserAdvertisementById(Integer id) {
        return null;
    }

    @Override
    public Page<AdmonitorUserAdvertisementPageVO> findAdmonitorUserAdvertisementByPage(Integer pageNum, Integer pageSize, AdmonitorUserAdvertisementQuery admonitorUserAdvertisementQuery) {
        return null;
    }

    @Override
    public List<AdmonitorUserAdvertisementInfoVO> findAllAdmonitorUserAdvertisement() {
        return List.of();
    }
}
