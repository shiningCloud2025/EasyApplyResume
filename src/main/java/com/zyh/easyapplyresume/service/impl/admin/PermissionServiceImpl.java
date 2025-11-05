package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.mapper.admin.PermissionMapper;
import com.zyh.easyapplyresume.model.form.admin.PermissionForm;
import com.zyh.easyapplyresume.model.pojo.admin.Permission;
import com.zyh.easyapplyresume.model.query.admin.PermissionPageQuery;
import com.zyh.easyapplyresume.model.vo.admin.PermissionInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.PermissionPageVO;
import com.zyh.easyapplyresume.service.admin.PermissionService;
import com.zyh.easyapplyresume.utils.Validator.PermissionFormValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Override
    public void addPermission(PermissionForm permissionForm) {
        PermissionFormValidator.validateForAdd(permissionForm);
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionForm, permission);
        permissionMapper.insert(permission);
    }

    @Override
    public void updatePermission(PermissionForm permissionForm) {
        PermissionFormValidator.validateForUpdate(permissionForm);
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionForm, permission);
        permissionMapper.updateById(permission);
    }

    @Override
    public void deletePermission(Integer permissionId) {
        Permission permission = new Permission();
        permission.setDeleted(1);
        permissionMapper.updateById(permission);
        permissionMapper.deleteRolePermissionByPermissionId(permissionId);
    }

    @Override
    public PermissionInfoVO findPermissionById(Integer permissionId) {
        return
    }

    @Override
    public Page<PermissionPageVO> findPermissionByPage(Integer pageNum, Integer pageSize, PermissionPageQuery permissionPageQuery) {
        return null;
    }

    @Override
    public List<PermissionInfoVO> findAllPermission() {
        return List.of();
    }
}
