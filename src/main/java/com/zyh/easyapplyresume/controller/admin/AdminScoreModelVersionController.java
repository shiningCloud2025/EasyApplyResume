package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminScoreModelVersionForm;
import com.zyh.easyapplyresume.model.query.admin.AdminScoreModelVersionQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminScoreModelVersionInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminScoreModelVersionPageVO;
import com.zyh.easyapplyresume.service.admin.AdminScoreModelVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 简历评分模型版本控制器-管理端
 *
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/scoreModelVersion")
@Tag(name = "简历评分模型版本控制器-管理端")
public class AdminScoreModelVersionController {

    @Autowired
    private AdminScoreModelVersionService adminScoreModelVersionService;

    @Operation(summary = "新增模型版本")
    @PostMapping(value = "/addScoreModelVersion", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResult<Integer> addScoreModelVersion(
            @ModelAttribute AdminScoreModelVersionForm form,
            @RequestParam(required = true, value = "modelFile") MultipartFile modelFile) {
        return BaseResult.ok(adminScoreModelVersionService.addScoreModelVersion(form, modelFile));
    }

    @Operation(summary = "修改模型版本")
    @PostMapping(value = "/updateScoreModelVersion", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResult<Integer> updateScoreModelVersion(
            @ModelAttribute AdminScoreModelVersionForm form,
            @RequestParam(required = false, value = "modelFile") MultipartFile modelFile) {
        return BaseResult.ok(adminScoreModelVersionService.updateScoreModelVersion(form, modelFile));
    }

    @Operation(summary = "删除模型版本")
    @DeleteMapping("/deleteScoreModelVersion")
    public BaseResult<Integer> deleteScoreModelVersion(
            @RequestParam(required = true, value = "scoreModelVersionId") Integer scoreModelVersionId) {
        return BaseResult.ok(adminScoreModelVersionService.deleteScoreModelVersion(scoreModelVersionId));
    }

    @Operation(summary = "查询模型版本详情")
    @GetMapping("/findScoreModelVersionById")
    public BaseResult<AdminScoreModelVersionInfoVO> findScoreModelVersionById(
            @RequestParam(required = true, value = "scoreModelVersionId") Integer scoreModelVersionId) {
        return BaseResult.ok(adminScoreModelVersionService.findScoreModelVersionById(scoreModelVersionId));
    }

    @Operation(summary = "分页查询模型版本")
    @PostMapping("/findScoreModelVersionByPage")
    public BaseResult<Page<AdminScoreModelVersionPageVO>> findScoreModelVersionByPage(
            @RequestParam(required = false, value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestBody AdminScoreModelVersionQuery query) {
        return BaseResult.ok(adminScoreModelVersionService.findScoreModelVersionByPage(pageNum, pageSize, query));
    }

    @Operation(summary = "查询所有模型版本")
    @GetMapping("/findAllScoreModelVersion")
    public BaseResult<List<AdminScoreModelVersionPageVO>> findAllScoreModelVersion() {
        return BaseResult.ok(adminScoreModelVersionService.findAllScoreModelVersion());
    }
}
