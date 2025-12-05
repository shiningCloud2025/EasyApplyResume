package com.zyh.easyapplyresume.task.user;

import com.zyh.easyapplyresume.service.user.UserDeleteResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 删除7天的简历自动回收-定时任务
 * @author shiningCloud2025
 */
@Slf4j
@Component
public class UserDeleteResumeScheduledTask {

    @Autowired
    private UserDeleteResumeService userDeleteResumeService;

    @Scheduled(cron = "${user-scheduled-tasks.user-delete-resume-task}")
    public void clearExpiredResumeTask() {
        log.info("开始执行清理过期简历任务");
        try {
            userDeleteResumeService.clearExpiredResume();
            log.info("清理过期简历任务执行完成");
        } catch (Exception e) {
            log.error("清理过期简历任务执行失败", e);
        }

    }

}
