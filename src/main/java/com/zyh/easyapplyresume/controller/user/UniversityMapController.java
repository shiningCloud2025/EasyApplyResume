package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.service.user.UniversityMapService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/getAllUniversityMap")
    public

}
