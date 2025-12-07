package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.businessEnum.AdminBusinessEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminFeedbackMapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminFeedbackRecordMapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminFeedbackForm;
import com.zyh.easyapplyresume.model.pojo.admin.Admin;
import com.zyh.easyapplyresume.model.pojo.admin.AdminFeedback;
import com.zyh.easyapplyresume.model.pojo.admin.AdminFeedbackRecord;
import com.zyh.easyapplyresume.model.query.admin.AdminFeedbackQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminFeedbackInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminFeedbackPageVO;
import com.zyh.easyapplyresume.service.admin.AdminFeedbackService;
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
public class AdminFeedbackServiceImpl implements AdminFeedbackService {
    @Autowired
    private AdminFeedbackMapper adminFeedbackMapper;

    @Autowired
    private SendCommunicationEmailServiceImpl sendCommunicationEmailService;

    @Autowired
    private AdminMapper adminMapper;

    // 从配置文件中读取默认的发送者邮箱
    @Value("${spring.mail.username}")
    private String defaultFromEmail;

    @Autowired
    private AdminFeedbackRecordMapper adminFeedbackRecordMapper;

    @Override
    public void addFeedback(AdminFeedbackForm adminFeedbackForm) {
        AdminFeedback adminFeedback = new AdminFeedback();
        adminFeedback.setAdminFeedbackTitle(adminFeedbackForm.getAdminFeedbackTitle());
        adminFeedback.setAdminFeedbackContent(adminFeedbackForm.getAdminFeedbackContent());
        adminFeedback.setAdminFeedbackTime(new Date());
        adminFeedback.setAdminFeedbackRecentTime(new Date());
        adminFeedback.setAdminFeedbackCurStep(AdminBusinessEnum.ADMIN_WAIT_RECEIVED.getMessage());
        adminFeedback.setAdminFeedbackAdminId(adminFeedbackForm.getAdminFeedbackAdminId());
        adminFeedbackMapper.insert(adminFeedback);
        sendCommunicationEmailService.sendTextEmailUsallyDefition(defaultFromEmail,"您有一条新的反馈待接受-管理平台","您有一条新的反馈待接收-管理平台");
    }

