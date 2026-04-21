package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.admin.AdminQuestionBankForm;
import com.zyh.easyapplyresume.model.query.admin.AdminQuestionBankQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionBankInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionBankPageVO;

/**
 * 题库题目管理服务
 * @author shiningCloud2025
 */
public interface AdminQuestionBankService {
    /**
     * 内部业务使用：按大类id同步题库题目大类名称
     */
    Integer updateQuestionBankFirstCategoryNameByFirstCategoryId(
            Integer questionFirstCategoryId,
            String questionFirstCategoryName
    );

    /**
     * 内部业务使用：按小类id同步题库题目分类信息
     */
    Integer updateQuestionBankSecondCategoryInfoBySecondCategoryId(
            Integer questionSecondCategoryId,
            Integer questionFirstCategoryId,
            String questionFirstCategoryName,
            String questionSecondCategoryName
    );

    /**
     * 内部业务使用：按小类id删除题库题目
     */
    Integer deleteQuestionBankBySecondCategoryId(Integer questionSecondCategoryId);

    /**
     * 内部业务使用：按大类id删除题库题目
     */
    Integer deleteQuestionBankByFirstCategoryId(Integer questionFirstCategoryId);

    /**
     * 新增题库题目
     */
    Integer addQuestionBank(AdminQuestionBankForm form);

    /**
     * 修改题库题目
     */
    Integer updateQuestionBank(AdminQuestionBankForm form);

    /**
     * 删除题库题目
     */
    Integer deleteQuestionBank(Integer questionBankId);

    /**
     * 查询题库题目详情
     */
    AdminQuestionBankInfoVO findQuestionBankById(Integer questionBankId);

    /**
     * 分页查询题库题目
     */
    Page<AdminQuestionBankPageVO> findQuestionBankByPage(
            Integer pageNum,
            Integer pageSize,
            AdminQuestionBankQuery query
    );
}
