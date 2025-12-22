package com.zyh.easyapplyresume.service.ad_monitor;

import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDailyVisitNum;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorUserDailyVisitNum;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
public interface AdmonitorUserDailyVisitNumService {

    /**
     * 新增访问记录
     * @param admonitorUserDailyVisitNum
     * @return
     */
    public Integer addAdmonitorUserDailyVisitNum(AdmonitorUserDailyVisitNum admonitorUserDailyVisitNum);

    /**
     * 计算某一天的访问量
     * @param time
     * @return
     */
    public Integer calculateAdmonitorUserDailyVisitNum(Date time);
    /**
     * 获取总访问量
     * @return
     */
    public Integer calculateAdmonitorUserDailyVisitNumTotal();

    /**
     * 获取某天新增访问量
     * @return
     */
    public Integer calculateDayIncreaseAdmonitorUserDailyVisitNum();

}
