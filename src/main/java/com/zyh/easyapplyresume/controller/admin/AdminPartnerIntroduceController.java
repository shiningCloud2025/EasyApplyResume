package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminPartnerIntroduceForm;
import com.zyh.easyapplyresume.model.vo.admin.AdminPartnerIntroduceInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminPartnerIntroduceService;
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
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('/admin/partnerIntroduce/add')")
    public BaseResult<Integer> addPartnerIntroduce(@RequestBody AdminPartnerIntroduceForm partnerIntroduceForm) {
        return BaseResult.ok(partnerIntroduceService.addPartnerIntroduce(partnerIntroduceForm));
    }

    @Operation(summary = "修改合作伙伴")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('/admin/partnerIntroduce/update')")
    public BaseResult<Integer> updatePartnerIntroduce(@RequestBody AdminPartnerIntroduceForm partnerIntroduceForm) {
        return BaseResult.ok(partnerIntroduceService.updatePartnerIntroduce(partnerIntroduceForm));
    }

    @Operation(summary = "获取合作伙伴信息")
    @GetMapping("/getInfo")
    @PreAuthorize("hasAuthority('/admin/partnerIntroduce/getInfo')")
    public BaseResult<AdminPartnerIntroduceInfoVO> getPartnerIntroduceInfo() {
        return BaseResult.ok(partnerIntroduceService.getPartnerIntroduceInfo());
    }
}
