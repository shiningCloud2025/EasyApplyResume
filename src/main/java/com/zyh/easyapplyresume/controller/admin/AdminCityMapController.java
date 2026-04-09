package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.pojo.admin.AreaMap;
import com.zyh.easyapplyresume.model.pojo.admin.CityMap;
import com.zyh.easyapplyresume.model.query.admin.CityMapQuery;
import com.zyh.easyapplyresume.model.vo.admin.CityMapInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.CityMapPageVO;
import com.zyh.easyapplyresume.service.admin.CityMapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 城市Map控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/cityMap")
@Tag(name="城市Map控制器-管理端")
public class AdminCityMapController {
    @Autowired
    private CityMapService cityMapService;

    @Operation(summary = "获取所有城市信息")
    @GetMapping("/getAllCity")
    public List<CityMap> getAllCity(){
        return cityMapService.getAllCity();
    }

    @Operation(summary = "根据城市id获取所有区县")
    @GetMapping("/getAllAreaByCityId")
    public List<AreaMap> getAllAreaByCityId(@RequestParam(required = true,value = "cityId") Integer cityId){
        return cityMapService.getAllAreaByCityId(cityId);
    }

    @Operation(summary = "查询城市Map详情")
    @GetMapping("/findCityMapById")
    public BaseResult<CityMapInfoVO> findCityMapById(
            @RequestParam(required = true, value = "cityMapId") Integer cityMapId) {
        return BaseResult.ok(cityMapService.findCityMapById(cityMapId));
    }

    @Operation(summary = "分页查询城市Map")
    @PostMapping("/findCityMapByPage")
    public BaseResult<Page<CityMapPageVO>> findCityMapByPage(
            @RequestParam(required = false, value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestBody CityMapQuery cityMapQuery) {
        return BaseResult.ok(cityMapService.findCityMapByPage(pageNum, pageSize, cityMapQuery));
    }
}
