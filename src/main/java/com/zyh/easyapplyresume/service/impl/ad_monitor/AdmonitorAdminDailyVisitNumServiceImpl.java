package com.zyh.easyapplyresume.service.impl.ad_monitor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorAdminDailyVisitNumMapper;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDailyVisitNum;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDailyVisitNumService;
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
public class AdmonitorAdminDailyVisitNumServiceImpl implements AdmonitorAdminDailyVisitNumService {
    @Autowired
    private AdmonitorAdminDailyVisitNumMapper admonitorAdminDailyVisitNumMapper;

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
            LambdaQueryWrapper<AdmonitorAdminDailyVisitNum> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdmonitorAdminDailyVisitNum::getAdminDailyVisitNumVisitTime,time);
            int size = admonitorAdminDailyVisitNumMapper.selectList(lambdaQueryWrapper).size();
            log.info("计算记录成功");
            return size;
        }catch (Exception e){
            log.info("计算记录失败");
            throw new RuntimeException("计算记录失败");
        }

    }
}
