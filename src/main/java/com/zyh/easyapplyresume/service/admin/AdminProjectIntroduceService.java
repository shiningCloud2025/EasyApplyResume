package com.zyh.easyapplyresume.service.admin;

import com.zyh.easyapplyresume.model.form.admin.AdminProjectIntroduceForm;
import com.zyh.easyapplyresume.model.vo.admin.AdminProjectIntroduceInfoVO;

/**
 * 项目介绍Service接口
 * @author shiningCloud2025
 */
public interface AdminProjectIntroduceService {

    /**
     * 添加项目介绍
     * @param projectIntroduceForm
     * @return
     */
    Integer addProjectIntroduce(AdminProjectIntroduceForm projectIntroduceForm);

    /**
     * 修改项目介绍
     * @param projectIntroduceForm
     * @return
     */
    Integer updateProjectIntroduce(AdminProjectIntroduceForm projectIntroduceForm);

    /**
     * 获取项目介绍信息
     * @return
     */
    AdminProjectIntroduceInfoVO getProjectIntroduceInfo();
}
