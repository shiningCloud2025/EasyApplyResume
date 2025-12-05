package com.zyh.easyapplyresume.task.user;

import com.zyh.easyapplyresume.service.user.UserDeleteResumeBySystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 存放在系统3个月的就删除-定时任务
 * @author shiningCloud2025
 */
@Slf4j
@Component
public class UserDeleteResumeBySystemScheduledTask {

    @Autowired
    private UserDeleteResumeBySystemService userDeleteResumeBySystemService;

    @Scheduled(cron = "${user-scheduled-tasks.user-delete-resume-by-system-task}")
    public void clearExpiredUserDeleteResumeEveryThreeMonthTask(){
        log.info("开始执行清理过期简历任务");
        try{
            userDeleteResumeBySystemService.clearExpiredUserDeleteResumeEveryThreeMonth();
            log.info("清理过期简历任务执行完成");
        }catch (Exception e){
            log.error("清理过期简历任务执行失败", e);
        }
    }
}