    @Override
    public void updateFeedbackStep(Integer feedbackId, Integer OperationCode,String title, String content,Integer operationPersonId) {
        AdminFeedbackRecord adminFeedbackRecord = new AdminFeedbackRecord();
        LambdaQueryWrapper<AdminFeedback> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminFeedback::getAdminFeedbackId, feedbackId);
        AdminFeedback adminFeedback = adminFeedbackMapper.selectOne(queryWrapper);
        Integer adminFeedbackAdminId = adminFeedback.getAdminFeedbackAdminId();
        LambdaQueryWrapper<Admin> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Admin::getAdminId, adminFeedbackAdminId);
        Admin admin = adminMapper.selectOne(queryWrapper1);
        String adminEmail = admin.getAdminEmail();
        adminFeedbackRecord.setAdminFeedbackRecordTitle(adminFeedback.getAdminFeedbackTitle());
        adminFeedbackRecord.setAdminFeedbackRecordContent(adminFeedback.getAdminFeedbackContent());
        adminFeedbackRecord.setAdminFeedbackRecordTime(adminFeedback.getAdminFeedbackTime());
        adminFeedbackRecord.setAdminFeedbackRecordCurrentStepSolveTime(new Date());
        adminFeedbackRecord.setAdminFeedbackRecordOldStep(adminFeedback.getAdminFeedbackCurStep());
        adminFeedbackRecord.setAdminFeedbackRecordApprovalPersonId(operationPersonId);
        LambdaQueryWrapper<Admin> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(Admin::getAdminId, operationPersonId);
        Admin admin1 = adminMapper.selectOne(queryWrapper2);
        adminFeedbackRecord.setAdminFeedbackRecordNewStep(admin1.getAdminUsername());
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
            sendCommunicationEmailService.sendTextEmailUsallyDefition(adminEmail,"您好"+admin.getAdminUsername()+",感谢您的反馈","您好"+admin.getAdminUsername()+"，您的反馈已被管理员接受，等待回复...");
            sendCommunicationEmailService.sendTextEmailUsallyDefition(defaultFromEmail,"您有一条新的反馈待回复-管理平台","您有一条新的反馈待回复-管理平台");
            adminFeedback.setAdminFeedbackCurStep(AdminBusinessEnum.ADMIN_DO_RECEIVER.getMessage());
            adminFeedbackRecord.setAdminFeedbackRecordNewStep(adminFeedback.getAdminFeedbackCurStep());
        }else if (OperationCode==1){
            adminFeedback.setAdminFeedbackCurStep(AdminBusinessEnum.ADMIN_ALREADY_IGNORE.getMessage());
            adminFeedbackRecord.setAdminFeedbackRecordNewStep(adminFeedback.getAdminFeedbackCurStep());
        }else if(OperationCode==2){
            sendCommunicationEmailService.sendHtmlEmailUsallyDefition(adminEmail,title, content);
            sendCommunicationEmailService.sendTextEmailUsallyDefition(defaultFromEmail,"您有一条新的反馈处理完毕-管理平台","您有一条新的反馈处理完毕-管理平台");
            adminFeedback.setAdminFeedbackCurStep(AdminBusinessEnum.ADMIN_ALREADY_REPLY.getMessage());
            adminFeedbackRecord.setAdminFeedbackRecordNewStep(adminFeedback.getAdminFeedbackCurStep());
        } else if (OperationCode==3) {
            sendCommunicationEmailService.sendTextEmailUsallyDefition(defaultFromEmail,"您有一条新的反馈处理完毕-管理平台","您有一条新的反馈处理完毕-管理平台");
            adminFeedback.setAdminFeedbackCurStep(AdminBusinessEnum.ADMIN_REJECT_REPLY.getMessage());
            adminFeedbackRecord.setAdminFeedbackRecordNewStep(adminFeedback.getAdminFeedbackCurStep());
        }
        adminFeedback.setAdminFeedbackRecentTime(new Date());
        adminFeedbackMapper.updateById(adminFeedback);
        adminFeedbackRecordMapper.insert(adminFeedbackRecord);
    }


    @Override
    public void deleteFeedback(Integer feedbackId) {
        LambdaQueryWrapper<AdminFeedback> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminFeedback::getAdminFeedbackId, feedbackId);
        adminFeedbackMapper.delete(queryWrapper);
    }

    @Override
    public AdminFeedbackInfoVO findFeedbackById(Integer feedbackId) {
        LambdaQueryWrapper<AdminFeedback> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminFeedback::getAdminFeedbackId, feedbackId);
        AdminFeedback adminFeedback = adminFeedbackMapper.selectOne(queryWrapper);
        AdminFeedbackInfoVO adminFeedbackInfoVO = BeanUtil.toBean(adminFeedback, AdminFeedbackInfoVO.class);
        Admin admin = adminMapper.selectById(adminFeedback.getAdminFeedbackAdminId());
        if (admin != null) {
            adminFeedbackInfoVO.setAdminFeedbackAdminName(admin.getAdminUsername());
        }
        return  adminFeedbackInfoVO;
    }

    @Override
    public Page<AdminFeedbackPageVO> getFeedbackPage(int size, int page, AdminFeedbackQuery adminFeedbackQuery) {
        LambdaQueryWrapper<AdminFeedback> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (adminFeedbackQuery != null) {
            if (adminFeedbackQuery.getAdminFeedbackTitle() != null && !adminFeedbackQuery.getAdminFeedbackTitle().trim().isEmpty()) {
                lambdaQueryWrapper.like(AdminFeedback::getAdminFeedbackTitle, adminFeedbackQuery.getAdminFeedbackTitle().trim());
            }

            if (adminFeedbackQuery.getAdminFeedbackContent() != null && !adminFeedbackQuery.getAdminFeedbackContent().trim().isEmpty()) {
                lambdaQueryWrapper.like(AdminFeedback::getAdminFeedbackContent, adminFeedbackQuery.getAdminFeedbackContent().trim());
            }
        }

        Page<AdminFeedback> feedbackPage = adminFeedbackMapper.selectPage(
                new Page<>(page, size),
                lambdaQueryWrapper
        );

        Page<AdminFeedbackPageVO> resultPage = new Page<>();
        resultPage.setCurrent(feedbackPage.getCurrent());
        resultPage.setSize(feedbackPage.getSize());
        resultPage.setTotal(feedbackPage.getTotal());
        resultPage.setPages(feedbackPage.getPages());
        resultPage.setRecords(feedbackPage.getRecords().stream()
                .map(feedback -> {
                    AdminFeedbackPageVO pageVO = new AdminFeedbackPageVO();
                    BeanUtil.copyProperties(feedback, pageVO);
                    Admin admin = adminMapper.selectById(feedback.getAdminFeedbackAdminId());
                    if (admin != null) {
                        pageVO.setAdminFeedbackAdminName(admin.getAdminUsername());
                    }
                    return pageVO;
                })
                .toList());

        return resultPage;
    }
}
