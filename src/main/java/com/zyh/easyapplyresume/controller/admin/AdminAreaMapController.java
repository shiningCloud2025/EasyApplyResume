package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.pojo.admin.AreaMap;
import com.zyh.easyapplyresume.model.pojo.admin.StreetMap;
import com.zyh.easyapplyresume.model.query.admin.AreaMapQuery;
import com.zyh.easyapplyresume.model.vo.admin.AreaMapInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AreaMapPageVO;
import com.zyh.easyapplyresume.service.admin.AreaMapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 区县Map控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/areaMap")
@Tag(name="区县Map控制器-管理端")
public class AdminAreaMapController {
    @Autowired
    private AreaMapService areaMapService;

    @Operation(summary = "获取所有区县信息")
    @GetMapping("/getAllArea")
    public List<AreaMap> getAllArea(){
        return areaMapService.getAllArea();
    }

    @Operation(summary = "根据区县id获取所有街道")
    @GetMapping("/getStreetByAreaId")
    public List<StreetMap> getStreetByAreaId(@RequestParam(required = true, value = "areaMapId") Integer areaMapId){
        return areaMapService.getStreetByAreaId(areaMapId);
    }

    @Operation(summary = "查询区县Map详情")
    @GetMapping("/findAreaMapById")
    @PreAuthorize("hasAuthority('/admin/areaMap/findAreaMapById')")
    public BaseResult<AreaMapInfoVO> findAreaMapById(
            @RequestParam(required = true, value = "areaMapId") Integer areaMapId) {
        return BaseResult.ok(areaMapService.findAreaMapById(areaMapId));
    }

    @Operation(summary = "分页查询区县Map")
    @PostMapping("/findAreaMapByPage")
    @PreAuthorize("hasAuthority('/admin/areaMap/findAreaMapByPage')")
    public BaseResult<Page<AreaMapPageVO>> findAreaMapByPage(
            @RequestParam(required = false, value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestBody AreaMapQuery areaMapQuery) {
        return BaseResult.ok(areaMapService.findAreaMapByPage(pageNum, pageSize, areaMapQuery));
    }
}
