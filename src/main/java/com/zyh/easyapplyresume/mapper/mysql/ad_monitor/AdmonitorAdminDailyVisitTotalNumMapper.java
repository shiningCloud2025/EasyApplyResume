package com.zyh.easyapplyresume.mapper.mysql.ad_monitor;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDailyVisitTotalNum;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface AdmonitorAdminDailyVisitTotalNumMapper extends BaseMapper<AdmonitorAdminDailyVisitTotalNum> {

    // 查询规定时间内的访问量
    public List<Integer> findFromTimeToEndTimeAdmonitorAdminDailyVisitTotalNum(@Param("fromDate") Date fromDate, @Param("endDate") Date endDate);

}
