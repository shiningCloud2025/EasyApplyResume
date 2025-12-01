package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.RoleMapper;
import com.zyh.easyapplyresume.model.form.admin.RoleForm;
import com.zyh.easyapplyresume.model.pojo.admin.Role;
import com.zyh.easyapplyresume.model.query.admin.RolePageQuery;
import com.zyh.easyapplyresume.model.vo.admin.PermissionInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.RoleInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.RolePageVO;
import com.zyh.easyapplyresume.service.admin.RoleService;
import com.zyh.easyapplyresume.utils.adminvalidator.RoleFormValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    public Integer addRole(RoleForm roleForm) {
        if (roleForm == null){
            return 0;
        }
        RoleFormValidator.validateForAdd(roleForm);
        Role role = new Role();
        BeanUtils.copyProperties(roleForm, role);
        try{
            return roleMapper.insert(role);
        }catch (DataAccessException e){
            throw resolveDbException(e);
        }
    }

    @Override
    public Integer updateRole(RoleForm roleForm) {
        if (roleForm == null){
            return 0;
        }
        if (roleForm.getRoleId()==1){
            throw new BusException(AdminCodeEnum.NO_UPDATE_SUPER_ADMNIN_ROLE);
        }
        RoleFormValidator.validateForUpdate(roleForm);
        Role role = new Role();
        BeanUtils.copyProperties(roleForm, role);
        try{
           return roleMapper.updateById(role);
        }catch (DataAccessException e)
        {
            throw resolveDbException(e);
        }
    }

    private BusException resolveDbException(Exception e) {
        String errorMsg = e.getMessage();

        // 1. 处理唯一约束冲突（DuplicateKeyException 或 SQLIntegrityConstraintViolationException）
        if (errorMsg != null && (errorMsg.contains("Duplicate entry") || e instanceof org.springframework.dao.DuplicateKeyException)) {
            if (errorMsg.contains("role_name") || errorMsg.contains("admin_role_pk")) {
                // 匹配角色名字段或角色名唯一索引
                return new BusException(AdminCodeEnum.ROLE_NAME_DUPLICATE);
            }
        }
        // 兜底：未匹配到角色唯一冲突，返回异常转换失败枚举
        return new BusException(AdminCodeEnum.DB_EXCEPTION_TRANSFORM_FAIL_EXCEPTION);
    }

    @Override
    public Integer deleteRole(Integer roleId) {
        if (roleId==1){
            throw new BusException(AdminCodeEnum.NO_DELETE_SUPER_ADMNIN_ROLE);
        }
        Role role = roleMapper.selectById(roleId);
        role.setDeleted(1);
        roleMapper.updateById(role);
        roleMapper.deleteRoleAdminByRoleId(roleId);
        return roleMapper.deleteRolePermissionByRoleId(roleId);
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
    public Integer assignPermissionToRole(Integer roleId, Integer[] permissionIds) {
        int count = 0;
        if(permissionIds!=null){
            return count;
        } else{
            roleMapper.deleteRolePermissionByRoleId(roleId);
            for (int permission : permissionIds) {
                count+=roleMapper.assignPermissionToRole(roleId, permission);
            }
        }
        return count;
    }
}
