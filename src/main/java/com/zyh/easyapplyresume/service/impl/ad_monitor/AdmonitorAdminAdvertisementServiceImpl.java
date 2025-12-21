package com.zyh.easyapplyresume.service.impl.ad_monitor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorAdminAdvertisementForm;
import com.zyh.easyapplyresume.model.query.ad_monitor.AdmonitorAdminAdvertisementQuery;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdminAdvertisementInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdminAdvertisementPageVO;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminAdvertisementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@Slf4j
public class AdmonitorAdminAdvertisementServiceImpl implements AdmonitorAdminAdvertisementService {
    @Override
    public Integer addAdmonitorAdminAdvertisement(AdmonitorAdminAdvertisementForm admonitorAdminAdvertisementForm) {
        return 0;
    }

    @Override
    public Integer updateAdmonitorAdminAdvertisement(AdmonitorAdminAdvertisementForm admonitorAdminAdvertisementForm) {
        return 0;
    }

    @Override
    public Integer deleteAdmonitorAdminAdvertisement(Integer id) {
        return 0;
    }

    @Override
    public AdmonitorAdminAdvertisementInfoVO findAdmonitorAdminAdvertisementById(Integer id) {
        return null;
    }

    @Override
    public Page<AdmonitorAdminAdvertisementPageVO> findAdmonitorAdminAdvertisementByPage(Integer pageNum, Integer pageSize, AdmonitorAdminAdvertisementQuery admonitorAdminAdvertisementQuery) {
        return null;
    }

    @Override
    public List<AdmonitorAdminAdvertisementInfoVO> findAllAdmonitorAdminAdvertisement() {
        return List.of();
    }
}
