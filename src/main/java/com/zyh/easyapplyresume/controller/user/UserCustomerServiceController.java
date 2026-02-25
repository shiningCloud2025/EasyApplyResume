package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.pojo.admin.AdminCustomerService;
import com.zyh.easyapplyresume.service.admin.AdminCustomerServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 人工客服控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/customerService")
@Tag(name = "人工客服控制器-用户端")
public class UserCustomerServiceController {
    @Autowired
    private AdminCustomerServiceService customerServiceService;

    @Operation(summary = "获取人工客服信息")
    @RequestMapping("/getInfo")
    public BaseResult<AdminCustomerService> getCustomerServiceInfo() {
        return BaseResult.ok(customerServiceService.getCustomerServiceInfo());
    }
}
