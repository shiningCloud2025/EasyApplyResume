package com.zyh.easyapplyresume.service.ad_monitor;

import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDailyVisitNum;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
public interface AdmonitorAdminDailyVisitNumService {

    /**
     * 新增访问记录
     * @param admonitorAdminDailyVisitNum
     * @return
     */
    public Integer addAdmonitorAdminDailyVisitNum(AdmonitorAdminDailyVisitNum admonitorAdminDailyVisitNum);

    /**
     * 计算某一天的访问量
     * @param time
     * @return
     */
    public Integer calculateAdmonitorAdminDailyVisitNum(Date time);
    /**
     * 获取总访问量
     * @return
     */
    public Integer calculateAdmonitorAdminDailyVisitNumTotal();

    /**
     * 获取某天新增访问量
     * @return
     */
    public Integer calculateDayIncreaseAdmonitorAdminDailyVisitNum();

}
