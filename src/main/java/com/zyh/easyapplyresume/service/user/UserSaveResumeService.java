package com.zyh.easyapplyresume.service.user;

import com.zyh.easyapplyresume.model.pojo.user.UserSaveResume;
import com.zyh.easyapplyresume.model.query.user.UserSaveResumeQuery;
import com.zyh.easyapplyresume.model.vo.admin.ResumeTemplateInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserSaveResumeInfoVO;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface UserSaveResumeService {
    /**
     * 根据用户id查询用户保存的所有简历
     */
    public List<UserSaveResumeInfoVO> getUserSaveResumeInfoByUserId(Integer userId, UserSaveResumeQuery userSaveResumeQuery);

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
    public void saveUserSaveResumeInfo(UserSaveResumeInfoVO userSaveResumeInfoVO);

    /**
     * 保存用户的简历(第一次添加，通过简历模版)
     */
    public void saveUserSaveResumeInfoFirst(ResumeTemplateInfoVO resumeTemplateInfoVO, Integer userId,String resumeName);


    /**
     * 根据用户id和简历排序以及简历名称去修改简历名称
     */
    public void updateUserDeleteResumeNameByUserIdAndResumeSortedNumAndResumeName(Integer userId, Integer resumeSortedNum, String resumeName);

}
