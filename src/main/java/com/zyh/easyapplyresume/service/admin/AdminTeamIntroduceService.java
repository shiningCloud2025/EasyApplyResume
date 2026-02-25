package com.zyh.easyapplyresume.service.admin;

import com.zyh.easyapplyresume.model.form.admin.AdminTeamIntroduceForm;
import com.zyh.easyapplyresume.model.vo.admin.AdminTeamIntroduceInfoVO;

/**
 * 团队介绍Service接口
 * @author shiningCloud2025
 */
public interface AdminTeamIntroduceService {

    /**
     * 添加团队介绍
     * @param teamIntroduceForm
     * @return
     */
    Integer addTeamIntroduce(AdminTeamIntroduceForm teamIntroduceForm);

    /**
     * 修改团队介绍
     * @param teamIntroduceForm
     * @return
     */
    Integer updateTeamIntroduce(AdminTeamIntroduceForm teamIntroduceForm);

    /**
     * 获取团队介绍信息
     * @return
     */
    AdminTeamIntroduceInfoVO getTeamIntroduceInfo();
}
