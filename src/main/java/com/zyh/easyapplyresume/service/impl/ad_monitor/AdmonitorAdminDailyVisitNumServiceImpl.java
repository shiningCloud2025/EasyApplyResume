package com.zyh.easyapplyresume.service.impl.ad_monitor;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorAdminDailyVisitNumMapper;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDailyVisitNum;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDailyVisitNumService;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDailyVisitTotalNumService;
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
@Slf4j
public class AdmonitorAdminDailyVisitNumServiceImpl implements AdmonitorAdminDailyVisitNumService {
    @Autowired
    private AdmonitorAdminDailyVisitNumMapper admonitorAdminDailyVisitNumMapper;

    @Autowired
    private AdmonitorAdminDailyVisitTotalNumService admonitorAdminDailyVisitTotalNumService;

    @Override
    public Integer addAdmonitorAdminDailyVisitNum(AdmonitorAdminDailyVisitNum admonitorAdminDailyVisitNum) {
        try{
            log.info("新增记录开始");
            return admonitorAdminDailyVisitNumMapper.insert(admonitorAdminDailyVisitNum);
        }catch (Exception e){
            log.info("新增记录失败");
            throw new RuntimeException("新增记录失败");
        }
    }

    @Override
    public Integer calculateAdmonitorAdminDailyVisitNum(Date time) {
        try{
            log.info("计算记录开始");
            // 把 Date 转成 LocalDate
            LocalDate localDate = time.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LambdaQueryWrapper<AdmonitorAdminDailyVisitNum> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdmonitorAdminDailyVisitNum::getAdminDailyVisitNumVisitTime,localDate);
            int size = admonitorAdminDailyVisitNumMapper.selectList(lambdaQueryWrapper).size();
            log.info("计算记录成功");
            return size;
        }catch (Exception e){
            log.info("计算记录失败");
            throw new RuntimeException("计算记录失败");
        }

    }

    @Override
    public Integer calculateAdmonitorAdminDailyVisitNumTotal() {
        try{
            log.info("计算记录开始");
            int totalNum = admonitorAdminDailyVisitNumMapper.selectList(null).size();
            log.info("计算记录成功");
            return totalNum;
        }catch (Exception e){
            log.info("计算记录失败");
            throw new RuntimeException("计算记录失败");
        }
    }

    @Override
    public Integer calculateDayIncreaseAdmonitorAdminDailyVisitNum() {
        Date yesterday = DateUtil.offsetDay(new Date(), -1);
        log.info("开始计算今日访问增长");
        LambdaQueryWrapper<AdmonitorAdminDailyVisitNum> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AdmonitorAdminDailyVisitNum::getAdminDailyVisitNumVisitTime,yesterday);
        int yesterdayNum = admonitorAdminDailyVisitNumMapper.selectList(lambdaQueryWrapper).size();

        LambdaQueryWrapper<AdmonitorAdminDailyVisitNum> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(AdmonitorAdminDailyVisitNum::getAdminDailyVisitNumVisitTime,new Date());
        int todayNum = admonitorAdminDailyVisitNumMapper.selectList(lambdaQueryWrapper1).size();

        if (todayNum-yesterdayNum>0){
            log.info("今日访问增长为{}",todayNum-yesterdayNum);
            return todayNum-yesterdayNum;
        }

        return 0;
    }


}
