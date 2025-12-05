package com.zyh.easyapplyresume.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.UserCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.IndustryMapMapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserDeleteResumeMapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserSaveResumeMapper;
import com.zyh.easyapplyresume.model.pojo.admin.IndustryMap;
import com.zyh.easyapplyresume.model.pojo.user.UserDeleteResume;
import com.zyh.easyapplyresume.model.pojo.user.UserSaveResume;
import com.zyh.easyapplyresume.model.vo.user.UserDeleteResumeInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserSaveResumeInfoVO;
import com.zyh.easyapplyresume.service.user.UserDeleteResumeService;
import com.zyh.easyapplyresume.service.user.UserSaveResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author shiningCloud2025
 */
@Slf4j
@Service
public class UserDeleteResumeServiceImpl implements UserDeleteResumeService {
    @Autowired
    private UserDeleteResumeMapper userDeleteResumeMapper;

    @Autowired
    private UserSaveResumeMapper userSaveResumeMapper;

    @Autowired
    private IndustryMapMapper industryMapMapper;

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
        Integer userSaveResumeUserId = userSaveResumeInfoVO.getUserSaveResumeUserId();
        LambdaQueryWrapper<UserDeleteResume> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDeleteResume::getUserDeleteResumeUserId, userSaveResumeUserId);
        queryWrapper.orderByDesc(UserDeleteResume::getUserDeleteResumeSortedNum);
        List<UserDeleteResume> userDeleteResumes = userDeleteResumeMapper.selectList(queryWrapper);
        Integer userDeleteResumeSortedNum = userDeleteResumes.get(0).getUserDeleteResumeSortedNum();
        UserDeleteResume userDeleteResume = new UserDeleteResume();
        userDeleteResume.setUserDeleteResumeId(userSaveResumeInfoVO.getUserSaveResumeId());
        userDeleteResume.setUserDeleteResumeResumeName(userSaveResumeInfoVO.getUserSaveResumeResumeName());
        userDeleteResume.setUserDeleteResumeIndustry(userSaveResumeInfoVO.getUserSaveResumeIndustry());
        userDeleteResume.setUserDeleteResumeResumeReactCode(userSaveResumeInfoVO.getUserSaveResumeResumeReactCode());
        userDeleteResume.setUserDeleteResumeCreatedTime(new Date());
        userDeleteResume.setUserDeleteResumeUpdatedTime(new Date());
        userDeleteResume.setUserDeleteResumeSortedNum(userDeleteResumeSortedNum + 1);
        userDeleteResume.setUserDeleteResumeUserId(userSaveResumeUserId);
        userDeleteResumeMapper.insert(userDeleteResume);
    }

    @Override
    public void addUserDeleteResumeToUserSaveResume(UserDeleteResumeInfoVO userDeleteResumeInfoVO) {
        Integer userId = userDeleteResumeInfoVO.getUserDeleteResumeUserId();
        LambdaQueryWrapper<UserSaveResume> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserSaveResume::getUserSaveResumeUserId, userId);
        queryWrapper.orderByDesc(UserSaveResume::getUserSaveResumeSortedNum);
        List<UserSaveResume> userSaveResumes = userSaveResumeMapper.selectList(queryWrapper);
        Integer userSaveResumeSortedNum = userSaveResumes.get(0).getUserSaveResumeSortedNum();
        if (userSaveResumeSortedNum>=4){
            throw new BusException(UserCodeEnum.USER_SAVE_RESUME_NOT_DAYU_FIVE);
        }
        UserSaveResume userSaveResume = new UserSaveResume();
        userSaveResume.setUserSaveResumeResumeName(userDeleteResumeInfoVO.getUserDeleteResumeResumeName());
        LambdaQueryWrapper<IndustryMap> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(IndustryMap::getIndustryMapIndustryName, userDeleteResumeInfoVO.getUserDeleteResumeIndustryName());
        IndustryMap industryMap = industryMapMapper.selectOne(queryWrapper1);
        userSaveResume.setUserSaveResumeIndustry(industryMap.getIndustryMapIndustryCode());
        userSaveResume.setUserSaveResumeResumeReactCode(userDeleteResumeInfoVO.getUserDeleteResumeResumeReactCode());
        userSaveResume.setUserSaveResumeCreatedTime(new Date());
        userSaveResume.setUserSaveResumeUpdatedTime(new Date());
        userSaveResume.setUserSaveResumeSortedNum(userSaveResumeSortedNum + 1);
        userSaveResume.setUserSaveResumeUserId(userId);
        userSaveResumeMapper.insert(userSaveResume);
    }

    @Override
    public void clearExpiredResume() {

    }
}
