package com.zyh.easyapplyresume.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.UserCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.IndustryMapMapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserDeleteResumeMapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserSaveResumeMapper;
import com.zyh.easyapplyresume.model.pojo.admin.IndustryMap;
import com.zyh.easyapplyresume.model.pojo.user.UserDeleteResume;
import com.zyh.easyapplyresume.model.pojo.user.UserSaveResume;
import com.zyh.easyapplyresume.model.query.user.CPortUserDeleteResumeQuery;
import com.zyh.easyapplyresume.model.vo.user.UserDeleteResumeInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserSaveResumeInfoVO;
import com.zyh.easyapplyresume.service.user.UserDeleteResumeBySystemService;
import com.zyh.easyapplyresume.service.user.UserDeleteResumeService;
import com.zyh.easyapplyresume.service.user.UserSaveResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shiningCloud2025
 */
@Slf4j
@Service
@Transactional
public class UserDeleteResumeServiceImpl implements UserDeleteResumeService {
    @Autowired
    private UserDeleteResumeMapper userDeleteResumeMapper;

    @Autowired
    private UserSaveResumeMapper userSaveResumeMapper;

    @Autowired
    private IndustryMapMapper industryMapMapper;

    @Autowired
    private UserDeleteResumeBySystemService userDeleteResumeBySystemService;

