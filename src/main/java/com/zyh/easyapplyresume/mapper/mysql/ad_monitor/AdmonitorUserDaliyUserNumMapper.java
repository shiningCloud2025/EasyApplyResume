package com.zyh.easyapplyresume.mapper.mysql.ad_monitor;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorUserDaliyUserNum;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.Date;
import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface AdmonitorUserDaliyUserNumMapper extends BaseMapper<AdmonitorUserDaliyUserNum> {
    /**
     * 查询规定时间内的用户数量
     * @param fromDate
     * @param endDate
     * @return
     */
    public List<Integer> findFromTimeToEndTimeAdmonitorUserDaliyUserNum(@Param("fromDate") Date fromDate, @Param("endDate") Date endDate);

}
