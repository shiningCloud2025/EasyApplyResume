package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.admin.AdminLlmUtilsInfoForm;
import com.zyh.easyapplyresume.model.query.admin.AdminLlmUtilsInfoQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminLlmUtilsInfoPageVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminLlmUtilsInfoVO;

/**
 * @author shiningCloud2025
 */
public interface AdminLlmUtilsInfoService {

    /**
     * 新增LLM工具类调用日志
     * @param adminLlmUtilsInfoForm
     */
    Integer addAdminLlmUtilsInfo(AdminLlmUtilsInfoForm adminLlmUtilsInfoForm);

    /**
     * 根据ID查询LLM工具类调用日志详情
     * @param llmUtilsInfoId
     * @return
     */
    AdminLlmUtilsInfoVO findAdminLlmUtilsInfoById(Long llmUtilsInfoId);

    /**
     * 分页查询LLM工具类调用日志
     * @param pageNum
     * @param pageSize
     * @param adminLlmUtilsInfoQuery
     * @return
     */
    Page<AdminLlmUtilsInfoPageVO> findAdminLlmUtilsInfoByPage(Integer pageNum, Integer pageSize, AdminLlmUtilsInfoQuery adminLlmUtilsInfoQuery);

}
