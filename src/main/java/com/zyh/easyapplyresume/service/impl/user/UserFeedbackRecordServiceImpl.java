package com.zyh.easyapplyresume.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminMapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserFeedbackRecordMapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserMapper;
import com.zyh.easyapplyresume.model.pojo.user.User;
import com.zyh.easyapplyresume.model.pojo.user.UserFeedbackRecord;
import com.zyh.easyapplyresume.model.query.user.UserFeedbackRecordQuery;
import com.zyh.easyapplyresume.model.vo.user.UserFeedbackRecordInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserFeedbackRecordPageVO;
import com.zyh.easyapplyresume.service.user.UserFeedbackRecordService;
import kotlin.jvm.internal.Lambda;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shiningCloud2025
 */
@Slf4j
@Service
@Transactional
public class UserFeedbackRecordServiceImpl implements UserFeedbackRecordService {
    @Autowired
    private UserFeedbackRecordMapper userFeedbackRecordMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public UserFeedbackRecordInfoVO findUserFeedbackRecordByFeedbackRecordId(Integer feedbackRecordId) {
        try{
            log.info("用户反馈记录ID：{}", feedbackRecordId);
            UserFeedbackRecord userFeedbackRecord = userFeedbackRecordMapper.selectById(feedbackRecordId);
            UserFeedbackRecordInfoVO userFeedbackRecordInfoVO = new UserFeedbackRecordInfoVO();
            BeanUtil.copyProperties(userFeedbackRecord, userFeedbackRecordInfoVO);
            userFeedbackRecordInfoVO.setUserFeedbackRecordName(userMapper.selectById(userFeedbackRecord.getUserFeedbackRecordId()).getUserUsername());
            userFeedbackRecordInfoVO.setAdminFeedbackRecordApprovalPersonName(adminMapper.selectById(userFeedbackRecord.getUserFeedbackRecordApprovalPersonId()).getAdminUsername());
            log.info("用户反馈记录信息：{}", userFeedbackRecordInfoVO);
            return userFeedbackRecordInfoVO;
        }catch (Exception e){
            log.error("用户反馈记录查询失败：{}", e.getMessage());
            return null;
        }
    }

    @Override
    public Page<UserFeedbackRecordPageVO> findUserFeedbackRecordPage(Integer pageNum, Integer pageSize, UserFeedbackRecordQuery userFeedbackRecordQuery) {
        LambdaQueryWrapper<UserFeedbackRecord> queryWrapper = new LambdaQueryWrapper<>();

        if (userFeedbackRecordQuery!=null){
            if (userFeedbackRecordQuery.getUserFeedbackRecordName()!=null&&!userFeedbackRecordQuery.getUserFeedbackRecordName().trim().isEmpty()){
                LambdaQueryWrapper<User> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.eq(User::getUserUsername, userFeedbackRecordQuery.getUserFeedbackRecordName());
                List<Integer> idList = userMapper.selectList(lambdaQueryWrapper1).stream().map(user -> {
                    return user.getUserId();
                }).toList();
                queryWrapper.in(UserFeedbackRecord::getUserFeedbackRecordId, idList);
            }
            if (userFeedbackRecordQuery.getUserFeedbackRecordTitle()!=null&&!userFeedbackRecordQuery.getUserFeedbackRecordTitle().trim().isEmpty()){
                queryWrapper.like(UserFeedbackRecord::getUserFeedbackRecordTitle, userFeedbackRecordQuery.getUserFeedbackRecordTitle());
            }
            if (userFeedbackRecordQuery.getUserFeedbackRecordApprovalPersonName()!=null&&!userFeedbackRecordQuery.getUserFeedbackRecordApprovalPersonName().trim().isEmpty()){
                LambdaQueryWrapper<User> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper2.eq(User::getUserUsername, userFeedbackRecordQuery.getUserFeedbackRecordApprovalPersonName());
                List<Integer> idList = userMapper.selectList(lambdaQueryWrapper2).stream().map(user -> {
                    return user.getUserId();
                }).toList();
                queryWrapper.in(UserFeedbackRecord::getUserFeedbackRecordApprovalPersonId, idList);
            }

        }

        Page<UserFeedbackRecord> feedbackRecordPage = userFeedbackRecordMapper.selectPage(
                new Page<>(pageNum, pageSize),
                queryWrapper
        );

        Page<UserFeedbackRecordPageVO> resultPage = new Page<>();
        resultPage.setCurrent(feedbackRecordPage.getCurrent());
        resultPage.setSize(feedbackRecordPage.getSize());
        resultPage.setTotal(feedbackRecordPage.getTotal());
        resultPage.setRecords(feedbackRecordPage.getRecords().stream().map(
                feedbackRecord -> {
                    UserFeedbackRecordPageVO userFeedbackRecordPageVO = new UserFeedbackRecordPageVO();
                    BeanUtil.copyProperties(feedbackRecord, userFeedbackRecordPageVO);
                    userFeedbackRecordPageVO.setUserFeedbackRecordName(userMapper.selectById(feedbackRecord.getUserFeedbackRecordId()).getUserUsername());
                    userFeedbackRecordPageVO.setAdminFeedbackRecordApprovalPersonName(adminMapper.selectById(feedbackRecord.getUserFeedbackRecordApprovalPersonId()).getAdminUsername());
                    return userFeedbackRecordPageVO;
                }
        ).toList());
        resultPage.setPages(feedbackRecordPage.getPages());

        return resultPage;
    }
}
