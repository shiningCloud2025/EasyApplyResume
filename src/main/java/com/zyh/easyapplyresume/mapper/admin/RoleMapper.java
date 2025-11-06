package com.zyh.easyapplyresume.mapper.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.easyapplyresume.model.pojo.admin.Role;
import com.zyh.easyapplyresume.model.vo.admin.PermissionInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.RoleInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface RoleMapper extends BaseMapper<Role> {
    // 根据role的id删除对应的admin
    void deleteRoleAdminByRoleId(Integer roleId);
    // 根据role的id删除对应的permission
    void deleteRolePermissionByRoleId(Integer roleId);
    // 查询角色和权限
    RoleInfoVO findRoleInfoById(Integer roleId);
    // 查询所有角色
    List<RoleInfoVO> findAllRole();
    // 查询角色拥有的权限
    List<PermissionInfoVO> findPermissionByRole(Integer roleId);
    // 给角色分配权限
    void assignPermissionToRole(@Param("roleId") Integer roleId,@Param("permissionId") Integer permissionId);
}
