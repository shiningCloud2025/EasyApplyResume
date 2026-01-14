package com.zyh.easyapplyresume.service.impl.ad_monitor;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorAdminDailyVisitNumMapper;
import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorUserDailyVisitNumMapper;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDailyVisitNum;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorUserDailyVisitNum;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDailyVisitTotalNumService;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorUserDailyVisitNumService;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorUserDailyVisitTotalNumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdmonitorUserDailyVisitNumServiceImpl implements AdmonitorUserDailyVisitNumService {

    @Autowired
    private AdmonitorUserDailyVisitNumMapper admonitorUserDailyVisitNumMapper;

    @Autowired
    private AdmonitorUserDailyVisitTotalNumService admonitorUserDailyVisitTotalNumService;

    @Override
    public Integer addAdmonitorUserDailyVisitNum(AdmonitorUserDailyVisitNum admonitorUserDailyVisitNum) {
        try{
            log.info("新增记录开始");
            return admonitorUserDailyVisitNumMapper.insert(admonitorUserDailyVisitNum);
        }catch (Exception e){
            log.info("新增记录失败");
            throw new RuntimeException("新增记录失败");
        }
    }

    @Override
    public Integer calculateAdmonitorUserDailyVisitNum(Date time) {
        try{
            log.info("计算记录开始");
            // 把 Date 转成 LocalDate
            LocalDate localDate = time.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LambdaQueryWrapper<AdmonitorUserDailyVisitNum> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdmonitorUserDailyVisitNum::getUserDailyVisitNumVisitTime,localDate);
            int size = admonitorUserDailyVisitNumMapper.selectList(lambdaQueryWrapper).size();
            log.info("计算记录成功");
            return size;
        }catch (Exception e){
            log.info("计算记录失败");
            throw new RuntimeException("计算记录失败");
        }
    }

    @Override
    public Integer calculateAdmonitorUserDailyVisitNumTotal() {
        try{
            log.info("计算记录开始");
            int totalNum = admonitorUserDailyVisitNumMapper.selectList(null).size();
            log.info("计算记录成功");
            return totalNum;
        }catch (Exception e){
            log.info("计算记录失败");
            throw new RuntimeException("计算记录失败");
        }
    }

    @Override
    public Integer calculateDayIncreaseAdmonitorUserDailyVisitNum() {
        Date yesterday = DateUtil.offsetDay(new Date(), -1);
        LocalDate yesterdayDate = yesterday.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        log.info("开始计算今日访问增长");
        LambdaQueryWrapper<AdmonitorUserDailyVisitNum> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AdmonitorUserDailyVisitNum::getUserDailyVisitNumVisitTime,yesterdayDate);
        int yesterdayNum = admonitorUserDailyVisitNumMapper.selectList(lambdaQueryWrapper).size();

        LocalDate todayDate = new Date().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LambdaQueryWrapper<AdmonitorUserDailyVisitNum> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(AdmonitorUserDailyVisitNum::getUserDailyVisitNumVisitTime,todayDate);
        int todayNum = admonitorUserDailyVisitNumMapper.selectList(lambdaQueryWrapper1).size();

        if (todayNum-yesterdayNum>0){
            log.info("今日访问增长为{}",todayNum-yesterdayNum);
            return todayNum-yesterdayNum;
        }

        return 0;
    }
}
