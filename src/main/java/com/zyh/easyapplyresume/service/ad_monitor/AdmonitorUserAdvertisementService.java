package com.zyh.easyapplyresume.service.ad_monitor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorAdminAdvertisementForm;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorUserAdvertisementForm;
import com.zyh.easyapplyresume.model.query.ad_monitor.AdmonitorAdminAdvertisementQuery;
import com.zyh.easyapplyresume.model.query.ad_monitor.AdmonitorUserAdvertisementQuery;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdminAdvertisementInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdminAdvertisementPageVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorUserAdvertisementInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorUserAdvertisementPageVO;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface AdmonitorUserAdvertisementService {
    /**
     * 新增用户端广告
     * @param admonitorUserAdvertisementForm
     */
    public Integer addAdmonitorUserAdvertisement(AdmonitorUserAdvertisementForm admonitorUserAdvertisementForm);

    /**
     * 修改用户端广告
     * @param admonitorUserAdvertisementForm
     * @return
     */
    public Integer updateAdmonitorUserAdvertisement(AdmonitorUserAdvertisementForm admonitorUserAdvertisementForm);

    /**
     * 删除用户端的广告
     * @param id
     * @return
     */
    public Integer deleteAdmonitorUserAdvertisement(Integer id);

    /**
     * 查询用户端广告
     * @param id
     * @return
     */
    public AdmonitorUserAdvertisementInfoVO findAdmonitorUserAdvertisementById(Integer id);

    /**
     * 分页查询用户端的广告
     * @param pageNum
     * @param pageSize
     * @param admonitorUserAdvertisementQuery
     * @return
     */
    public Page<AdmonitorUserAdvertisementPageVO> findAdmonitorUserAdvertisementByPage(Integer pageNum, Integer pageSize, AdmonitorUserAdvertisementQuery admonitorUserAdvertisementQuery);

    /**
     * 查询所有用户端的广告
     * @return
     */
    public List<AdmonitorUserAdvertisementInfoVO> findAllAdmonitorUserAdvertisement();

}
