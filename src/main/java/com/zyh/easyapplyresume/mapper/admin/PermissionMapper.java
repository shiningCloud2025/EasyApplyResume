package com.zyh.easyapplyresume.mapper.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.easyapplyresume.model.pojo.admin.Permission;
import com.zyh.easyapplyresume.model.vo.admin.PermissionInfoVO;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {
    // 根据权限id删除角色权限表
    void deleteRolePermissionByPermissionId(Integer permissionId);
    // 查询所有权限
    public List<PermissionInfoVO> findAllPermission();

}
