package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.pojo.admin.AdminCustomerService;
import com.zyh.easyapplyresume.service.admin.AdminCustomerServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 人工客服控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/customerService")
@Tag(name = "人工客服控制器-管理端")
public class AdminCustomerServiceController {
    @Autowired
    private AdminCustomerServiceService customerServiceService;

    @Operation(summary = "添加人工客服")
    @RequestMapping("/add")
    public BaseResult<Integer> addCustomerService(@RequestBody AdminCustomerService customerService) {
        return BaseResult.ok(customerServiceService.addCustomerService(customerService));
    }

    @Operation(summary = "修改人工客服")
    @RequestMapping("/update")
    public BaseResult<Integer> updateCustomerService(@RequestBody AdminCustomerService customerService) {
        return BaseResult.ok(customerServiceService.updateCustomerService(customerService));
    }

    @Operation(summary = "获取人工客服信息")
    @RequestMapping("/getInfo")
    public BaseResult<AdminCustomerService> getCustomerServiceInfo() {
        return BaseResult.ok(customerServiceService.getCustomerServiceInfo());
    }
}
