package com.zyh.easyapplyresume.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.businessEnum.AdminBusinessEnum;
import com.zyh.easyapplyresume.bean.businessEnum.UserBusinessEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminFeedbackMapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminFeedbackRecordMapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminMapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserFeedbackMapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserFeedbackRecordMapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserMapper;
import com.zyh.easyapplyresume.model.form.user.UserFeedbackForm;
import com.zyh.easyapplyresume.model.pojo.admin.Admin;
import com.zyh.easyapplyresume.model.pojo.user.User;
import com.zyh.easyapplyresume.model.pojo.user.UserFeedback;
import com.zyh.easyapplyresume.model.pojo.user.UserFeedbackRecord;
import com.zyh.easyapplyresume.model.query.admin.AdminFeedbackQuery;
import com.zyh.easyapplyresume.model.query.user.UserFeedbackQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminFeedbackPageVO;
import com.zyh.easyapplyresume.model.vo.user.UserFeedbackInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserFeedbackPageVO;
import com.zyh.easyapplyresume.service.impl.admin.SendCommunicationEmailServiceImpl;
import com.zyh.easyapplyresume.service.user.UserFeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Slf4j
@Service
@Transactional
public class UserFeedbackServiceImpl implements UserFeedbackService {
    @Autowired
    private UserFeedbackMapper userFeedbackMapper;

    @Autowired
    private SendCommunicationEmailServiceImpl sendCommunicationEmailService;

    @Autowired
    private UserMapper userMapper;

    // 从配置文件中读取默认的发送者邮箱
    @Value("${spring.mail.username}")
    private String defaultFromEmail;

    @Autowired
    private UserFeedbackRecordMapper userFeedbackRecordMapper;

    @Autowired
    private AdminMapper adminMapper;
    @Override
    public void addFeedback(UserFeedbackForm userFeedbackForm) {
        UserFeedback userFeedback = new UserFeedback();
        userFeedback.setUserFeedbackTitle(userFeedbackForm.getUserFeedbackTitle());
        userFeedback.setUserFeedbackContent(userFeedbackForm.getUserFeedbackContent());
        userFeedback.setUserFeedbackTime(new Date());
        userFeedback.setUserFeedbackRecentTime(new Date());
        userFeedback.setUserFeedbackCurStep(UserBusinessEnum.USER_WAIT_RECEIVED.getMessage());
        userFeedback.setUserFeedbackUserId(userFeedbackForm.getUserFeedbackUserId());
        userFeedbackMapper.insert(userFeedback);
        sendCommunicationEmailService.sendTextEmailUsallyDefition(defaultFromEmail,"您有一条新的反馈待接受-用户平台","您有一条新的反馈待接收-用户平台");
    }

