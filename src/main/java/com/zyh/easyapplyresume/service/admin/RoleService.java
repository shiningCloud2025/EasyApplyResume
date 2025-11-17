package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.admin.RoleForm;
import com.zyh.easyapplyresume.model.query.admin.RolePageQuery;
import com.zyh.easyapplyresume.model.vo.admin.PermissionInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.RoleInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.RolePageVO;

import java.util.List;
/**
 * @author shiningCloud2025
 */
public interface RoleService {
    /**
     * 新增角色
     */
    public void addRole(RoleForm roleForm);
    /**
     * 修改角色
     */
    public void updateRole(RoleForm roleForm);
    /**
     * 删除角色
     */
    public void deleteRole(Integer roleId);
    /**
     * 查询角色信息
     */
    public RoleInfoVO findRoleById(Integer roleId);
    /**
     * 分页查询角色信息
     */
    public Page<RolePageVO> findRoleByPage(Integer pageNum, Integer pageSize, RolePageQuery rolePageQuery);
    /**
     * 查询所有角色
     */
    public List<RoleInfoVO> findAllRole();
    /**
     * 查询角色拥有的权限
     */
    public List<PermissionInfoVO> findPermissionByRole(Integer roleId);
    /**
     * 为角色分配权限
     */
    public void assignPermissionToRole(Integer roleId, Integer[] permissionIds);



}
