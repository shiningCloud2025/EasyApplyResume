package com.zyh.easyapplyresume.service.user;

import com.zyh.easyapplyresume.model.pojo.user.UserSaveResume;
import com.zyh.easyapplyresume.model.vo.user.UserSaveResumeInfoVO;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface UserSaveResumeService {
    /**
     * 根据用户id查询用户保存的所有简历
     */
    public List<UserSaveResumeInfoVO> getUserSaveResumeInfoByUserId(Integer userId);

    /**
     * 根据用户id和简历排序查询用户保存的简历
     */
    public UserSaveResumeInfoVO getUserSaveResumeInfoByUserIdAndResumeId(Integer userId, Integer userSaveResumeSortedNum);

    /**
     * 根据用户id和简历排序删除用户保存的简历
     */
    public void deleteUserSaveResumeInfoByUserIdAndResumeId(Integer userId, Integer userSaveResumeSortedNum);

    /**
     * 保存用户保存的简历(非第一次添加)
     */
    public UserSaveResume saveUserSaveResumeInfo(UserSaveResume userSaveResume);

    /**
     * 保存用户的简历(第一次添加)
     */
    public UserSaveResume saveUserSaveResumeInfoFirst(UserSaveResume userSaveResume);

}
