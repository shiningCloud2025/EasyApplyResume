package com.zyh.easyapplyresume.service.admin;

import com.zyh.easyapplyresume.model.form.admin.AdminCustomerServiceForm;
import com.zyh.easyapplyresume.model.vo.admin.AdminCustomerServiceInfoVO;

/**
 * 人工客服Service接口
 * @author shiningCloud2025
 */
public interface AdminCustomerServiceService {

    /**
     * 添加人工客服
     * @param customerServiceForm
     * @return
     */
    Integer addCustomerService(AdminCustomerServiceForm customerServiceForm);

    /**
     * 修改人工客服
     * @param customerServiceForm
     * @return
     */
    Integer updateCustomerService(AdminCustomerServiceForm customerServiceForm);

    /**
     * 获取人工客服信息
     * @return
     */
    AdminCustomerServiceInfoVO getCustomerServiceInfo();
}
