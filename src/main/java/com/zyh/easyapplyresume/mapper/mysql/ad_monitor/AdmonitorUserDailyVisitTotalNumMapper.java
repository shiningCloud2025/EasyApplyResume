package com.zyh.easyapplyresume.mapper.mysql.ad_monitor;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorUserDailyVisitTotalNum;
import io.lettuce.core.dynamic.annotation.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface AdmonitorUserDailyVisitTotalNumMapper extends BaseMapper<AdmonitorUserDailyVisitTotalNum> {
    // 查询规定时间内的访问量
    public List<Integer> findFromTimeToEndTimeAdmonitorUserDailyVisitTotalNum(@Param("fromDate") LocalDate fromDate, @Param("endDate") LocalDate endDate);
}
