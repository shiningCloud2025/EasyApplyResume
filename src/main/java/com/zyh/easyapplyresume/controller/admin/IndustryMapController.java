package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.IndustryMapForm;
import com.zyh.easyapplyresume.model.query.admin.IndustryMapQuery;
import com.zyh.easyapplyresume.model.vo.admin.IndustryMapInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.IndustryMapPageVO;
import com.zyh.easyapplyresume.service.admin.IndustryMapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 行业Map控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/industryMap")
@Tag(name="行业Map控制器-管理端")
public class IndustryMapController {

    @Autowired
    private IndustryMapService industryMapService;

    @Operation(summary = "新增行业Map")
    @PostMapping("/addIndustryMap")
    public BaseResult<Integer> addIndustryMap(@RequestBody IndustryMapForm industryMapForm){
        return BaseResult.ok(industryMapService.addIndustryMap(industryMapForm));
    }

    @Operation(summary = "修改行业Map")
    @PostMapping("/updateIndustryMap")
    public BaseResult<Integer>  updateIndustryMap(@RequestBody IndustryMapForm industryMapForm){
        return BaseResult.ok(industryMapService.updateIndustryMap(industryMapForm));
    }

    @Operation(summary = "查询行业Map")
    @GetMapping("/findIndustryMapById")
    public BaseResult<IndustryMapInfoVO> findIndustryMapById(@RequestParam(required = true,value = "industryMapId") Integer industryMapId){
        return BaseResult.ok(industryMapService.findIndustryMapById(industryMapId));
    }

    @Operation(summary = "分页查询")
    @PostMapping("/findIndustryMapByPage")
    public BaseResult<Page<IndustryMapPageVO>> findIndustryMapByPage(@RequestParam (required = false,value = "pageNum",defaultValue = "1")Integer pageNum,
                                                                     @RequestParam(required = false,value = "pageSize",defaultValue = "10")  Integer pageSize,
                                                                     @RequestBody IndustryMapQuery industryMapQuery){
        return BaseResult.ok(industryMapService.findIndustryMapByPage(pageNum,pageSize,industryMapQuery));
    }

    @Operation(summary = "查询所有行业Map")
    @GetMapping("/findAllIndustryMap")
    public BaseResult<List<IndustryMapInfoVO>> findAllIndustryMap(){
        return BaseResult.ok(industryMapService.findAllIndustryMap());
    }
}
