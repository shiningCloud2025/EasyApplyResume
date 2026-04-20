package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.admin.AdminQuestionBankForm;
import com.zyh.easyapplyresume.model.query.admin.AdminQuestionBankQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionBankInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionBankPageVO;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.AdminQuestionBankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminQuestionBankServiceImpl implements AdminQuestionBankService {
    @Override
    public Integer addQuestionBank(AdminQuestionBankForm form) {
        return 0;
    }

    @Override
    public Integer updateQuestionBank(AdminQuestionBankForm form) {
        return 0;
    }

    @Override
    public Integer deleteQuestionBank(Integer questionBankId) {
        return 0;
    }

    @Override
    public AdminQuestionBankInfoVO findQuestionBankById(Integer questionBankId) {
        return null;
    }

    @Override
    public Page<AdminQuestionBankPageVO> findQuestionBankByPage(Integer pageNum, Integer pageSize, AdminQuestionBankQuery query) {
        return null;
    }
}
