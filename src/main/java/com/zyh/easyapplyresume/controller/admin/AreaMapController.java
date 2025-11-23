package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.model.pojo.admin.AreaMap;
import com.zyh.easyapplyresume.model.pojo.admin.StreetMap;
import com.zyh.easyapplyresume.service.admin.AreaMapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 区县Map控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/areaMap")
@Tag(name="区县Map控制器-管理端")
public class AreaMapController {
    @Autowired
    private AreaMapService areaMapService;

    @Operation(summary = "获取所有区县信息")
    @GetMapping("/getAllArea")
    public List<AreaMap> getAllArea(){
        return areaMapService.getAllArea();
    }

    @Operation(summary = "根据区县id获取所有街道")
    @GetMapping("/getStreetByAreaId")
    public List<StreetMap> getStreetByAreaId(@RequestParam (required = true,value = "areaMapId") Integer areaMapId){
        return areaMapService.getStreetByAreaId(areaMapId);
    }




}
