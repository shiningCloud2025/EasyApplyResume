package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.model.pojo.admin.AreaMap;
import com.zyh.easyapplyresume.model.pojo.admin.CityMap;
import com.zyh.easyapplyresume.service.admin.CityMapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 城市Map控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/cityMap")
@Tag(name="城市Map控制器-管理端")
public class CityMapController {
    @Autowired
    private CityMapService cityMapService;

    @Operation(summary = "获取所有城市信息")
    @GetMapping("/getAllCity")
    public List<CityMap> getAllCity(){
        return cityMapService.getAllCity();
    }

    @Operation(summary = "根据城市id获取所有区县")
    @GetMapping("/getAllAreaByCityId")
    public List<AreaMap> getAllAreaByCityId(@RequestParam (required = true,value = "cityId") Integer cityId){
        return cityMapService.getAllAreaByCityId(cityId);
    }
}
