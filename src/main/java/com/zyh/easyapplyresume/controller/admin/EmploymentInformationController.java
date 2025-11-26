package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.EmploymentInformationForm;
import com.zyh.easyapplyresume.model.query.admin.EmploymentInformationQuery;
import com.zyh.easyapplyresume.model.vo.admin.EmploymentInformationInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.EmploymentInformationPageVO;
import com.zyh.easyapplyresume.service.admin.EmploymentInformationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 招聘滋滋控制器-管理端
 * Controller层只负责调用服务和返回成功，不处理任何异常
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/employmentInformation")
@Tag(name = "招聘信息接口-管理端")
public class EmploymentInformationController {

    @Autowired
    private EmploymentInformationService employmentInformationService;

    @Operation(summary = "新增招聘信息")
    @RequestMapping("/addEmploymentInformation")
    public BaseResult<?> addEmploymentInformation(@RequestBody EmploymentInformationForm employmentInformationForm){
        return BaseResult.ok(employmentInformationService.addEmploymentInformation(employmentInformationForm));
    }

    @Operation(summary = "修改招聘信息")
    @RequestMapping("/updateEmploymentInformation")
    public BaseResult<?> updateEmploymentInformation(@RequestBody EmploymentInformationForm employmentInformationForm){
        return BaseResult.ok(employmentInformationService.updateEmploymentInformation(employmentInformationForm));
    }

    @Operation(summary = "删除招聘信息")
    @RequestMapping("/deleteEmploymentInformation")
    public BaseResult<?> deleteEmploymentInformation(@RequestBody EmploymentInformationForm employmentInformationForm){
        return BaseResult.ok(employmentInformationService.deleteEmploymentInformation(employmentInformationForm.getEmploymentInformationId()));
    }

    @Operation(summary = "根据id查询招聘信息信息")
    @RequestMapping("/getEmploymentInformationInfo")
    public BaseResult<EmploymentInformationInfoVO> getEmploymentInformationInfo(@RequestParam(required = true,value = "employmentInformationId")Integer employmentInformationId){
        return BaseResult.ok(employmentInformationService.getEmploymentInformationInfo(employmentInformationId));
    }

    @Operation(summary = "分页查询招聘信息信息")
    @RequestMapping("/getEmploymentInformationPage")
    public BaseResult<Page<EmploymentInformationPageVO>> getEmploymentInformationPage(@RequestParam(required = true,value = "pageNum")Integer pageNum,
                                                                                      @RequestParam(required = true,value = "pageSize")Integer pageSize,
                                                                                      @RequestBody EmploymentInformationQuery employmentInformationQuery){
        return BaseResult.ok(employmentInformationService.getEmploymentInformationPage(pageNum,pageSize,employmentInformationQuery));
    }

    @Operation(summary = "获取所有招聘信息信息")
    @RequestMapping("/getAllEmploymentInformation")
    public BaseResult<List<EmploymentInformationInfoVO>> getAllEmploymentInformation(){
        return BaseResult.ok(employmentInformationService.getAllEmploymentInformation());
    }



}
