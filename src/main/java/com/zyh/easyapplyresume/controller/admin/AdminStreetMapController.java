package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.pojo.admin.StreetMap;
import com.zyh.easyapplyresume.model.query.admin.StreetMapQuery;
import com.zyh.easyapplyresume.model.vo.admin.StreetMapInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.StreetMapPageVO;
import com.zyh.easyapplyresume.service.admin.StreetMapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 街道Map控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/streetMap")
@Tag(name="街道Map控制器-管理端")
public class AdminStreetMapController {
    @Autowired
    private StreetMapService streetMapService;

    @Operation(summary = "获取所有街道信息")
    @GetMapping("/getAllStreet")
    public List<StreetMap> getAllStreet(){
        return streetMapService.getAllStreet();
    }

    @Operation(summary = "查询街道Map详情")
    @GetMapping("/findStreetMapById")
    public BaseResult<StreetMapInfoVO> findStreetMapById(
            @RequestParam(required = true, value = "streetMapId") Integer streetMapId) {
        return BaseResult.ok(streetMapService.findStreetMapById(streetMapId));
    }

    @Operation(summary = "分页查询街道Map")
    @PostMapping("/findStreetMapByPage")
    public BaseResult<Page<StreetMapPageVO>> findStreetMapByPage(
            @RequestParam(required = false, value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestBody StreetMapQuery streetMapQuery) {
        return BaseResult.ok(streetMapService.findStreetMapByPage(pageNum, pageSize, streetMapQuery));
    }
}
