package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.businessEnum.AdminBusinessEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminFeedbackMapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminFeedbackForm;
import com.zyh.easyapplyresume.model.pojo.admin.Admin;
import com.zyh.easyapplyresume.model.pojo.admin.AdminFeedback;
import com.zyh.easyapplyresume.model.query.admin.AdminFeedbackQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminFeedbackInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminFeedbackPageVO;
import com.zyh.easyapplyresume.service.admin.AdminFeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Slf4j
@Service
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
    }

    @Override
    public void updateFeedbackStep(Integer feedbackId, Integer OperationCode,String title, String content) {
        LambdaQueryWrapper<AdminFeedback> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminFeedback::getAdminFeedbackId, feedbackId);
        AdminFeedback adminFeedback = adminFeedbackMapper.selectOne(queryWrapper);
        Integer adminFeedbackAdminId = adminFeedback.getAdminFeedbackAdminId();
        LambdaQueryWrapper<Admin> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Admin::getAdminId, adminFeedbackAdminId);
        Admin admin = adminMapper.selectOne(queryWrapper1);
        String adminEmail = admin.getAdminEmail();
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
            sendCommunicationEmailService.sendTextEmailUsallyDefition(defaultFromEmail,"您有一条新的反馈待处理-管理平台","您有一条新的反馈待处理-管理平台");
            adminFeedback.setAdminFeedbackCurStep(AdminBusinessEnum.ADMIN_DO_RECEIVER.getMessage());
        }else if (OperationCode==1){
            adminFeedback.setAdminFeedbackCurStep(AdminBusinessEnum.ADMIN_ALREADY_IGNORE.getMessage());
        }
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
        return BeanUtil.toBean(adminFeedback, AdminFeedbackInfoVO.class);
    }

    @Override
    public Page<AdminFeedbackPageVO> getFeedbackPage(int size, int page, AdminFeedbackQuery adminFeedbackQuery) {

        return null;
    }
}
