package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.mapper.mysql.admin.RoleMapper;
import com.zyh.easyapplyresume.model.form.admin.RoleForm;
import com.zyh.easyapplyresume.model.pojo.admin.Role;
import com.zyh.easyapplyresume.model.query.admin.RolePageQuery;
import com.zyh.easyapplyresume.model.vo.admin.PermissionInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.RoleInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.RolePageVO;
import com.zyh.easyapplyresume.service.admin.RoleService;
import com.zyh.easyapplyresume.utils.Validator.RoleFormValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.lambdaQuery;
/**
 * @author shiningCloud2025
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public void addRole(RoleForm roleForm) {
        RoleFormValidator.validateForAdd(roleForm);
        Role role = new Role();
        BeanUtils.copyProperties(roleForm, role);
        roleMapper.insert(role);
    }

    @Override
    public void updateRole(RoleForm roleForm) {
        RoleFormValidator.validateForUpdate(roleForm);
        Role role = new Role();
        BeanUtils.copyProperties(roleForm, role);
        roleMapper.updateById(role);
    }

    @Override
    public void deleteRole(Integer roleId) {
        Role role = roleMapper.selectById(roleId);
        role.setDeleted(1);
        roleMapper.updateById(role);
        roleMapper.deleteRoleAdminByRoleId(roleId);
        roleMapper.deleteRolePermissionByRoleId(roleId);

    }

    @Override
    public RoleInfoVO findRoleById(Integer roleId) {
        return roleMapper.findRoleInfoById(roleId);
    }

    @Override
    public Page<RolePageVO> findRoleByPage(Integer pageNum, Integer pageSize, RolePageQuery rolePageQuery) {
        LambdaQueryWrapper<Role> lambdaQueryWrapper = lambdaQuery(Role.class);
        lambdaQueryWrapper.eq(Role::getDeleted, 0);
        if (rolePageQuery.getRoleName() != null && !rolePageQuery.getRoleName().isEmpty()) {
            lambdaQueryWrapper.like(Role::getRoleName, rolePageQuery.getRoleName());
        }
        if (rolePageQuery.getRoleIntroduce() != null && !rolePageQuery.getRoleIntroduce().isEmpty()) {
            lambdaQueryWrapper.like(Role::getRoleIntroduce, rolePageQuery.getRoleIntroduce());
        }
        Page<Role> rolePage = roleMapper.selectPage(new Page<>(pageNum, pageSize), lambdaQueryWrapper);

        // 3. 转换：Role实体列表 → RoleInfoVO列表（属性复制+自定义处理）
        List<RolePageVO> voList = rolePage.getRecords().stream()
                .map(role -> {
                    RolePageVO pageVO = new RolePageVO();
                    // 基础属性复制（要求Role和RoleInfoVO字段名、类型一致，如roleId、roleName等）
                    BeanUtils.copyProperties(role, pageVO);
                    // （关键）若字段名不一致，需手动补充映射（示例如下，根据实际VO结构调整）
                    // 示例1：若Role有"createTime"，VO有"roleCreateTime" → infoVO.setRoleCreateTime(role.getCreateTime());
                    // 示例2：若需格式化日期 → infoVO.setFormatCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(role.getCreateTime()));
                    return pageVO;
                })
                .collect(Collectors.toList());

        // 4. 构建VO分页对象（复制原始分页的分页参数）
        Page<RolePageVO> roleVOPage = new Page<>();
        roleVOPage.setRecords(voList);         // 核心：设置转换后的VO列表
        roleVOPage.setCurrent(rolePage.getCurrent()); // 当前页码（如第1页）
        roleVOPage.setSize(rolePage.getSize());       // 每页条数（如10条/页）
        roleVOPage.setTotal(rolePage.getTotal());     // 总数据量（关键：用于计算总页数）
        roleVOPage.setPages(rolePage.getPages());     // 总页数（如共5页）
        return roleVOPage;
    }

    @Override
    public List<RoleInfoVO> findAllRole() {
        return roleMapper.findAllRole();
    }

    @Override
    public List<PermissionInfoVO> findPermissionByRole(Integer roleId) {
        return roleMapper.findPermissionByRole(roleId);
    }

    @Override
    public void assignPermissionToRole(Integer roleId, Integer[] permissionIds) {
        if(permissionIds!=null){
            return ;
        } else{
            for (int permission : permissionIds) {
                roleMapper.deleteRolePermissionByRoleId(roleId);
                roleMapper.assignPermissionToRole(roleId, permission);
            }
        }
    }
}
