package com.zyh.easyapplyresume.service.impl.ad_monitor;

import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorAdminDailyVisitTotalNumMapper;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDailyVisitTotalNum;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDailyVisitTotalNumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@Slf4j
public class AdmonitorAdminDailyVisitTotalNumServiceImpl implements AdmonitorAdminDailyVisitTotalNumService {
    @Autowired
    private AdmonitorAdminDailyVisitTotalNumMapper admonitorAdminDailyVisitTotalNumMapper;
    @Override
    public Integer addAdmonitorAdminDailyVisitTotalNum(AdmonitorAdminDailyVisitTotalNum admonitorAdminDailyVisitTotalNum) {
        try{
            log.info("新增记录开始");
            return admonitorAdminDailyVisitTotalNumMapper.insert(admonitorAdminDailyVisitTotalNum);
        }catch (Exception e){
            log.info("新增记录失败");
            throw new RuntimeException("新增记录失败");
        }
    }

    @Override
    public Integer findFromTimeToEndTimeAdmonitorAdminDailyVisitTotalNum(Date fromDate, Date endDate) {
        try{
            log.info("查询记录开始");
            return admonitorAdminDailyVisitTotalNumMapper.findFromTimeToEndTimeAdmonitorAdminDailyVisitTotalNum(fromDate,endDate);
        } catch (Exception e){
            log.info("查询记录失败");
            throw new RuntimeException("查询记录失败");
        }
    }
}
