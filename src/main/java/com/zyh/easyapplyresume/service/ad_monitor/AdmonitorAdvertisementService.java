package com.zyh.easyapplyresume.service.ad_monitor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorAdminAdvertisementForm;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorAdvertisementForm;
import com.zyh.easyapplyresume.model.query.ad_monitor.AdmonitorAdminAdvertisementQuery;
import com.zyh.easyapplyresume.model.query.ad_monitor.AdmonitorAdvertisementQuery;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdminAdvertisementInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdminAdvertisementPageVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdvertisementInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdvertisementPageVO;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface AdmonitorAdvertisementService {
    /**
     * 新增监测端广告
     * @param admonitorAdvertisementForm
     */
    public Integer addAdmonitorAdvertisement(AdmonitorAdvertisementForm admonitorAdvertisementForm);

    /**
     * 修改监测端广告
     * @param admonitorAdvertisementForm
     * @return
     */
    public Integer updateAdmonitorAdvertisement(AdmonitorAdvertisementForm admonitorAdvertisementForm);

    /**
     * 删除监测端的广告
     * @param id
     * @return
     */
    public Integer deleteAdmonitorAdvertisement(Integer id);

    /**
     * 查询监测端广告
     * @param id
     * @return
     */
    public AdmonitorAdvertisementInfoVO findAdmonitorAdvertisementById(Integer id);

    /**
     * 分页查询管监测端的广告
     * @param pageNum
     * @param pageSize
     * @param admonitorAdvertisementQuery
     * @return
     */
    public Page<AdmonitorAdvertisementPageVO> findAdmonitorAdvertisementByPage(Integer pageNum, Integer pageSize, AdmonitorAdvertisementQuery admonitorAdvertisementQuery);

    /**
     * 查询所有监测端的广告
     * @return
     */
    public List<AdmonitorAdvertisementInfoVO> findAllAdmonitorAdminAdvertisement();


}
