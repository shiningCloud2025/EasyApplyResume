package com.zyh.easyapplyresume.service.ad_monitor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorAdminAdvertisementForm;
import com.zyh.easyapplyresume.model.query.ad_monitor.AdmonitorAdminAdvertisementQuery;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdminAdvertisementInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdminAdvertisementPageVO;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface AdmonitorAdminAdvertisementService {
    /**
     * 新增管理端广告
      * @param admonitorAdminAdvertisementForm
     */
    public Integer addAdmonitorAdminAdvertisement(AdmonitorAdminAdvertisementForm admonitorAdminAdvertisementForm);

    /**
     * 修改管理端广告
     * @param admonitorAdminAdvertisementForm
     * @return
     */
    public Integer updateAdmonitorAdminAdvertisement(AdmonitorAdminAdvertisementForm admonitorAdminAdvertisementForm);

    /**
     * 删除管理端的广告
     * @param id
     * @return
     */
    public Integer deleteAdmonitorAdminAdvertisement(Integer id);

    /**
     * 查询管理端广告
     * @param id
     * @return
     */
    public AdmonitorAdminAdvertisementInfoVO findAdmonitorAdminAdvertisementById(Integer id);

    /**
     * 分页查询管理端的广告
     * @param pageNum
     * @param pageSize
     * @param admonitorAdminAdvertisementQuery
     * @return
     */
    public Page<AdmonitorAdminAdvertisementPageVO> findAdmonitorAdminAdvertisementByPage(Integer pageNum, Integer pageSize, AdmonitorAdminAdvertisementQuery admonitorAdminAdvertisementQuery);

    /**
     * 查询所有管理端的广告
     * @return
     */
    public List<AdmonitorAdminAdvertisementInfoVO> findAllAdmonitorAdminAdvertisement();

}
