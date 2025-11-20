package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.model.pojo.admin.StreetMap;
import com.zyh.easyapplyresume.service.admin.StreetMapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 街道Map控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/streetMap")
@Tag(name="街道Map控制器-管理端")
public class StreetMapController {
    @Autowired
    private StreetMapService streetMapService;

    @Operation(summary = "获取所有街道信息")
    @GetMapping("/getAllStreet")
    public List<StreetMap> getAllStreet(){
        return streetMapService.getAllStreet();
    }

}
