package com.zyh.easyapplyresume.task.ad_monitor;

import cn.hutool.core.date.DateUtil;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDailyVisitTotalNum;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDailyVisitNumService;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDailyVisitTotalNumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 每天的凌晨 00:00:10去统计一波访问量、用户量等等
 * 目的: 最及时、减少人流量冲击
 * @author shiningCloud2025
 */
@Slf4j
@Component
public class AdmonitorAdminDailyVisitNumScheduledTask {

    @Autowired
    private AdmonitorAdminDailyVisitNumService admonitorAdminDailyVisitNumService;

    @Autowired
    private AdmonitorAdminDailyVisitTotalNumService admonitorAdminDailyVisitTotalNumService;
    @Scheduled(cron = "${admonitor-scheduled-tasks.admonitor-admin-daily-visit-num-task}")
    public void calculateYesterdayVisitTask(){
        Date yesterday = DateUtil.offsetDay(new Date(), -1);
        log.info("开始执行统计昨日访问量任务");
        Integer visitNum = admonitorAdminDailyVisitNumService.calculateAdmonitorAdminDailyVisitNum(yesterday);
        log.info("昨日访问量统计完成");
        AdmonitorAdminDailyVisitTotalNum admonitorAdminDailyVisitTotalNum = new AdmonitorAdminDailyVisitTotalNum();
        admonitorAdminDailyVisitTotalNum.setAdminDailyVisitTotalNumDate(yesterday);
        admonitorAdminDailyVisitTotalNum.setAdminDailyVisitTotalNumNum(visitNum);
        log.info("开始执行新增昨日访问量任务");
        admonitorAdminDailyVisitTotalNumService.addAdmonitorAdminDailyVisitTotalNum(admonitorAdminDailyVisitTotalNum);
        log.info("新增昨日访问量任务完成");
    }

}
