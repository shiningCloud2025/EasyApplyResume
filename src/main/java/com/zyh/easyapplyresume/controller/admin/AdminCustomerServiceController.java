package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminCustomerServiceForm;
import com.zyh.easyapplyresume.model.vo.admin.AdminCustomerServiceInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminCustomerServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('/admin/customerService/add')")
    public BaseResult<Integer> addCustomerService(@RequestBody AdminCustomerServiceForm customerServiceForm) {
        return BaseResult.ok(customerServiceService.addCustomerService(customerServiceForm));
    }

    @Operation(summary = "修改人工客服")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('/admin/customerService/update')")
    public BaseResult<Integer> updateCustomerService(@RequestBody AdminCustomerServiceForm customerServiceForm) {
        return BaseResult.ok(customerServiceService.updateCustomerService(customerServiceForm));
    }

    @Operation(summary = "获取人工客服信息")
    @GetMapping("/getInfo")
    @PreAuthorize("hasAuthority('/admin/customerService/getInfo')")
    public BaseResult<AdminCustomerServiceInfoVO> getCustomerServiceInfo() {
        return BaseResult.ok(customerServiceService.getCustomerServiceInfo());
    }
}
