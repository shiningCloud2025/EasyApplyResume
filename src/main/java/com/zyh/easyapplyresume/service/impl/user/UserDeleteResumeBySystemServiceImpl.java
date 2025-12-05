package com.zyh.easyapplyresume.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserDeleteResumeBySystemMapper;
import com.zyh.easyapplyresume.model.pojo.user.UserDeleteResume;
import com.zyh.easyapplyresume.model.pojo.user.UserDeleteResumeBySystem;
import com.zyh.easyapplyresume.service.user.UserDeleteResumeBySystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shiningCloud2025
 */
@Slf4j
@Service
@Transactional
public class UserDeleteResumeBySystemServiceImpl implements UserDeleteResumeBySystemService {
    @Autowired
    private UserDeleteResumeBySystemMapper userDeleteResumeBySystemMapper;

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

    }
}
