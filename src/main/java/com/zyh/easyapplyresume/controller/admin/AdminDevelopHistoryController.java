package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminDevelopHistoryForm;
import com.zyh.easyapplyresume.model.vo.admin.AdminDevelopHistoryInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminDevelopHistoryService;
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
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('/admin/developHistory/add')")
    public BaseResult<Integer> addDevelopHistory(@RequestBody AdminDevelopHistoryForm developHistoryForm) {
        return BaseResult.ok(developHistoryService.addDevelopHistory(developHistoryForm));
    }

    @Operation(summary = "修改发展历程")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('/admin/developHistory/update')")
    public BaseResult<Integer> updateDevelopHistory(@RequestBody AdminDevelopHistoryForm developHistoryForm) {
        return BaseResult.ok(developHistoryService.updateDevelopHistory(developHistoryForm));
    }

    @Operation(summary = "获取发展历程信息")
    @GetMapping("/getInfo")
    @PreAuthorize("hasAuthority('/admin/developHistory/getInfo')")
    public BaseResult<AdminDevelopHistoryInfoVO> getDevelopHistoryInfo() {
        return BaseResult.ok(developHistoryService.getDevelopHistoryInfo());
    }
}
