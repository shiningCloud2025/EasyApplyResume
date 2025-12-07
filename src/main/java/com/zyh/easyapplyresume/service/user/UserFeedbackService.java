package com.zyh.easyapplyresume.service.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.user.UserFeedbackForm;
import com.zyh.easyapplyresume.model.query.admin.AdminFeedbackQuery;
import com.zyh.easyapplyresume.model.query.user.UserFeedbackQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminFeedbackPageVO;
import com.zyh.easyapplyresume.model.vo.user.UserFeedbackInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserFeedbackPageVO;

/**
 * @author shiningCloud2025
 */
public interface UserFeedbackService{

    /**
     * 添加反馈信息
     * @param userFeedbackForm
     */
    public void addFeedback(UserFeedbackForm userFeedbackForm);

    /**
     * 更新反馈阶段
     * @param feedbackId
     * @param OperationCode
     */
    public void updateFeedbackStep(Integer feedbackId, Integer OperationCode,String title, String content,Integer operationPersonId);


    /**
     * 删除反馈信息
     * @param feedbackId
     */
    public void deleteFeedback(Integer feedbackId);

    /**
     * 查询反馈信息
     * @param feedbackId
     */
    public UserFeedbackInfoVO findFeedbackById(Integer feedbackId);

    /**
     * 分页查询反馈信息
     */
    public Page<UserFeedbackPageVO> getFeedbackPage(int size, int page, UserFeedbackQuery userFeedbackQuery);
}
