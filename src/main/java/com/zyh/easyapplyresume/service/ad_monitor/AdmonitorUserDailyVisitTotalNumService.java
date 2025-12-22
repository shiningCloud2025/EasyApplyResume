package com.zyh.easyapplyresume.service.ad_monitor;

import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDailyVisitTotalNum;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorUserDailyVisitTotalNum;

import java.util.Date;
import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface AdmonitorUserDailyVisitTotalNumService {

    /**
     * 新增某日的访问量
     * @param admonitorUserDailyVisitTotalNum
     * @return
     */
    public Integer addAdmonitorUserDailyVisitTotalNum(AdmonitorUserDailyVisitTotalNum admonitorUserDailyVisitTotalNum);

    /**
     * 查询规定时间内的访问量
     * @param fromDate
     * @param endDate
     * @return
     */
    public List<Integer> findFromTimeToEndTimeAdmonitorUserDailyVisitTotalNum(Date fromDate, Date endDate);

}
