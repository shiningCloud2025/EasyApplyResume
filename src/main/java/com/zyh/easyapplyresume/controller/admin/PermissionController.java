package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.PermissionForm;
import com.zyh.easyapplyresume.model.query.admin.PermissionPageQuery;
import com.zyh.easyapplyresume.model.vo.admin.PermissionInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.PermissionPageVO;
import com.zyh.easyapplyresume.service.admin.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/permission")
@Tag(name="权限控制器-管理端")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @Operation(summary = "新增权限")
    @PostMapping("/add")
    public BaseResult<Integer> addPermission(@RequestBody PermissionForm permissionForm){
        return BaseResult.ok(permissionService.addPermission(permissionForm));
    }

    @Operation(summary = "修改权限")
    @PostMapping("/update")
    public BaseResult<Integer> updatePermission(@RequestBody PermissionForm permissionForm){
        return BaseResult.ok(permissionService.updatePermission(permissionForm));
    }

    @Operation(summary = "删除权限")
    @DeleteMapping("/delete")
    public BaseResult<Integer> deletePermission(@RequestParam(required = true,value = "permissionId") Integer permissionId){
        return BaseResult.ok(permissionService.deletePermission(permissionId));
    }

    @Operation(summary = "查询权限")
    @GetMapping("/findById")
    public BaseResult<PermissionInfoVO> findPermissionById(@RequestParam(required = true,value = "permissionId") Integer permissionId){
        return BaseResult.ok(permissionService.findPermissionById(permissionId));
    }

    @Operation(summary = "分页查询权限")
    @GetMapping("/findByPage")
    public BaseResult<Page<PermissionPageVO>> findPermissionByPage(@RequestParam(required = false,value = "pageNum",defaultValue = "1") Integer pageNum,
                                                                   @RequestParam(required = false,value = "pageSize",defaultValue = "10") Integer pageSize,
                                                                   @RequestBody PermissionPageQuery permissionPageQuery){
        return BaseResult.ok(permissionService.findPermissionByPage(pageNum,pageSize,permissionPageQuery));
    }

    @Operation(summary = "查看所有权限")
    @GetMapping("/findAll")
    public BaseResult<List<PermissionInfoVO>> findAllPermission(){
        return BaseResult.ok(permissionService.findAllPermission());
    }

}
