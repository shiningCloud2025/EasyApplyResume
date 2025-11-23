package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.admin.IndustryMapForm;
import com.zyh.easyapplyresume.model.query.admin.IndustryMapQuery;
import com.zyh.easyapplyresume.model.vo.admin.IndustryMapInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.IndustryMapPageVO;
import com.zyh.easyapplyresume.service.admin.IndustryMapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping("/addIndustryMap")
    public void addIndustryMap(IndustryMapForm industryMapForm){

    }

    @Operation(summary = "修改行业Map")
    @RequestMapping("/updateIndustryMap")
    public void updateIndustryMap(IndustryMapForm industryMapForm){

    }

    @Operation(summary = "查询行业Map")
    @RequestMapping("/findIndustryMapById")
    public IndustryMapInfoVO findIndustryMapById(Integer industryMapId){

    }

    @Operation(summary = "分页查询")
    @RequestMapping("/findIndustryMapByPage")
    public Page<IndustryMapPageVO> findIndustryMapByPage(Integer pageNum, Integer pageSize, IndustryMapQuery industryMapQuery){

    }

    @Operation(summary = "查询所有行业Map")
    @RequestMapping("/findAllIndustryMap")
    public List<IndustryMapInfoVO> findAllIndustryMap(){

    }
}
