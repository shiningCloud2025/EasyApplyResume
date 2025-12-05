package com.zyh.easyapplyresume.service.user;

import com.zyh.easyapplyresume.model.pojo.user.UserDeleteResume;
import com.zyh.easyapplyresume.model.pojo.user.UserSaveResume;
import com.zyh.easyapplyresume.model.vo.user.UserDeleteResumeInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserSaveResumeInfoVO;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface UserDeleteResumeService {
    /**
     * 根据用户id查询用户删除的所有简历
     */
    public List<UserDeleteResumeInfoVO> getUserDeleteResumeInfoByUserId(Integer userId);

    /**
     * 根据用户id和简历排序查询用户删除的简历
     */
    public UserDeleteResumeInfoVO getUserDeleteResumeInfoByUserIdAndResumeSortedNum(Integer userId, Integer resumeSortedNum);


    /**
     * 删除用户保存的简历时，调用这个进行添加
     */
    public void addUserDeleteSaveResume(UserSaveResumeInfoVO userSaveResumeInfoVO);

    /**
     * 从用户删除的简历中取出，放回用户简历表
     */
    public void addUserDeleteResumeToUserSaveResume(UserDeleteResumeInfoVO userDeleteResumeInfoVO);

    /**
     * 清空垃圾箱
     */
    public void clearUserAllDeleteResume(Integer userId);


    /**
     * 清理过期的简历定时任务，每天早上4点和晚上12点进行一次清理
     */
    public void clearExpiredResume();

}
