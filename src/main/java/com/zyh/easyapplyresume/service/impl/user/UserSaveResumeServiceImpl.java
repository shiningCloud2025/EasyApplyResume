package com.zyh.easyapplyresume.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserSaveResumeMapper;
import com.zyh.easyapplyresume.model.pojo.user.UserSaveResume;
import com.zyh.easyapplyresume.model.vo.user.UserSaveResumeInfoVO;
import com.zyh.easyapplyresume.service.user.UserDeleteResumeService;
import com.zyh.easyapplyresume.service.user.UserSaveResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author shiningCloud2025
 */
@Slf4j
@Service
public class UserSaveResumeServiceImpl implements UserSaveResumeService {
    @Autowired
    private UserSaveResumeMapper userSaveResumeMapper;
    @Autowired
    private UserDeleteResumeService userDeleteResumeService;
    @Override
    public List<UserSaveResumeInfoVO> getUserSaveResumeInfoByUserId(Integer userSaveResumeUserId) {
        LambdaQueryWrapper<UserSaveResume> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserSaveResume::getUserSaveResumeUserId, userSaveResumeUserId);
        lambdaQueryWrapper.orderByDesc(UserSaveResume::getUserSaveResumeSortedNum);
        List<UserSaveResume> userSaveResumeList = userSaveResumeMapper.selectList(lambdaQueryWrapper);
        if (userSaveResumeList != null){
            return BeanUtil.copyToList(userSaveResumeList, UserSaveResumeInfoVO.class);
        }
        return null;
    }

    @Override
    public UserSaveResumeInfoVO getUserSaveResumeInfoByUserIdAndResumeId(Integer userId, Integer userSaveResumeSortedNum) {
        LambdaQueryWrapper<UserSaveResume> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserSaveResume::getUserSaveResumeUserId, userId);
        lambdaQueryWrapper.eq(UserSaveResume::getUserSaveResumeSortedNum, userSaveResumeSortedNum);
        UserSaveResume userSaveResume = userSaveResumeMapper.selectOne(lambdaQueryWrapper);
        if (userSaveResume != null){
            return BeanUtil.copyProperties(userSaveResume, UserSaveResumeInfoVO.class);
        }
        return null;
    }

    @Override
    public void deleteUserSaveResumeInfoByUserIdAndResumeId(Integer userId, Integer userSaveResumeSortedNum) {
        LambdaQueryWrapper<UserSaveResume> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserSaveResume::getUserSaveResumeUserId, userId);
        lambdaQueryWrapper.eq(UserSaveResume::getUserSaveResumeSortedNum, userSaveResumeSortedNum);
        UserSaveResume userSaveResume = userSaveResumeMapper.selectOne(lambdaQueryWrapper);
        UserSaveResumeInfoVO userSaveResumeInfoVO = BeanUtil.copyProperties(userSaveResume, UserSaveResumeInfoVO.class)
        userDeleteResumeService.addUserDeleteSaveResume(userSaveResumeInfoVO);
        userSaveResumeMapper.delete(lambdaQueryWrapper);
        reorderResumeSortedNum(userId, userSaveResumeSortedNum);
    }

    @Override
    public UserSaveResume saveUserSaveResumeInfo(UserSaveResume userSaveResume) {
        return null;
    }


    @Override
    public UserSaveResume saveUserSaveResumeInfoFirst(UserSaveResume userSaveResume) {
        return null;
    }

    private void reorderResumeSortedNum(Integer userId, Integer deletedSortedNum) {
        LambdaQueryWrapper<UserSaveResume> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserSaveResume::getUserSaveResumeUserId, userId);
        queryWrapper.gt(UserSaveResume::getUserSaveResumeSortedNum, deletedSortedNum);
        queryWrapper.orderByAsc(UserSaveResume::getUserSaveResumeSortedNum);
        List<UserSaveResume> resumeList = userSaveResumeMapper.selectList(queryWrapper);
        
        if (resumeList != null && !resumeList.isEmpty()) {
            for (UserSaveResume resume : resumeList) {
                LambdaUpdateWrapper<UserSaveResume> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(UserSaveResume::getUserSaveResumeId, resume.getUserSaveResumeId());
                updateWrapper.eq(UserSaveResume::getUserSaveResumeSortedNum, resume.getUserSaveResumeSortedNum());
                updateWrapper.set(UserSaveResume::getUserSaveResumeSortedNum, resume.getUserSaveResumeSortedNum() - 1);
                userSaveResumeMapper.update(null, updateWrapper);
            }
        }
    }
}
