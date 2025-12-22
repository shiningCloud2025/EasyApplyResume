package com.zyh.easyapplyresume.task.ad_monitor;

import com.zyh.easyapplyresume.mapper.mysql.admin.AdminMapper;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDaliyAdminNum;
import com.zyh.easyapplyresume.model.pojo.admin.Admin;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDaliyAdminNumService;
import com.zyh.easyapplyresume.service.admin.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 每天的晚上 23:59:30去统计一波管理员数量等等
 * 目的: 最及时、减少人流量冲击
 * @author shiningCloud2025
 */
@Slf4j
@Component
public class AdmonitorAdminDaliyAdminNumScheduledTask {
    @Autowired
    private AdmonitorAdminDaliyAdminNumService admonitorAdminDaliyAdminNumService;

    @Autowired
    private AdminMapper adminMapper;


    @Scheduled(cron = "${admonitor-scheduled-tasks.admonitor-admin-daily-admin-num-task}")
    public void calculateYesterdayAdminNumTask(){
        log.info("开始执行统计今日管理员数量任务");
        int num = adminMapper.selectList(null).size();
        AdmonitorAdminDaliyAdminNum admonitorAdminDaliyAdminNum = new AdmonitorAdminDaliyAdminNum();
        admonitorAdminDaliyAdminNum.setAdminDaliyAdminNumDate(new Date());
        admonitorAdminDaliyAdminNum.setAdminDaliyAdminNumNum(num);
        admonitorAdminDaliyAdminNumService.addAdmonitorAdminDaliyAdminNum(admonitorAdminDaliyAdminNum);
        log.info("今日管理员数量统计完成");
    }
}
