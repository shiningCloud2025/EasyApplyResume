package com.zyh.easyapplyresume.service.admin;

import com.zyh.easyapplyresume.model.form.admin.AdminJoinUsForm;
import com.zyh.easyapplyresume.model.vo.admin.AdminJoinUsInfoVO;

/**
 * 加入我们Service接口
 * @author shiningCloud2025
 */
public interface AdminJoinUsService {

    /**
     * 添加加入我们
     * @param joinUsForm
     * @return
     */
    Integer addJoinUs(AdminJoinUsForm joinUsForm);

    /**
     * 修改加入我们
     * @param joinUsForm
     * @return
     */
    Integer updateJoinUs(AdminJoinUsForm joinUsForm);

    /**
     * 获取加入我们信息
     * @return
     */
    AdminJoinUsInfoVO getJoinUsInfo();
}
