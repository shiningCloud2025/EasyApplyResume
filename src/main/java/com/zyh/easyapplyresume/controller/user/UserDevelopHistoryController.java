package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.vo.admin.AdminDevelopHistoryInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminDevelopHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发展历程控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/developHistory")
@Tag(name = "发展历程控制器-用户端")
public class UserDevelopHistoryController {
    @Autowired
    private AdminDevelopHistoryService developHistoryService;

    @Operation(summary = "获取发展历程信息")
    @RequestMapping("/getInfo")
    public BaseResult<AdminDevelopHistoryInfoVO> getDevelopHistoryInfo() {
        return BaseResult.ok(developHistoryService.getDevelopHistoryInfo());
    }
}
