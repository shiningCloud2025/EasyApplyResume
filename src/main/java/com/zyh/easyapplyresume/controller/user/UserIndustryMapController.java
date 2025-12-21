package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.vo.admin.IndustryMapInfoVO;
import com.zyh.easyapplyresume.service.admin.IndustryMapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 行业Map控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/industryMap")
@Tag(name="行业Map控制器-用户端")
public class UserIndustryMapController {

    @Autowired
    private IndustryMapService industryMapService;
    @Operation(summary = "查询所有行业Map")
    @GetMapping("/findAllIndustryMap")
    public BaseResult<List<IndustryMapInfoVO>> findAllIndustryMap(){
        return BaseResult.ok(industryMapService.findAllIndustryMap());
    }

}
