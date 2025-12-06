package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.pojo.user.UniversityMap;
import com.zyh.easyapplyresume.service.user.UniversityMapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 大学Map控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/universityMap")
@Tag(name = "大学Map控制器-用户端")
public class UniversityMapController {

    @Autowired
    private UniversityMapService universityMapService;

    @Operation(summary = "获取所有的大学")
    @GetMapping("/getAllUniversityMap")
    public BaseResult<List<UniversityMap>> getAllUniversityMap(){
        return BaseResult.ok(universityMapService.getAllUniversityMap());
    }

    @Operation(summary = "模糊查询所有的大学")
    @GetMapping("/getAllUniversityMapByName")
    public BaseResult<List<UniversityMap>> getAllUniversityMapByName(@RequestParam String universityMapName){
        return BaseResult.ok(universityMapService.getAllUniversityMapByName(universityMapName));
    }
}
