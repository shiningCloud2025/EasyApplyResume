package com.zyh.easyapplyresume.service.admin;

import com.zyh.easyapplyresume.model.form.admin.AdminDevelopHistoryForm;
import com.zyh.easyapplyresume.model.vo.admin.AdminDevelopHistoryInfoVO;

/**
 * 发展历程Service接口
 * @author shiningCloud2025
 */
public interface AdminDevelopHistoryService {

    /**
     * 添加发展历程
     * @param developHistoryForm
     * @return
     */
    Integer addDevelopHistory(AdminDevelopHistoryForm developHistoryForm);

    /**
     * 修改发展历程
     * @param developHistoryForm
     * @return
     */
    Integer updateDevelopHistory(AdminDevelopHistoryForm developHistoryForm);

    /**
     * 获取发展历程信息
     * @return
     */
    AdminDevelopHistoryInfoVO getDevelopHistoryInfo();
}
