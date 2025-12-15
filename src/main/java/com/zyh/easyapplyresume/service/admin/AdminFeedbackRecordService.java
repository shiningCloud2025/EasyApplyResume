package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.query.admin.AdminFeedbackRecordQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminFeedbackRecordInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminFeedbackRecordPageVO;

/**
 * @author shiningCloud2025
 */
public interface AdminFeedbackRecordService {

    // 查询管理员反馈记录
    AdminFeedbackRecordInfoVO findAdminFeedbackRecordByFeedbackRecordId(Integer feedbackRecordId);

    // 分页查询管理员反馈记录
    Page<AdminFeedbackRecordPageVO> findAdminFeedbackRecordPage(Integer pageNum, Integer pageSize, AdminFeedbackRecordQuery  query);


}