    @Override
    public List<UserDeleteResumeInfoVO> getUserDeleteResumeInfoByUserId(Integer userDeleteResumeId, CPortUserDeleteResumeQuery cPortUserDeleteResumeQuery) {
        try{
            log.info("根据用户id查询用户删除简历信息开始");
            LambdaQueryWrapper<UserDeleteResume> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(UserDeleteResume::getUserDeleteResumeUserId, userDeleteResumeId);
            lambdaQueryWrapper.orderByDesc(UserDeleteResume::getUserDeleteResumeSortedNum);
            if (cPortUserDeleteResumeQuery != null){
                if (cPortUserDeleteResumeQuery.getUserSaveResumeResumeName() != null&& !cPortUserDeleteResumeQuery.getUserSaveResumeResumeName().isEmpty()){
                    lambdaQueryWrapper.like(UserDeleteResume::getUserDeleteResumeResumeName, cPortUserDeleteResumeQuery.getUserSaveResumeResumeName());
                }
                if (cPortUserDeleteResumeQuery.getUserSaveResumeIndustry() != null){
                    lambdaQueryWrapper.eq(UserDeleteResume::getUserDeleteResumeIndustry, cPortUserDeleteResumeQuery.getUserSaveResumeIndustry());
                }
            }

            List<UserDeleteResume> userDeleteResumes = userDeleteResumeMapper.selectList(lambdaQueryWrapper);
            if (userDeleteResumes != null){
                return BeanUtil.copyToList(userDeleteResumes, UserDeleteResumeInfoVO.class);
            }
            return null;
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.error("查询用户删除简历信息失败！", e);
            throw new RuntimeException("查询用户删除简历信息失败");
        }
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
        try{
            log.info("将用户保存的简历添加到用户删除简历开始");
            Integer userSaveResumeUserId = userSaveResumeInfoVO.getUserSaveResumeUserId();
            LambdaQueryWrapper<UserDeleteResume> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserDeleteResume::getUserDeleteResumeUserId, userSaveResumeUserId);
            queryWrapper.orderByDesc(UserDeleteResume::getUserDeleteResumeSortedNum);
            List<UserDeleteResume> userDeleteResumes = userDeleteResumeMapper.selectList(queryWrapper);
            UserDeleteResume userDeleteResume = new UserDeleteResume();
            userDeleteResume.setUserDeleteResumeId(userSaveResumeInfoVO.getUserSaveResumeId());
            userDeleteResume.setUserDeleteResumeResumeName(userSaveResumeInfoVO.getUserSaveResumeResumeName());
            userDeleteResume.setUserDeleteResumeIndustry(userSaveResumeInfoVO.getUserSaveResumeIndustry());
            userDeleteResume.setUserDeleteResumeIndustryName(industryMapMapper.selectById(userSaveResumeInfoVO.getUserSaveResumeIndustry()).getIndustryMapIndustryName());
            userDeleteResume.setUserDeleteResumeResumeReactCode(userSaveResumeInfoVO.getUserSaveResumeResumeReactCode());
            userDeleteResume.setUserDeleteResumeCreatedTime(userSaveResumeInfoVO.getUserSaveResumeCreatedTime());
            userDeleteResume.setUserDeleteResumeUpdatedTime(userSaveResumeInfoVO.getUserSaveResumeUpdatedTime());
            Integer userDeleteResumeSortedNum = 0;
            if (userDeleteResumes==null||userDeleteResumes.isEmpty()){
                userDeleteResume.setUserDeleteResumeSortedNum(userDeleteResumeSortedNum);
            }else{
                userDeleteResumeSortedNum = userDeleteResumes.getFirst().getUserDeleteResumeSortedNum();
                userDeleteResume.setUserDeleteResumeSortedNum(userDeleteResumeSortedNum + 1);

            }
            userDeleteResume.setUserDeleteResumeUserId(userSaveResumeUserId);
            userDeleteResume.setUserDeleteResumeDeleteTime(new Date());
            userDeleteResumeMapper.insert(userDeleteResume);
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.error("将用户保存的简历添加到用户删除简历失败", e);
            // TODO:一定要回抛,不然事务失效就会有，删除了但是记录失败 按理说两者要同成功或者同失败的
            throw new RuntimeException("将用户保存的简历添加到用户删除简历失败");
        }

    }

    @Override
    public void addUserDeleteResumeToUserSaveResume(UserDeleteResumeInfoVO userDeleteResumeInfoVO) {
        try{
            log.info("将用户删除简历添加到用户保存简历开始");
            Integer userId = userDeleteResumeInfoVO.getUserDeleteResumeUserId();
            LambdaQueryWrapper<UserSaveResume> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserSaveResume::getUserSaveResumeUserId, userId);
            queryWrapper.orderByDesc(UserSaveResume::getUserSaveResumeSortedNum);
            List<UserSaveResume> userSaveResumes = userSaveResumeMapper.selectList(queryWrapper);

            UserSaveResume userSaveResume = new UserSaveResume();
            userSaveResume.setUserSaveResumeResumeName(userDeleteResumeInfoVO.getUserDeleteResumeResumeName());
            LambdaQueryWrapper<IndustryMap> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(IndustryMap::getIndustryMapIndustryName, userDeleteResumeInfoVO.getUserDeleteResumeIndustryName());
            IndustryMap industryMap = industryMapMapper.selectOne(queryWrapper1);
            userSaveResume.setUserSaveResumeIndustry(industryMap.getIndustryMapIndustryCode());
            userSaveResume.setUserSaveResumeResumeReactCode(userDeleteResumeInfoVO.getUserDeleteResumeResumeReactCode());
            userSaveResume.setUserSaveResumeCreatedTime(userDeleteResumeInfoVO.getUserDeleteResumeCreatedTime());
            userSaveResume.setUserSaveResumeUpdatedTime(userDeleteResumeInfoVO.getUserDeleteResumeUpdatedTime());
            Integer userSaveResumeSortedNum=0;
            if (userSaveResumes==null||userSaveResumes.isEmpty()){
                userSaveResume.setUserSaveResumeSortedNum(userSaveResumeSortedNum);
            }else{
                userSaveResumeSortedNum = userSaveResumes.getFirst().getUserSaveResumeSortedNum();
                userSaveResume.setUserSaveResumeSortedNum(userSaveResumeSortedNum + 1);
            }
            if (userSaveResumeSortedNum>=4){
                throw new BusException(UserCodeEnum.USER_SAVE_RESUME_NOT_DAYU_FIVE);
            }
            userSaveResume.setUserSaveResumeUserId(userId);
            userSaveResumeMapper.insert(userSaveResume);
            LambdaQueryWrapper<UserDeleteResume> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper2.eq(UserDeleteResume::getUserDeleteResumeUserId, userDeleteResumeInfoVO.getUserDeleteResumeUserId());
            lambdaQueryWrapper2.eq(UserDeleteResume::getUserDeleteResumeSortedNum, userDeleteResumeInfoVO.getUserDeleteResumeSortedNum());
            userDeleteResumeMapper.delete(lambdaQueryWrapper2);
            reorderResumeSortedNum(userDeleteResumeInfoVO.getUserDeleteResumeUserId(), userDeleteResumeInfoVO.getUserDeleteResumeSortedNum());
            // TODO 添加回去以后要删掉
            log.info("将用户删除简历添加到用户保存简历成功");
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.error("添加用户删除简历失败", e);
            throw new RuntimeException("添加用户删除简历失败");
        }

    }

    @Override
    public void clearUserAllDeleteResume(Integer userId) {
        try{
            log.info("开始删除用户所有删除简历");
            LambdaQueryWrapper<UserDeleteResume> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserDeleteResume::getUserDeleteResumeUserId, userId);
            List<UserDeleteResume> userDeleteResumes = userDeleteResumeMapper.selectList(queryWrapper);
            // TODO 这里应该有问题
            userDeleteResumeBySystemService.addExpiredUserDeleteResume(userDeleteResumes);
            userDeleteResumeMapper.delete(queryWrapper);
            log.info("删除用户所有删除简历成功");
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.error("删除用户所有删除简历失败", e);
            throw new RuntimeException("删除用户所有删除简历失败");
        }

    }

    @Override
    public void clearExpiredResume() {
        try{
            List<UserDeleteResume> userDeleteResumes = userDeleteResumeMapper.selectList(null);
            if (userDeleteResumes == null || userDeleteResumes.isEmpty()) {
                return;
            }
            Date now = new Date();
            long sevenDaysInMillis = 7 * 24 * 60 * 60 * 1000L;
            List<UserDeleteResume> expiredResumes = new ArrayList<>();
            for (UserDeleteResume userDeleteResume : userDeleteResumes) {
                Date deleteTime = userDeleteResume.getUserDeleteResumeDeleteTime();
                if (deleteTime != null) {
                    long timeDiff = now.getTime() - deleteTime.getTime();
                    if (timeDiff > sevenDaysInMillis) {
                        expiredResumes.add(userDeleteResume);
                    }
                }
            }
            if (!expiredResumes.isEmpty()){
                log.info("删除的简历有：{}", expiredResumes);
                userDeleteResumeBySystemService.addExpiredUserDeleteResume(expiredResumes);
                userDeleteResumeMapper.deleteBatchIds(expiredResumes.stream()
                        .map(item -> item.getUserDeleteResumeId())
                        .collect(Collectors.toList()));
            }

        } catch (BusException e){
            throw e;
        } catch (Exception e){
            log.error("系统删除简历表添加失败", e);
            throw new RuntimeException("系统删除简历表添加失败");
        }
    }

    @Override
    public void updateUserDeleteResumeNameByUserIdAndResumeSortedNumAndResumeName(Integer userId, Integer resumeSortedNum, String resumeName) {
        LambdaQueryWrapper<UserDeleteResume> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDeleteResume::getUserDeleteResumeUserId, userId);
        queryWrapper.eq(UserDeleteResume::getUserDeleteResumeSortedNum, resumeSortedNum);
        UserDeleteResume userDeleteResume = userDeleteResumeMapper.selectOne(queryWrapper);
        userDeleteResume.setUserDeleteResumeResumeName(resumeName);
        userDeleteResumeMapper.updateById(userDeleteResume);
    }

    private void reorderResumeSortedNum(Integer userId, Integer deletedSortedNum) {
        LambdaQueryWrapper<UserDeleteResume> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDeleteResume::getUserDeleteResumeUserId, userId);
        queryWrapper.gt(UserDeleteResume::getUserDeleteResumeSortedNum, deletedSortedNum);
        queryWrapper.orderByAsc(UserDeleteResume::getUserDeleteResumeSortedNum);
        List<UserDeleteResume> resumeList = userDeleteResumeMapper.selectList(queryWrapper);

        if (resumeList != null && !resumeList.isEmpty()) {
            for (UserDeleteResume resume : resumeList) {
                LambdaUpdateWrapper<UserDeleteResume> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(UserDeleteResume::getUserDeleteResumeUserId, resume.getUserDeleteResumeUserId());
                updateWrapper.eq(UserDeleteResume::getUserDeleteResumeSortedNum, resume.getUserDeleteResumeSortedNum());
                updateWrapper.set(UserDeleteResume::getUserDeleteResumeSortedNum, resume.getUserDeleteResumeSortedNum() - 1);
                userDeleteResumeMapper.update(null, updateWrapper);
            }
        }
    }
}
