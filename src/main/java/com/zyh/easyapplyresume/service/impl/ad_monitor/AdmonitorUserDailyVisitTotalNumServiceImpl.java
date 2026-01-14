package com.zyh.easyapplyresume.service.impl.ad_monitor;

import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorUserDailyVisitTotalNumMapper;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorUserDailyVisitTotalNum;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorUserDailyVisitTotalNumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdmonitorUserDailyVisitTotalNumServiceImpl implements AdmonitorUserDailyVisitTotalNumService {
    @Autowired
    private AdmonitorUserDailyVisitTotalNumMapper admonitorUserDailyVisitTotalNumMapper;
    @Override
    public Integer addAdmonitorUserDailyVisitTotalNum(AdmonitorUserDailyVisitTotalNum admonitorUserDailyVisitTotalNum) {
        try{
            log.info("新增记录开始");
            return admonitorUserDailyVisitTotalNumMapper.insert(admonitorUserDailyVisitTotalNum);
        }catch (Exception e){
            log.info("新增记录失败");
            throw new RuntimeException("新增记录失败");
        }
    }

    @Override
    public List<Integer> findFromTimeToEndTimeAdmonitorUserDailyVisitTotalNum(Date fromDate, Date endDate) {
        try{
            log.info("查询记录开始");
            LocalDate localFromDate = fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localEndDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return admonitorUserDailyVisitTotalNumMapper.findFromTimeToEndTimeAdmonitorUserDailyVisitTotalNum(localFromDate,localEndDate);
        } catch (Exception e){
            log.info("查询记录失败");
            throw new RuntimeException("查询记录失败");
        }
    }
}
