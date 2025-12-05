package com.zyh.easyapplyresume.service.user;

import com.zyh.easyapplyresume.model.pojo.user.UserDeleteResume;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface UserDeleteResumeBySystemService {

    // 用户删除的简历到期，会受到系统删除简历表
    public void addExpiredUserDeleteResume(List<UserDeleteResume> userDeleteResumes );

    // 每三个月清理一次系统的过期简历
    public void  clearExpiredUserDeleteResumeEveryThreeMonth();

}
