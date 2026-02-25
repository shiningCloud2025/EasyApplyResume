package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.pojo.admin.AdminDevelopHistory;
import com.zyh.easyapplyresume.service.admin.AdminDevelopHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发展历程控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/developHistory")
@Tag(name = "发展历程控制器-管理端")
public class AdminDevelopHistoryController {
    @Autowired
    private AdminDevelopHistoryService developHistoryService;

    @Operation(summary = "添加发展历程")
    @RequestMapping("/add")
    public BaseResult<Integer> addDevelopHistory(@RequestBody AdminDevelopHistory developHistory) {
        return BaseResult.ok(developHistoryService.addDevelopHistory(developHistory));
    }

    @Operation(summary = "修改发展历程")
    @RequestMapping("/update")
    public BaseResult<Integer> updateDevelopHistory(@RequestBody AdminDevelopHistory developHistory) {
        return BaseResult.ok(developHistoryService.updateDevelopHistory(developHistory));
    }

    @Operation(summary = "获取发展历程信息")
    @RequestMapping("/getInfo")
    public BaseResult<AdminDevelopHistory> getDevelopHistoryInfo() {
        return BaseResult.ok(developHistoryService.getDevelopHistoryInfo());
    }
}
