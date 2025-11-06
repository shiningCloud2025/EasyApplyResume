package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.mapper.admin.PermissionMapper;
import com.zyh.easyapplyresume.model.form.admin.PermissionForm;
import com.zyh.easyapplyresume.model.pojo.admin.Permission;
import com.zyh.easyapplyresume.model.query.admin.PermissionPageQuery;
import com.zyh.easyapplyresume.model.vo.admin.PermissionInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.PermissionPageVO;
import com.zyh.easyapplyresume.service.admin.PermissionService;
import com.zyh.easyapplyresume.utils.Validator.PermissionFormValidator;
import kotlin.jvm.internal.Lambda;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.lambdaQuery;

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
        Permission permission = permissionMapper.selectById(permissionId);
        return BeanUtil.copyProperties(permission, PermissionInfoVO.class);
    }
    @Override
    public Page<PermissionPageVO> findPermissionByPage(Integer pageNum, Integer pageSize, PermissionPageQuery permissionPageQuery) {
        // 1. 构建 LambdaQueryWrapper
        LambdaQueryWrapper<Permission> lambdaQueryWrapper = lambdaQuery(Permission.class);

        // 2. 判空过滤：permissionName 不为空则模糊查询
        if (permissionPageQuery.getPermissionName() != null && !permissionPageQuery.getPermissionName().isEmpty()) {
            lambdaQueryWrapper.like(Permission::getPermissionName, permissionPageQuery.getPermissionName());
        }

        // 判空过滤：permissionUrl 不为空则模糊查询
        if (permissionPageQuery.getPermissionUrl() != null && !permissionPageQuery.getPermissionUrl().isEmpty()) {
            lambdaQueryWrapper.like(Permission::getPermissionUrl, permissionPageQuery.getPermissionUrl());
        }

        // 3. 调用 Mapper 分页查询（需确保 PermissionMapper 有 selectPage 方法）
        Page<Permission> permissionPage = permissionMapper.selectPage(
                new Page<>(pageNum, pageSize),  // 分页参数：当前页、每页条数
                lambdaQueryWrapper              // 查询条件
        );

        // 4. 实体转换：Permission（数据库实体）→ PermissionPageVO（返回给前端的 VO）
        List<PermissionPageVO> voList = permissionPage.getRecords().stream()
                .map(permission -> {
                    PermissionPageVO pageVO = new PermissionPageVO();
                    // 复制同名字段（如 permissionId、permissionName、permissionUrl 等，要求字段名+类型一致）
                    BeanUtils.copyProperties(permission, pageVO);
                    // 若 VO 和 实体字段名不一致，需手动补充映射（示例如下，根据实际 VO 结构调整）
                    // 示例：实体有 "createTime"，VO 有 "permissionCreateTime" → pageVO.setPermissionCreateTime(permission.getCreateTime());
                    // 示例：日期格式化 → pageVO.setFormatCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(permission.getCreateTime()));
                    return pageVO;
                })
                .collect(Collectors.toList());

        // 5. 封装 VO 分页对象（复制原始分页的所有分页参数，保证分页逻辑正确）
        Page<PermissionPageVO> permissionVOPage = new Page<>();
        permissionVOPage.setRecords(voList);         // 核心：转换后的 VO 列表
        permissionVOPage.setCurrent(permissionPage.getCurrent()); // 当前页码
        permissionVOPage.setSize(permissionPage.getSize());       // 每页条数
        permissionVOPage.setTotal(permissionPage.getTotal());     // 总数据量（关键：计算总页数用）
        permissionVOPage.setPages(permissionPage.getPages());     // 总页数

        return permissionVOPage;
    }

    @Override
    public List<PermissionInfoVO> findAllPermission() {
        List<Permission> permissions = permissionMapper.selectList(null);
        List<PermissionInfoVO> permissionInfoVOs = BeanUtil.copyToList(permissions, PermissionInfoVO.class);
        return permissionInfoVOs;

    }
}
