package com.zyh.easyapplyresume.service.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.query.user.UserQuestionBankQuery;
import com.zyh.easyapplyresume.model.vo.user.UserQuestionBankPageVO;

/**
 * 用户端题库题目服务
 * @author shiningCloud2025
 */
public interface UserQuestionBankService {

    /**
     * 分页查询用户端题库题目
     */
    Page<UserQuestionBankPageVO> findQuestionBankByPage(
            Integer userId,
            Integer pageNum,
            Integer pageSize,
            UserQuestionBankQuery query
    );
}
