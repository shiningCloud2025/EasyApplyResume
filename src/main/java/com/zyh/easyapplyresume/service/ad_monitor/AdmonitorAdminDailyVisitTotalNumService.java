package com.zyh.easyapplyresume.service.ad_monitor;

import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDailyVisitTotalNum;

import java.util.Date;
import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface AdmonitorAdminDailyVisitTotalNumService {

    /**
     * 新增某日的访问量
     * @param admonitorAdminDailyVisitTotalNum
     * @return
     */
    public Integer addAdmonitorAdminDailyVisitTotalNum(AdmonitorAdminDailyVisitTotalNum admonitorAdminDailyVisitTotalNum);

    /**
     * 查询规定时间内的访问量
     * @param fromDate
     * @param endDate
     * @return
     */
    public List<Integer> findFromTimeToEndTimeAdmonitorAdminDailyVisitTotalNum(Date fromDate, Date endDate);

}
