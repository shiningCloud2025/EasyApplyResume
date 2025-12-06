package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminFeedbackMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminFeedbackForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminFeedback;
import com.zyh.easyapplyresume.model.query.admin.AdminFeedbackQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminFeedbackInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminFeedbackPageVO;
import com.zyh.easyapplyresume.service.admin.AdminFeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Override
    public void addFeedback(AdminFeedbackForm adminFeedbackForm) {
        AdminFeedback adminFeedback = new AdminFeedback();
        adminFeedback.setAdminFeedbackTitle(adminFeedbackForm.getAdminFeedbackTitle());
        adminFeedback.setAdminFeedbackContent(adminFeedbackForm.getAdminFeedbackContent());
        adminFeedback.setAdminFeedbackTime(new Date());
        adminFeedback.setAdminFeedbackRecentTime(new Date());
        
    }

    @Override
    public void deleteFeedback(Integer feedbackId) {

    }

    @Override
    public AdminFeedbackInfoVO findFeedbackById(Integer feedbackId) {
        return null;
    }

    @Override
    public Page<AdminFeedbackPageVO> getFeedbackPage(int size, int page, AdminFeedbackQuery adminFeedbackQuery) {
        return null;
    }
}
