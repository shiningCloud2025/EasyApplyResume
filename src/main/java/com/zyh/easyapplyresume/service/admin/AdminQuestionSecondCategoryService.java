package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.admin.AdminQuestionSecondCategoryForm;
import com.zyh.easyapplyresume.model.query.admin.AdminQuestionSecondCategoryQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionSecondCategoryInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionSecondCategoryPageVO;

import java.util.List;

/**
 * 题库小类管理服务
 * @author shiningCloud2025
 */
public interface AdminQuestionSecondCategoryService {

    /**
     * 新增题库小类
     */
    Integer addQuestionSecondCategory(AdminQuestionSecondCategoryForm form);

    /**
     * 修改题库小类
     */
    Integer updateQuestionSecondCategory(AdminQuestionSecondCategoryForm form);

    /**
     * 删除题库小类
     */
    Integer deleteQuestionSecondCategory(Integer questionSecondCategoryId);

    /**
     * 内部业务使用：按大类id删除题库小类
     */
    Integer deleteQuestionSecondCategoryByFirstCategoryId(Integer questionFirstCategoryId);

    /**
     * 查询题库小类详情
     */
    AdminQuestionSecondCategoryInfoVO findQuestionSecondCategoryById(Integer questionSecondCategoryId);

    /**
     * 分页查询题库小类
     */
    Page<AdminQuestionSecondCategoryPageVO> findQuestionSecondCategoryByPage(Integer pageNum, Integer pageSize, AdminQuestionSecondCategoryQuery query);

    /**
     * 根据题库大类id查询题库小类
     */
    List<AdminQuestionSecondCategoryInfoVO> findQuestionSecondCategoryByFirstCategoryId(Integer questionFirstCategoryId);

    /**
     * 查询所有题库小类
     */
    List<AdminQuestionSecondCategoryInfoVO> findAllQuestionSecondCategory();
}
