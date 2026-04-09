package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.pojo.admin.CityMap;
import com.zyh.easyapplyresume.model.pojo.admin.ProvinceMap;
import com.zyh.easyapplyresume.model.query.admin.ProvinceMapQuery;
import com.zyh.easyapplyresume.model.vo.admin.ProvinceMapInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.ProvinceMapPageVO;
import com.zyh.easyapplyresume.service.admin.ProvinceMapAdminService;
import com.zyh.easyapplyresume.service.admin.ProvinceMapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 省份Map控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/provinceMap")
@Tag(name="省份Map控制器-管理端")
public class AdminProvinceMapController {
    @Autowired
    private ProvinceMapService provinceMapService;

    @Autowired
    private ProvinceMapAdminService provinceMapAdminService;

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

    @Operation(summary = "查询省份Map详情")
    @GetMapping("/findProvinceMapById")
    public BaseResult<ProvinceMapInfoVO> findProvinceMapById(
            @RequestParam(required = true, value = "provinceMapId") Integer provinceMapId) {
        return BaseResult.ok(provinceMapAdminService.findProvinceMapById(provinceMapId));
    }

    @Operation(summary = "分页查询省份Map")
    @PostMapping("/findProvinceMapByPage")
    public BaseResult<Page<ProvinceMapPageVO>> findProvinceMapByPage(
            @RequestParam(required = false, value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestBody ProvinceMapQuery provinceMapQuery) {
        return BaseResult.ok(provinceMapAdminService.findProvinceMapByPage(pageNum, pageSize, provinceMapQuery));
    }


}
