package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.model.pojo.admin.CityMap;
import com.zyh.easyapplyresume.model.pojo.admin.ProvinceMap;
import com.zyh.easyapplyresume.service.admin.ProvinceMapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 省份Map控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/provinceMap")
@Tag(name="省份Map控制器-管理端")
public class ProvinceMapController {
    @Autowired
    private ProvinceMapService provinceMapService;

    @Operation(summary = "获取所有省份信息")
    @GetMapping("/getAllProvince")
    public List<ProvinceMap> getAllProvince(){
        return provinceMapService.getAllProvince();
    }

    @Operation(summary = "根据省份id获取所有市")
    @GetMapping("/getCityByProvinceId")
    public List<CityMap> getCityByProvinceId(@RequestParam(required = true,value = "provinceMapId") Integer provinceMapId){
        return provinceMapService.getCityByProvinceId(provinceMapId);
    }


}
