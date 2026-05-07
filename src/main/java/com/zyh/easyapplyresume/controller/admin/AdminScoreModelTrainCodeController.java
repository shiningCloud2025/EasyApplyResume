package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminScoreModelTrainCodeForm;
import com.zyh.easyapplyresume.model.query.admin.AdminScoreModelTrainCodeQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminScoreModelTrainCodeInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminScoreModelTrainCodePageVO;
import com.zyh.easyapplyresume.service.admin.AdminScoreModelTrainCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 简历评分模型训练代码控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/scoreModelTrainCode")
@Tag(name = "简历评分模型训练代码控制器-管理端")
public class AdminScoreModelTrainCodeController {

    @Autowired
    private AdminScoreModelTrainCodeService adminScoreModelTrainCodeService;

    @Operation(summary = "新增训练代码")
    @PostMapping("/addScoreModelTrainCode")
    @PreAuthorize("hasAuthority('/admin/scoreModelTrainCode/addScoreModelTrainCode')")
    public BaseResult<Integer> addScoreModelTrainCode(@RequestBody AdminScoreModelTrainCodeForm form) {
        return BaseResult.ok(adminScoreModelTrainCodeService.addScoreModelTrainCode(form));
    }

    @Operation(summary = "修改训练代码")
    @PostMapping("/updateScoreModelTrainCode")
    @PreAuthorize("hasAuthority('/admin/scoreModelTrainCode/updateScoreModelTrainCode')")
    public BaseResult<Integer> updateScoreModelTrainCode(@RequestBody AdminScoreModelTrainCodeForm form) {
        return BaseResult.ok(adminScoreModelTrainCodeService.updateScoreModelTrainCode(form));
    }

    @Operation(summary = "删除训练代码")
    @DeleteMapping("/deleteScoreModelTrainCode")
    @PreAuthorize("hasAuthority('/admin/scoreModelTrainCode/deleteScoreModelTrainCode')")
    public BaseResult<Integer> deleteScoreModelTrainCode(
            @RequestParam(required = true, value = "scoreModelTrainCodeId") Integer scoreModelTrainCodeId) {
        return BaseResult.ok(adminScoreModelTrainCodeService.deleteScoreModelTrainCode(scoreModelTrainCodeId));
    }

    @Operation(summary = "查询训练代码详情")
    @GetMapping("/findScoreModelTrainCodeById")
    @PreAuthorize("hasAuthority('/admin/scoreModelTrainCode/findScoreModelTrainCodeById')")
    public BaseResult<AdminScoreModelTrainCodeInfoVO> findScoreModelTrainCodeById(
            @RequestParam(required = true, value = "scoreModelTrainCodeId") Integer scoreModelTrainCodeId) {
        return BaseResult.ok(adminScoreModelTrainCodeService.findScoreModelTrainCodeById(scoreModelTrainCodeId));
    }

    @Operation(summary = "分页查询训练代码")
    @PostMapping("/findScoreModelTrainCodeByPage")
    @PreAuthorize("hasAuthority('/admin/scoreModelTrainCode/findScoreModelTrainCodeByPage')")
    public BaseResult<Page<AdminScoreModelTrainCodePageVO>> findScoreModelTrainCodeByPage(
            @RequestParam(required = false, value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestBody AdminScoreModelTrainCodeQuery query) {
        return BaseResult.ok(adminScoreModelTrainCodeService.findScoreModelTrainCodeByPage(pageNum, pageSize, query));
    }

    @Operation(summary = "查询所有训练代码")
    @GetMapping("/findAllScoreModelTrainCode")
    public BaseResult<List<AdminScoreModelTrainCodePageVO>> findAllScoreModelTrainCode() {
        return BaseResult.ok(adminScoreModelTrainCodeService.findAllScoreModelTrainCode());
    }

    @Operation(summary = "下载训练代码压缩包")
    @GetMapping("/downloadScoreModelTrainCode")
    @PreAuthorize("hasAuthority('/admin/scoreModelTrainCode/downloadScoreModelTrainCode')")
    public void downloadScoreModelTrainCode(
            @RequestParam(required = true, value = "scoreModelTrainCodeId") Integer scoreModelTrainCodeId,
            HttpServletResponse response) {
        adminScoreModelTrainCodeService.downloadScoreModelTrainCode(scoreModelTrainCodeId, response);
    }
}
