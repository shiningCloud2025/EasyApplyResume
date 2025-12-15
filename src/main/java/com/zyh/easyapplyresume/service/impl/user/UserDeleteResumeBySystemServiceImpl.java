package com.zyh.easyapplyresume.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.mapper.mysql.admin.IndustryMapMapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserDeleteResumeBySystemMapper;
import com.zyh.easyapplyresume.model.pojo.user.UserDeleteResume;
import com.zyh.easyapplyresume.model.pojo.user.UserDeleteResumeBySystem;
import com.zyh.easyapplyresume.model.query.user.UserDeleteResumeQuery;
import com.zyh.easyapplyresume.model.vo.user.UserDeleteResumeBySystemPageVO;
import com.zyh.easyapplyresume.model.vo.user.UserDeleteResumeInfoVO;
import com.zyh.easyapplyresume.service.user.UserDeleteResumeBySystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shiningCloud2025
 */
@Slf4j
@Service
@Transactional
public class UserDeleteResumeBySystemServiceImpl implements UserDeleteResumeBySystemService {
    @Autowired
    private UserDeleteResumeBySystemMapper userDeleteResumeBySystemMapper;
    @Autowired
    private IndustryMapMapper industryMapMapper;

    @Override
    public void addExpiredUserDeleteResume(List<UserDeleteResume> userDeleteResumes) {
        List<UserDeleteResumeBySystem> userDeleteResumeBySystems = new LinkedList<>();
        try {
            for (UserDeleteResume userDeleteResume : userDeleteResumes) {
                UserDeleteResumeBySystem userDeleteResumeBySystem = new UserDeleteResumeBySystem();
                userDeleteResumeBySystem.setUserDeleteResumeBySystemResumeName(userDeleteResume.getUserDeleteResumeResumeName());
                userDeleteResumeBySystem.setUserDeleteResumeBySystemIndustry(userDeleteResume.getUserDeleteResumeIndustry());
                userDeleteResumeBySystem.setUserDeleteResumeBySystemResumeReactCode(userDeleteResume.getUserDeleteResumeResumeReactCode());
                userDeleteResumeBySystem.setUserDeleteResumeBySystemCreatedTime(userDeleteResume.getUserDeleteResumeCreatedTime());
                userDeleteResumeBySystem.setUserDeleteResumeBySystemUpdatedTime(userDeleteResume.getUserDeleteResumeUpdatedTime());
                Integer userId = userDeleteResume.getUserDeleteResumeUserId();
                LambdaQueryWrapper<UserDeleteResumeBySystem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(UserDeleteResumeBySystem::getUserDeleteResumeBySystemUserId, userId);
                lambdaQueryWrapper.orderByDesc(UserDeleteResumeBySystem::getUserDeleteResumeBySystemSortedNum);
                List<UserDeleteResumeBySystem> userDeleteResumeBySystemList = userDeleteResumeBySystemMapper.selectList(lambdaQueryWrapper);
                Integer userDeleteResumeBySystemSortedNum = userDeleteResumeBySystemList.get(0).getUserDeleteResumeBySystemSortedNum();
                userDeleteResumeBySystem.setUserDeleteResumeBySystemSortedNum(userDeleteResumeBySystemSortedNum + 1);
                userDeleteResumeBySystem.setUserDeleteResumeBySystemUserId(userId);
                userDeleteResumeBySystem.setUserDeleteResumeBySystemRecycleTime(new Date());
                userDeleteResumeBySystems.add(userDeleteResumeBySystem);
            }
            userDeleteResumeBySystemMapper.insert(userDeleteResumeBySystems);
        }catch (Exception e){
            log.error("系统删除简历表添加失败");
        }

    }



    @Override
    public void clearExpiredUserDeleteResumeEveryThreeMonth() {
        try{
            List<UserDeleteResumeBySystem> userDeleteResumeBySystemList = userDeleteResumeBySystemMapper.selectList(null);
            if (userDeleteResumeBySystemList == null || userDeleteResumeBySystemList.isEmpty()) {
                return;
            }
            Date now = new Date();
            long threeMonthsInMillis = 90L * 24 * 60 * 60 * 1000L;
            List<UserDeleteResumeBySystem> expiredResumes = new ArrayList<>();
            for (UserDeleteResumeBySystem userDeleteResumeBySystem : userDeleteResumeBySystemList) {
                Date recycleTime = userDeleteResumeBySystem.getUserDeleteResumeBySystemRecycleTime();
                if (recycleTime != null) {
                    long timeDiff = now.getTime() - recycleTime.getTime();
                    if (timeDiff > threeMonthsInMillis) {
                        expiredResumes.add(userDeleteResumeBySystem);
                    }
                }
            }
            if (!expiredResumes.isEmpty()){
                log.info("系统回收站删除的简历有：{}", expiredResumes);
                userDeleteResumeBySystemMapper.deleteBatchIds(expiredResumes.stream()
                        .map(UserDeleteResumeBySystem::getUserDeleteResumeBySystemId)
                        .collect(Collectors.toList()));
            }
        } catch (Exception e){
            log.error("系统回收站清理过期简历失败", e);
        }

    }

