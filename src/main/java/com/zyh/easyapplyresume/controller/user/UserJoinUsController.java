package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.vo.admin.AdminJoinUsInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminJoinUsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 加入我们控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/joinUs")
@Tag(name = "加入我们控制器-用户端")
public class UserJoinUsController {
    @Autowired
    private AdminJoinUsService joinUsService;

    @Operation(summary = "获取加入我们信息")
    @RequestMapping("/getInfo")
    public BaseResult<AdminJoinUsInfoVO> getJoinUsInfo() {
        return BaseResult.ok(joinUsService.getJoinUsInfo());
    }
}
