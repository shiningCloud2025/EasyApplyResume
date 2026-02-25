package com.zyh.easyapplyresume.service.admin;

import com.zyh.easyapplyresume.model.form.admin.AdminPartnerIntroduceForm;
import com.zyh.easyapplyresume.model.vo.admin.AdminPartnerIntroduceInfoVO;

/**
 * 合作伙伴Service接口
 * @author shiningCloud2025
 */
public interface AdminPartnerIntroduceService {

    /**
     * 添加合作伙伴
     * @param partnerIntroduceForm
     * @return
     */
    Integer addPartnerIntroduce(AdminPartnerIntroduceForm partnerIntroduceForm);

    /**
     * 修改合作伙伴
     * @param partnerIntroduceForm
     * @return
     */
    Integer updatePartnerIntroduce(AdminPartnerIntroduceForm partnerIntroduceForm);

    /**
     * 获取合作伙伴信息
     * @return
     */
    AdminPartnerIntroduceInfoVO getPartnerIntroduceInfo();
}
