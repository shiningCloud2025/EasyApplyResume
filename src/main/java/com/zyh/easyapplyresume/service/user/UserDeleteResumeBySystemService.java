package com.zyh.easyapplyresume.service.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.pojo.user.UserDeleteResume;
import com.zyh.easyapplyresume.model.query.user.UserDeleteResumeQuery;
import com.zyh.easyapplyresume.model.vo.user.UserDeleteResumeBySystemInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserDeleteResumeBySystemPageVO;
import com.zyh.easyapplyresume.model.vo.user.UserDeleteResumeInfoVO;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface UserDeleteResumeBySystemService {

    // 用户删除的简历到期，回收到系统删除简历表
    public void addExpiredUserDeleteResume(List<UserDeleteResume> userDeleteResumes );

    // 每三个月清理一次系统的过期简历
    public void  clearExpiredUserDeleteResumeEveryThreeMonth();

    // 根据id查看系统回收的、用户删除的简历
    public UserDeleteResumeBySystemInfoVO getUserDeleteResumeInfoById(Integer userDeleteResumeId);

    // 分页查询系统回收的、用户删除的简历
    public Page<UserDeleteResumeBySystemPageVO> getUserDeleteResumeInfoPage(Integer pageNum, Integer pageSize, UserDeleteResumeQuery userDeleteResumeQuery);



}
