package com.zyh.easyapplyresume.service.impl.ad_monitor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorAdvertisementForm;
import com.zyh.easyapplyresume.model.query.ad_monitor.AdmonitorAdvertisementQuery;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdvertisementInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdvertisementPageVO;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdvertisementService;

import java.util.List;

public class AdmonitorAdvertisementServiceImpl implements AdmonitorAdvertisementService {
    @Override
    public Integer addAdmonitorAdvertisement(AdmonitorAdvertisementForm admonitorAdvertisementForm) {
        return 0;
    }

    @Override
    public Integer updateAdmonitorAdvertisement(AdmonitorAdvertisementForm admonitorAdvertisementForm) {
        return 0;
    }

    @Override
    public Integer deleteAdmonitorAdvertisement(Integer id) {
        return 0;
    }

    @Override
    public AdmonitorAdvertisementInfoVO findAdmonitorAdvertisementById(Integer id) {
        return null;
    }

    @Override
    public Page<AdmonitorAdvertisementPageVO> findAdmonitorAdvertisementByPage(Integer pageNum, Integer pageSize, AdmonitorAdvertisementQuery admonitorAdvertisementQuery) {
        return null;
    }

    @Override
    public List<AdmonitorAdvertisementInfoVO> findAllAdmonitorAdminAdvertisement() {
        return List.of();
    }
}