    @Override
    public void updateFeedbackStep(Integer feedbackId, Integer OperationCode, String title, String content, Integer operationPersonId) {
        UserFeedbackRecord userFeedbackRecord = new UserFeedbackRecord();
        LambdaQueryWrapper<UserFeedback> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFeedback::getUserFeedbackId,feedbackId);
        UserFeedback userFeedback = userFeedbackMapper.selectOne(queryWrapper);
        Integer userFeedbackUserId = userFeedback.getUserFeedbackUserId();
        LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(User::getUserId,userFeedbackUserId);
        User user = userMapper.selectOne(queryWrapper1);
        String userEmail = user.getUserEmail();
        userFeedbackRecord.setUserFeedbackRecordTitle(userFeedback.getUserFeedbackTitle());
        userFeedbackRecord.setUserFeedbackRecordContent(userFeedback.getUserFeedbackContent());
        userFeedbackRecord.setUserFeedbackRecordTime(userFeedback.getUserFeedbackTime());
        userFeedbackRecord.setUserFeedbackRecordCurrentStepSolveTime(new Date());
        userFeedbackRecord.setUserFeedbackRecordOldStep(userFeedback.getUserFeedbackCurStep());
        userFeedbackRecord.setUserFeedbackRecordApprovalPersonId(operationPersonId);
        LambdaQueryWrapper<Admin> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(Admin::getAdminId, operationPersonId);
        Admin admin1 = adminMapper.selectOne(queryWrapper2);
        userFeedbackRecord.setUserFeedbackRecordApprovalPersonName(admin1.getAdminUsername());
        /**
         * TODO
         *                                              ->回复(操作码2)-发送短信，变成已回复
         *          -> 接受(操作码0)->发送短信,然后变成待回复
         *                                              ->拒回复(操作码3)->变成拒回复
         * 待接收
         *          -> 忽视(操作码1)->变成忽视
         *
         */
        if (OperationCode==0){
            sendCommunicationEmailService.sendTextEmailUsallyDefition(userEmail,"您好"+user.getUserUsername()+",感谢您的反馈","您好"+user.getUserUsername()+"，您的反馈已被管理员接受，等待回复...");
            sendCommunicationEmailService.sendTextEmailUsallyDefition(defaultFromEmail,"您有一条新的反馈待回复-用户平台","您有一条新的反馈待回复-用户平台");
            userFeedback.setUserFeedbackCurStep(UserBusinessEnum.USER_DO_RECEIVER.getMessage());
            userFeedbackRecord.setUserFeedbackRecordNewStep(userFeedback.getUserFeedbackCurStep());
        }else if (OperationCode==1){
            userFeedback.setUserFeedbackCurStep(UserBusinessEnum.USER_ALREADY_IGNORE.getMessage());
            userFeedbackRecord.setUserFeedbackRecordNewStep(userFeedback.getUserFeedbackCurStep());
        }else if(OperationCode==2){
            sendCommunicationEmailService.sendHtmlEmailUsallyDefition(userEmail,title, content);
            sendCommunicationEmailService.sendTextEmailUsallyDefition(defaultFromEmail,"您有一条新的反馈处理完毕-用户平台","您有一条新的反馈处理完毕-用户平台");
            userFeedback.setUserFeedbackCurStep(UserBusinessEnum.USER_ALREADY_REPLY.getMessage());
            userFeedbackRecord.setUserFeedbackRecordNewStep(userFeedback.getUserFeedbackCurStep());
        } else if (OperationCode==3) {
            sendCommunicationEmailService.sendTextEmailUsallyDefition(defaultFromEmail,"您有一条新的反馈处理完毕-用户平台","您有一条新的反馈处理完毕-用户平台");
            userFeedback.setUserFeedbackCurStep(UserBusinessEnum.USER_REJECT_REPLY.getMessage());
            userFeedbackRecord.setUserFeedbackRecordNewStep(userFeedback.getUserFeedbackCurStep());
        }

    }

    @Override
    public void deleteFeedback(Integer feedbackId) {
        LambdaQueryWrapper<UserFeedback> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFeedback::getUserFeedbackId, feedbackId);
        userFeedbackMapper.delete(queryWrapper);
    }

    @Override
    public UserFeedbackInfoVO findFeedbackById(Integer feedbackId) {
        LambdaQueryWrapper<UserFeedback> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFeedback::getUserFeedbackId, feedbackId);
        UserFeedback userFeedback = userFeedbackMapper.selectOne(queryWrapper);
        UserFeedbackInfoVO userFeedbackInfoVO = BeanUtil.toBean(userFeedback, UserFeedbackInfoVO.class);
        User user = userMapper.selectById(userFeedback.getUserFeedbackUserId());
        if (user != null) {
            userFeedbackInfoVO.setUserFeedbackUserName(user.getUserUsername());
        }
        return userFeedbackInfoVO;
    }

    @Override
    public Page<UserFeedbackPageVO> getFeedbackPage(int size, int page, UserFeedbackQuery userFeedbackQuery) {
        LambdaQueryWrapper<UserFeedback> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (userFeedbackQuery != null) {
            if (userFeedbackQuery.getUserFeedbackTitle() != null && !userFeedbackQuery.getUserFeedbackTitle().trim().isEmpty()) {
                lambdaQueryWrapper.like(UserFeedback::getUserFeedbackTitle, userFeedbackQuery.getUserFeedbackTitle().trim());
            }

            if (userFeedbackQuery.getUserFeedbackContent() != null && !userFeedbackQuery.getUserFeedbackContent().trim().isEmpty()) {
                lambdaQueryWrapper.like(UserFeedback::getUserFeedbackContent, userFeedbackQuery.getUserFeedbackContent().trim());
            }
        }

        Page<UserFeedback> feedbackPage = userFeedbackMapper.selectPage(
                new Page<>(page, size),
                lambdaQueryWrapper
        );

        Page<UserFeedbackPageVO> resultPage = new Page<>();
        resultPage.setCurrent(feedbackPage.getCurrent());
        resultPage.setSize(feedbackPage.getSize());
        resultPage.setTotal(feedbackPage.getTotal());
        resultPage.setPages(feedbackPage.getPages());
        resultPage.setRecords(feedbackPage.getRecords().stream()
                .map(feedback -> {
                    UserFeedbackPageVO pageVO = new UserFeedbackPageVO();
                    BeanUtil.copyProperties(feedback, pageVO);
                    User user = userMapper.selectById(feedback.getUserFeedbackUserId());
                    if (user != null) {
                        pageVO.setUserFeedbackUserName(user.getUserUsername());
                    }
                    return pageVO;
                })
                .toList());

        return resultPage;
    }
}
