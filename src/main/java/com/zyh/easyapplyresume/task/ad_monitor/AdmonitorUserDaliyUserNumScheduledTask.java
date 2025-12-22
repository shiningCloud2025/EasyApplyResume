package com.zyh.easyapplyresume.task.ad_monitor;

import com.zyh.easyapplyresume.mapper.mysql.admin.AdminMapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserMapper;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDaliyAdminNum;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorUserDaliyUserNum;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDaliyAdminNumService;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorUserDaliyUserNumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 每天的晚上 23:59:20去统计一波管理员数量等等
 * 目的: 最及时、减少人流量冲击
 * @author shiningCloud2025
 */
@Slf4j
@Component
public class AdmonitorUserDaliyUserNumScheduledTask {
    @Autowired
    private AdmonitorUserDaliyUserNumService admonitorUserDaliyUserNumService;

    @Autowired
    private UserMapper userMapper;


    @Scheduled(cron = "${admonitor-scheduled-tasks.admonitor-user-daily-user-num-task}")
    public void calculateYesterdayAdminNumTask(){
        log.info("开始执行统计今日管理员数量任务");
        int num = userMapper.selectList(null).size();
        AdmonitorUserDaliyUserNum admonitorUserDaliyUserNum = new AdmonitorUserDaliyUserNum();
        admonitorUserDaliyUserNum.setUserDaliyUserNumDate(new Date());
        admonitorUserDaliyUserNum.setUserDaliyUserNumNum(num);
        admonitorUserDaliyUserNumService.addAdmonitorUserDaliyUserNum(admonitorUserDaliyUserNum);
        log.info("今日管理员数量统计完成");
    }
}
