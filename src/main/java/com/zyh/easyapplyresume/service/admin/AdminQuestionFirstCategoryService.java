package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.admin.AdminQuestionFirstCategoryForm;
import com.zyh.easyapplyresume.model.query.admin.AdminQuestionFirstCategoryQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionFirstCategoryInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionFirstCategoryPageVO;

import java.util.List;

/**
 * 题库大类管理服务
 * @author shiningCloud2025
 */
public interface AdminQuestionFirstCategoryService {

    /**
     * 新增题库大类
     */
    Integer addQuestionFirstCategory(AdminQuestionFirstCategoryForm form);

    /**
     * 修改题库大类
     */
    Integer updateQuestionFirstCategory(AdminQuestionFirstCategoryForm form);

    /**
     * 删除题库大类
     */
    Integer deleteQuestionFirstCategory(Integer questionFirstCategoryId);

    /**
     * 查询题库大类详情
     */
    AdminQuestionFirstCategoryInfoVO findQuestionFirstCategoryById(Integer questionFirstCategoryId);

    /**
     * 分页查询题库大类
     */
    Page<AdminQuestionFirstCategoryPageVO> findQuestionFirstCategoryByPage(Integer pageNum, Integer pageSize, AdminQuestionFirstCategoryQuery query);

    /**
     * 查询所有题库大类
     */
    List<AdminQuestionFirstCategoryInfoVO> findAllQuestionFirstCategory();
}
