package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.pojo.admin.AdminPartnerIntroduce;
import com.zyh.easyapplyresume.service.admin.AdminPartnerIntroduceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 合作伙伴控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/partnerIntroduce")
@Tag(name = "合作伙伴控制器-管理端")
public class AdminPartnerIntroduceController {
    @Autowired
    private AdminPartnerIntroduceService partnerIntroduceService;

    @Operation(summary = "添加合作伙伴")
    @RequestMapping("/add")
    public BaseResult<Integer> addPartnerIntroduce(@RequestBody AdminPartnerIntroduce partnerIntroduce) {
        return BaseResult.ok(partnerIntroduceService.addPartnerIntroduce(partnerIntroduce));
    }

    @Operation(summary = "修改合作伙伴")
    @RequestMapping("/update")
    public BaseResult<Integer> updatePartnerIntroduce(@RequestBody AdminPartnerIntroduce partnerIntroduce) {
        return BaseResult.ok(partnerIntroduceService.updatePartnerIntroduce(partnerIntroduce));
    }

    @Operation(summary = "获取合作伙伴信息")
    @RequestMapping("/getInfo")
    public BaseResult<AdminPartnerIntroduce> getPartnerIntroduceInfo() {
        return BaseResult.ok(partnerIntroduceService.getPartnerIntroduceInfo());
    }
}
