package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.vo.admin.AdminPartnerIntroduceInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminPartnerIntroduceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 合作伙伴控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/partnerIntroduce")
@Tag(name = "合作伙伴控制器-用户端")
public class UserPartnerIntroduceController {
    @Autowired
    private AdminPartnerIntroduceService partnerIntroduceService;

    @Operation(summary = "获取合作伙伴信息")
    @GetMapping("/getInfo")
    public BaseResult<AdminPartnerIntroduceInfoVO> getPartnerIntroduceInfo() {
        return BaseResult.ok(partnerIntroduceService.getPartnerIntroduceInfo());
    }
}
