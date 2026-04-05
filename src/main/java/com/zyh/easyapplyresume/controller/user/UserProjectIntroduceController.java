package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.vo.admin.AdminProjectIntroduceInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminProjectIntroduceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目介绍控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/projectIntroduce")
@Tag(name = "项目介绍控制器-用户端")
public class UserProjectIntroduceController {
    @Autowired
    private AdminProjectIntroduceService projectIntroduceService;

    @Operation(summary = "获取项目介绍信息")
    @GetMapping("/getInfo")
    public BaseResult<AdminProjectIntroduceInfoVO> getProjectIntroduceInfo() {
        return BaseResult.ok(projectIntroduceService.getProjectIntroduceInfo());
    }
}
