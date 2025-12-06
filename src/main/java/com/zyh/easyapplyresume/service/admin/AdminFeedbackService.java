package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.admin.AdminFeedbackForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminFeedback;
import com.zyh.easyapplyresume.model.query.admin.AdminFeedbackQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminFeedbackInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminFeedbackPageVO;

/**
 * @author shiningCloud2025
 */
public interface AdminFeedbackService {
    /**
     * 添加反馈信息
     * @param adminFeedbackForm
     */
    public void addFeedback(AdminFeedbackForm adminFeedbackForm);

    /**
     * 删除反馈信息
     * @param feedbackId
     */
    public void deleteFeedback(Integer feedbackId);

    /**
     * 查询反馈信息
     * @param feedbackId
     */
    public AdminFeedbackInfoVO findFeedbackById(Integer feedbackId);


    /**
     * 分页查询反馈信息
     */
    public Page<AdminFeedbackPageVO> getFeedbackPage(int size, int page, AdminFeedbackQuery adminFeedbackQuery);


}
