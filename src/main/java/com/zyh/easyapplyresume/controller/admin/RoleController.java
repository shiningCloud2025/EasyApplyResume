package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.RoleForm;
import com.zyh.easyapplyresume.model.query.admin.RolePageQuery;
import com.zyh.easyapplyresume.model.vo.admin.PermissionInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.RoleInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.RolePageVO;
import com.zyh.easyapplyresume.service.admin.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/role")
@Tag(name="角色控制器-管理端")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Operation(summary = "新增角色")
    @PostMapping("/add")
    public BaseResult<Integer> addRole(@RequestBody RoleForm roleForm){
        return BaseResult.ok(roleService.addRole(roleForm));
    }

    @Operation(summary = "修改角色")
    @PostMapping("/update")
    public BaseResult<Integer> updateRole(@RequestBody RoleForm roleForm){
        return BaseResult.ok(roleService.updateRole(roleForm));
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/delete")
    public BaseResult<Integer> deleteRole(@RequestParam(required = true,value = "roleId") Integer roleId){
        return BaseResult.ok(roleService.deleteRole(roleId));
    }

    @Operation(summary = "查询角色")
    @GetMapping("/findById")
    public BaseResult<RoleInfoVO> findRoleById(@RequestParam(required = true,value = "roleId") Integer roleId){
        return BaseResult.ok(roleService.findRoleById(roleId));
    }

    @Operation(summary = "分页查询角色")
    @PostMapping("/findByPage")
    public BaseResult<Page<RolePageVO>> findRoleByPage(@RequestParam(required = false,value = "pageNum",defaultValue = "1") Integer pageNum,
                                                       @RequestParam(required = false,value = "pageSize",defaultValue = "10") Integer pageSize,
                                                       @RequestBody RolePageQuery rolePageQuery){
        return BaseResult.ok(roleService.findRoleByPage(pageNum,pageSize,rolePageQuery));
    }

    @Operation(summary = "查看角色拥有的权限")
    @GetMapping("/findPermissionByRole")
    public BaseResult<List<PermissionInfoVO>> findPermissionByRole(@RequestParam(required = true,value = "roleId") Integer roleId){
        return BaseResult.ok(roleService.findPermissionByRole(roleId));
    }

    @Operation(summary = "为角色分配权限")
    @PostMapping("/assignPermissionToRole")
    public BaseResult<Integer> assignPermissionToRole(@RequestParam(required = true,value = "roleId") Integer roleId,
                                                     @RequestParam(required = true,value = "permissionIds") Integer[] permissionIds){
        return BaseResult.ok(roleService.assignPermissionToRole(roleId,permissionIds));
    }

}
