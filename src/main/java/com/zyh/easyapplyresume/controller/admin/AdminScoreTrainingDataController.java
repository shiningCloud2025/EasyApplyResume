package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminScoreTrainingDataForm;
import com.zyh.easyapplyresume.model.query.admin.AdminScoreTrainingDataQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminScoreTrainingDataInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminScoreTrainingDataPageVO;
import com.zyh.easyapplyresume.service.admin.AdminScoreTrainingDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 简历评分训练数据控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/scoreTrainingData")
@Tag(name = "简历评分训练数据控制器-管理端")
public class AdminScoreTrainingDataController {

    @Autowired
    private AdminScoreTrainingDataService adminScoreTrainingDataService;

    @Operation(summary = "新增简历评分训练数据")
    @PostMapping("/addScoreTrainingData")
    public BaseResult<Integer> addScoreTrainingData(@RequestBody AdminScoreTrainingDataForm form) {
        return BaseResult.ok(adminScoreTrainingDataService.addScoreTrainingData(form));
    }

    @Operation(summary = "删除简历评分训练数据")
    @DeleteMapping("/deleteScoreTrainingData")
    public BaseResult<Integer> deleteScoreTrainingData(
            @RequestParam(required = true, value = "scoreTrainingDataId") Integer scoreTrainingDataId) {
        return BaseResult.ok(adminScoreTrainingDataService.deleteScoreTrainingData(scoreTrainingDataId));
    }

    @Operation(summary = "查询简历评分训练数据详情")
    @GetMapping("/findScoreTrainingDataById")
    public BaseResult<AdminScoreTrainingDataInfoVO> findScoreTrainingDataById(
            @RequestParam(required = true, value = "scoreTrainingDataId") Integer scoreTrainingDataId) {
        return BaseResult.ok(adminScoreTrainingDataService.findScoreTrainingDataById(scoreTrainingDataId));
    }

    @Operation(summary = "分页查询简历评分训练数据")
    @PostMapping("/findScoreTrainingDataByPage")
    public BaseResult<Page<AdminScoreTrainingDataPageVO>> findScoreTrainingDataByPage(
            @RequestParam(required = false, value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestBody AdminScoreTrainingDataQuery query) {
        return BaseResult.ok(adminScoreTrainingDataService.findScoreTrainingDataByPage(pageNum, pageSize, query));
    }

    @Operation(summary = "查询所有简历评分训练数据")
    @GetMapping("/findAllScoreTrainingData")
    public BaseResult<List<AdminScoreTrainingDataPageVO>> findAllScoreTrainingData() {
        return BaseResult.ok(adminScoreTrainingDataService.findAllScoreTrainingData());
    }

    @Operation(summary = "导出简历评分训练数据")
    @PostMapping("/exportScoreTrainingData")
    public void exportScoreTrainingData(@RequestBody AdminScoreTrainingDataQuery query, HttpServletResponse response) {
        adminScoreTrainingDataService.exportScoreTrainingData(query, response);
    }
}
