package com.zyh.easyapplyresume.service.ad_monitor;

import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDaliyAdminNum;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorUserDaliyUserNum;

import java.util.Date;
import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface AdmonitorUserDaliyUserNumService {
    /**
     * 新增某日的管理员数量
     * @param admonitorUserDaliyUserNum
     * @return
     */
    public Integer addAdmonitorUserDaliyUserNum(AdmonitorUserDaliyUserNum admonitorUserDaliyUserNum);

    /**
     * 查询规定时间内的管理员数量
     * @param fromDate
     * @param endDate
     * @return
     */
    public List<Integer> findFromTimeToEndTimeAdmonitorUserDaliyUserNum(Date fromDate, Date endDate);
}
