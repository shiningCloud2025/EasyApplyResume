package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminForm;
import com.zyh.easyapplyresume.model.query.admin.AdminPageQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminPageVO;
import com.zyh.easyapplyresume.model.vo.admin.RoleInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/admin")
@Tag(name="管理员控制器-管理端")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Operation(summary = "新增管理员")
    @PostMapping("/add")
    public BaseResult<Integer> addAdmin(@RequestBody AdminForm adminForm){
        return BaseResult.ok(adminService.addAdmin(adminForm));
    }

    @Operation(summary = "修改管理员")
    @PostMapping("/update")
    public BaseResult<Integer> updateAdmin(@RequestBody AdminForm adminForm){
        return BaseResult.ok(adminService.updateAdmin(adminForm));
    }

    @Operation(summary = "删除管理员")
    @DeleteMapping("/delete")
    public BaseResult<Integer> deleteAdmin(@RequestParam(required = true,value = "adminId") Integer adminId){
       return BaseResult.ok(adminService.deleteAdmin(adminId));
    }

    @Operation(summary = "查询管理员")
    @GetMapping("/findById")
    public BaseResult<AdminInfoVO> findAdminById(@RequestParam(required = true,value = "adminId") Integer adminId){
        return BaseResult.ok(adminService.findAdminById(adminId));
    }

    @Operation(summary = "分页查询管理员")
    @GetMapping("/findByPage")
    public BaseResult<Page<AdminPageVO>> findAdminByPage(@RequestParam(required = false,value = "pageNum",defaultValue = "1") Integer pageNum,
                                                         @RequestParam(required = false,value = "pageSize",defaultValue = "10") Integer pageSize,
                                                         @RequestBody AdminPageQuery adminPageQuery){
        return BaseResult.ok(adminService.findAdminByPage(pageNum,pageSize,adminPageQuery));
    }

    @Operation(summary = "查看管理员拥有的角色")
    @GetMapping("/findRoleByAdmin")
    public BaseResult<List<RoleInfoVO>> findRoleByAdmin(@RequestParam(required = true,value = "adminId") Integer adminId){
        return BaseResult.ok(adminService.findRoleByAdmin(adminId));
    }

    @Operation(summary = "为管理员分配角色")
    @PostMapping("/assignRoleToAdmin")
    public BaseResult<Integer> assignRoleToAdmin(@RequestParam(required = true,value = "adminId") Integer adminId,
                                  @RequestParam(required = true,value = "roleIds") Integer[] roleIds){
        return BaseResult.ok(adminService.assignRoleToAdmin(adminId,roleIds));
    }


}
