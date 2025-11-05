package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.admin.PermissionForm;
import com.zyh.easyapplyresume.model.query.admin.PermissionPageQuery;
import com.zyh.easyapplyresume.model.vo.admin.PermissionInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.PermissionPageVO;

import java.util.List;

public interface PermissionService {
    /**
     * 新增权限
     */
    public void addPermission(PermissionForm permissionForm);
    /**
     * 修改权限
     */
    public void updatePermission(PermissionForm permissionForm);
    /**
     * 删除权限
     */
    public void deletePermission(Integer permissionId);
    /**
     * 根据权限ID查询权限信息
     */
    public PermissionInfoVO findPermissionById(Integer permissionId);
    /**
     * 分页查询权限信息
     */
    public Page<PermissionPageVO> findPermissionByPage(Integer pageNum, Integer pageSize, PermissionPageQuery permissionPageQuery);
    /**
     * 查询所有权限信息
     */
    public List<PermissionInfoVO> findAllPermission();
}
