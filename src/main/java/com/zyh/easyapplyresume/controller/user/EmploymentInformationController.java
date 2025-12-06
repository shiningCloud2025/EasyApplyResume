package com.zyh.easyapplyresume.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.query.admin.EmploymentInformationQuery;
import com.zyh.easyapplyresume.model.vo.admin.EmploymentInformationInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.EmploymentInformationPageVO;
import com.zyh.easyapplyresume.service.admin.EmploymentInformationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 就业信息控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/employmentInformation")
@Tag(name = "就业信息控制器-用户端")
public class EmploymentInformationController {

    @Autowired
    private EmploymentInformationService employmentInformationService;

    @GetMapping("/getEmploymentInformationPage")
    @Operation(summary = "分页查询招聘信息信息")
    public BaseResult<Page<EmploymentInformationPageVO>> getEmploymentInformationPage(@RequestParam(required = false, defaultValue = "10") int size,
                                                                                      @RequestParam(required = false, defaultValue = "1") int page,
                                                                                      @RequestBody EmploymentInformationQuery employmentInformationQuery) {
        return BaseResult.ok(employmentInformationService.getEmploymentInformationPage(size, page, employmentInformationQuery));
    }

    @GetMapping("/getEmploymentInformationInfo")
    @Operation(summary = "获取招聘信息信息")
    public BaseResult<EmploymentInformationInfoVO> getEmploymentInformationInfo(@RequestParam(required = true,value = "employmentInformationId") Integer employmentInformationId) {
        return BaseResult.ok(employmentInformationService.getEmploymentInformationInfo(employmentInformationId));
    }



}
