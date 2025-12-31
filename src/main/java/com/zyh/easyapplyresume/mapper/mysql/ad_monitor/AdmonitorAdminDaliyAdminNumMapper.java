package com.zyh.easyapplyresume.mapper.mysql.ad_monitor;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDaliyAdminNum;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface AdmonitorAdminDaliyAdminNumMapper extends BaseMapper<AdmonitorAdminDaliyAdminNum> {
    /**
     * 查询规定时间内的管理员数量
     * @param fromDate
     * @param endDate
     * @return
     */
    public List<Integer> findFromTimeToEndTimeAdmonitorAdminDaliyAdminNum(@Param("fromDate") LocalDate fromDate, @Param("endDate") LocalDate endDate);
}
