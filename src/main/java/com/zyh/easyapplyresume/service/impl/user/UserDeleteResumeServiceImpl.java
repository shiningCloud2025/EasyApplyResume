package com.zyh.easyapplyresume.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserDeleteResumeMapper;
import com.zyh.easyapplyresume.model.pojo.user.UserDeleteResume;
import com.zyh.easyapplyresume.model.pojo.user.UserSaveResume;
import com.zyh.easyapplyresume.model.vo.user.UserDeleteResumeInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserSaveResumeInfoVO;
import com.zyh.easyapplyresume.service.user.UserDeleteResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shiningCloud2025
 */
@Slf4j
@Service
public class UserDeleteResumeServiceImpl implements UserDeleteResumeService {
    @Autowired
    private UserDeleteResumeMapper userDeleteResumeMapper;
    @Override
    public List<UserDeleteResumeInfoVO> getUserDeleteResumeInfoByUserId(Integer userDeleteResumeId) {
        LambdaQueryWrapper<UserDeleteResume> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserDeleteResume::getUserDeleteResumeUserId, userDeleteResumeId);
        lambdaQueryWrapper.orderByDesc(UserDeleteResume::getUserDeleteResumeSortedNum);
        List<UserDeleteResume> userDeleteResumes = userDeleteResumeMapper.selectList(lambdaQueryWrapper);
        if (userDeleteResumes != null){
            return BeanUtil.copyToList(userDeleteResumes, UserDeleteResumeInfoVO.class);
        }
        return null;
    }

    @Override
    public UserDeleteResumeInfoVO getUserDeleteResumeInfoByUserIdAndResumeSortedNum(Integer userId, Integer resumeSortedNum) {
        LambdaQueryWrapper<UserDeleteResume> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserDeleteResume::getUserDeleteResumeUserId, userId);
        lambdaQueryWrapper.eq(UserDeleteResume::getUserDeleteResumeSortedNum, resumeSortedNum);
        UserDeleteResume userDeleteResume = userDeleteResumeMapper.selectOne(lambdaQueryWrapper);
        if (userDeleteResume != null){
            return BeanUtil.copyProperties(userDeleteResume, UserDeleteResumeInfoVO.class);
        }
        return null;
    }

    @Override
    public void addUserDeleteSaveResume(UserSaveResumeInfoVO userSaveResumeInfoVO) {
        userSaveResumeInfoVO.
    }

    @Override
    public void addUserDeleteResumeToUserSaveResume(UserDeleteResumeInfoVO userDeleteResumeInfoVO) {

    }

    @Override
    public void clearExpiredResume() {

    }
}
