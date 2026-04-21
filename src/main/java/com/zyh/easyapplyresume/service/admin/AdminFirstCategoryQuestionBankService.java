package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.query.user.UserFirstCategoryQuestionBankQuery;
import com.zyh.easyapplyresume.model.vo.user.UserFirstCategoryQuestionBankInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserFirstCategoryQuestionBankPageVO;

/**
 * 用户-笔试题目管理服务
 * @author shiningCloud2025
 */
public interface AdminFirstCategoryQuestionBankService {
    /**
     * 内部业务使用：按题目id同步题库分类信息
     */
    Integer updateFirstCategoryQuestionBankCategoryInfo(
            Integer questionBankId,
            Integer questionFirstCategoryId,
            String questionFirstCategoryName,
            Integer questionSecondCategoryId,
            String questionSecondCategoryName
    );

    /**
     * 内部业务使用：按大类id同步用户题目大类名称
     */
    Integer updateFirstCategoryQuestionBankFirstCategoryNameByFirstCategoryId(
            Integer questionFirstCategoryId,
            String questionFirstCategoryName
    );

    /**
     * 内部业务使用：按小类id同步用户题目分类信息
     */
    Integer updateFirstCategoryQuestionBankCategoryInfoBySecondCategoryId(
            Integer questionSecondCategoryId,
            Integer questionFirstCategoryId,
            String questionFirstCategoryName,
            String questionSecondCategoryName
    );

    /**
     * 内部业务使用：按小类id删除用户题目记录
     */
    Integer deleteFirstCategoryQuestionBankBySecondCategoryId(Integer questionSecondCategoryId);

    /**
     * 内部业务使用：按大类id删除用户题目记录
     */
    Integer deleteFirstCategoryQuestionBankByFirstCategoryId(Integer questionFirstCategoryId);

    /**
     * 删除用户-笔试题目记录
     */
    Integer deleteFirstCategoryQuestionBank(Integer userId, Integer questionBankId);

    /**
     * 查询用户-笔试题目详情
     */
    UserFirstCategoryQuestionBankInfoVO findFirstCategoryQuestionBankById(Integer userId, Integer questionBankId);

    /**
     * 分页查询用户-笔试题目
     */
    Page<UserFirstCategoryQuestionBankPageVO> findFirstCategoryQuestionBankByPage(Integer pageNum, Integer pageSize, UserFirstCategoryQuestionBankQuery query);
}
