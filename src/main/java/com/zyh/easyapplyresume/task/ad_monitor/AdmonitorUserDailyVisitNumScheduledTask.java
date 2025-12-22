package com.zyh.easyapplyresume.task.ad_monitor;

import cn.hutool.core.date.DateUtil;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDailyVisitTotalNum;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorUserDailyVisitTotalNum;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDailyVisitNumService;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDailyVisitTotalNumService;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorUserDailyVisitNumService;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorUserDailyVisitTotalNumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 每天的凌晨 00:00:20去统计一波访问量、用户量等等
 * 目的: 最及时、减少人流量冲击
 * @author shiningCloud2025
 */
@Slf4j
@Component
public class AdmonitorUserDailyVisitNumScheduledTask {

    @Autowired
    private AdmonitorUserDailyVisitNumService admonitorUserDailyVisitNumService;

    @Autowired
    private AdmonitorUserDailyVisitTotalNumService admonitorUserDailyVisitTotalNumService;
    @Scheduled(cron = "${admonitor-scheduled-tasks.admonitor-user-daily-visit-num-task}")
    public void calculateYesterdayVisitTask(){
        Date yesterday = DateUtil.offsetDay(new Date(), -1);
        log.info("开始执行统计用户昨日访问量任务");
        Integer visitNum = admonitorUserDailyVisitNumService.calculateAdmonitorUserDailyVisitNum(yesterday);
        log.info("昨日用户访问量统计完成");
        AdmonitorUserDailyVisitTotalNum admonitorUserDailyVisitTotalNum = new AdmonitorUserDailyVisitTotalNum();
        admonitorUserDailyVisitTotalNum.setUserDailyVisitTotalNumDate(yesterday);
        admonitorUserDailyVisitTotalNum.setUserDailyVisitTotalNumNum(visitNum);
        log.info("开始执行用户新增昨日访问量任务");
        admonitorUserDailyVisitTotalNumService.addAdmonitorUserDailyVisitTotalNum(admonitorUserDailyVisitTotalNum);
        log.info("用户新增昨日访问量任务完成");
    }

}
