package com.zyh.easyapplyresume.service.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.query.user.UserFeedbackRecordQuery;
import com.zyh.easyapplyresume.model.vo.user.UserFeedbackRecordInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserFeedbackRecordPageVO;

/**
 * @author shiningCloud2025
 */
public interface UserFeedbackRecordService {

    // 查看用户反馈记录
    UserFeedbackRecordInfoVO findUserFeedbackRecordByFeedbackRecordId(Integer feedbackRecordId);

    // 分页查看用户反馈记录
    Page<UserFeedbackRecordPageVO> findUserFeedbackRecordPage(Integer pageNum, Integer pageSize, UserFeedbackRecordQuery query);


}
