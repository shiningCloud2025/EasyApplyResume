package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.admin.AdminUserGuideForm;
import com.zyh.easyapplyresume.model.query.admin.AdminUserGuideQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminUserGuideInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminUserGuidePageVO;

/**
 * 使用指南Service接口
 * @author shiningCloud2025
 */
public interface AdminUserGuideService {

    /**
     * 添加使用指南
     * @param userGuideForm
     * @return
     */
    Integer addUserGuide(AdminUserGuideForm userGuideForm);

    /**
     * 修改使用指南
     * @param userGuideForm
     * @return
     */
    Integer updateUserGuide(AdminUserGuideForm userGuideForm);

    /**
     * 删除使用指南
     * @param userGuideId
     * @return
     */
    Integer deleteUserGuide(Integer userGuideId);

    /**
     * 获取使用指南信息
     * @param userGuideId
     * @return
     */
    AdminUserGuideInfoVO getUserGuideInfo(Integer userGuideId);

    /**
     * 分页查询使用指南（支持按标题模糊查询）
     * @param size 分页大小
     * @param page 页码
     * @param userGuideQuery 查询条件
     * @return 分页结果
     */
    Page<AdminUserGuidePageVO> getUserGuidePage(int size, int page, AdminUserGuideQuery userGuideQuery);
}