    @Override
    public UserDeleteResumeInfoVO getUserDeleteResumeInfoById(Integer userDeleteResumeId) {
        try{
            log.info("根据系统删除简历ID查询系统删除简历信息开始");
            LambdaQueryWrapper<UserDeleteResumeBySystem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(UserDeleteResumeBySystem::getUserDeleteResumeBySystemId, userDeleteResumeId);
            UserDeleteResumeBySystem userDeleteResumeBySystem = userDeleteResumeBySystemMapper.selectOne(lambdaQueryWrapper);
            UserDeleteResumeInfoVO userDeleteResumeInfoVO = BeanUtil.copyProperties(userDeleteResumeBySystem, UserDeleteResumeInfoVO.class);
            userDeleteResumeInfoVO.setUserDeleteResumeIndustryName(industryMapMapper.selectById(userDeleteResumeBySystem.getUserDeleteResumeBySystemIndustry()).getIndustryMapIndustryName());
            log.info("根据系统删除简历ID查询系统删除简历信息成功");
            return userDeleteResumeInfoVO;
        }catch (Exception e){
            log.error("根据系统删除简历ID查询系统删除简历信息失败", e);
            return null;
        }

    }

    @Override
    public Page<UserDeleteResumeBySystemPageVO> getUserDeleteResumeInfoPage(Integer pageNum, Integer pageSize, UserDeleteResumeQuery userDeleteResumeQuery) {
        LambdaQueryWrapper<UserDeleteResumeBySystem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (userDeleteResumeQuery != null){
            if (userDeleteResumeQuery.getUserDeleteResumeBySystemResumeName()!= null&& !userDeleteResumeQuery.getUserDeleteResumeBySystemResumeName().trim().isEmpty()){
                lambdaQueryWrapper.like(UserDeleteResumeBySystem::getUserDeleteResumeBySystemResumeName, userDeleteResumeQuery.getUserDeleteResumeBySystemResumeName());
            }
            if (userDeleteResumeQuery.getUserDeleteResumeBySystemUserId()!= null){
                lambdaQueryWrapper.eq(UserDeleteResumeBySystem::getUserDeleteResumeBySystemUserId, userDeleteResumeQuery.getUserDeleteResumeBySystemUserId());
            }
            if (userDeleteResumeQuery.getUserDeleteResumeBySystemRecycleTime()!= null){
                lambdaQueryWrapper.eq(UserDeleteResumeBySystem::getUserDeleteResumeBySystemRecycleTime, userDeleteResumeQuery.getUserDeleteResumeBySystemRecycleTime());
            }
        }
        Page<UserDeleteResumeBySystem> deleteResumeBySystemPage = userDeleteResumeBySystemMapper.selectPage(
                new Page<>(pageNum, pageSize),
                lambdaQueryWrapper
        );

        Page<UserDeleteResumeBySystemPageVO> resultPage = new Page<>();
        resultPage.setCurrent(deleteResumeBySystemPage.getCurrent());
        resultPage.setSize(deleteResumeBySystemPage.getSize());
        resultPage.setTotal(deleteResumeBySystemPage.getTotal());
        resultPage.setPages(deleteResumeBySystemPage.getPages());
        resultPage.setRecords(deleteResumeBySystemPage.getRecords().stream().map(
                deleteResumeBySystem -> {
                    UserDeleteResumeBySystemPageVO userDeleteResumeBySystemPageVO = new UserDeleteResumeBySystemPageVO();
                    BeanUtil.copyProperties(deleteResumeBySystem, userDeleteResumeBySystemPageVO);
                    userDeleteResumeBySystemPageVO.setUserDeleteResumeBySystemIndustryName(industryMapMapper.selectById(deleteResumeBySystem.getUserDeleteResumeBySystemIndustry()).getIndustryMapIndustryName());
                    return userDeleteResumeBySystemPageVO;
                }
        ).toList());

        return resultPage;
    }
}
