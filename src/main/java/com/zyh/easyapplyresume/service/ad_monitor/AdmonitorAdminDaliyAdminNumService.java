package com.zyh.easyapplyresume.service.ad_monitor;

import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDaliyAdminNum;

import java.util.Date;
import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface AdmonitorAdminDaliyAdminNumService {
    /**
     * 新增某日的管理员数量
     * @param admonitorAdminDaliyAdminNum
     * @return
     */
    public Integer addAdmonitorAdminDaliyAdminNum(AdmonitorAdminDaliyAdminNum admonitorAdminDaliyAdminNum);

    /**
     * 查询规定时间内的管理员数量
     * @param fromDate
     * @param endDate
     * @return
     */
    public List<Integer> findFromTimeToEndTimeAdmonitorAdminDaliyAdminNum(Date fromDate, Date endDate);
}
