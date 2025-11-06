package com.zyh.easyapplyresume.utils.Validator;

import com.zyh.easyapplyresume.bean.BusException;
import com.zyh.easyapplyresume.bean.CodeEnum;
import com.zyh.easyapplyresume.model.form.admin.RoleForm;

/**
 * RoleForm检查工具类，用于验证角色表单数据并设置默认值
 * @author shiningCloud2025
 */
public class RoleFormValidator {

    /**
     * 验证并处理RoleForm新增场景数据
     * 设置默认值并验证字段长度
     * @param roleForm 待验证的表单对象
     * @throws BusException 字段长度不符合要求时抛出
     */
    public static void validateForAdd(RoleForm roleForm) {
        // 设置默认值
        if (roleForm.getRoleName() == null || roleForm.getRoleName().trim().isEmpty()) {
            roleForm.setRoleName("默认角色");
        }
        if (roleForm.getRoleIntroduce() == null || roleForm.getRoleIntroduce().trim().isEmpty()) {
            roleForm.setRoleIntroduce("这个角色很懒，没有写任何介绍");
        }

        // 验证字段长度
        if (roleForm.getRoleName().length() > 12) {
            throw new BusException(CodeEnum.ROLE_NAME_TOO_LONG);
        }
        if (roleForm.getRoleIntroduce().length() > 30) {
            throw new BusException(CodeEnum.ROLE_INTRODUCE_TOO_LONG);
        }
    }

    /**
     * 验证RoleForm修改场景数据
     * 确保所有必要字段非空并验证长度
     * @param roleForm 待验证的表单对象
     * @throws BusException 字段为空或长度不符合要求时抛出
     */
    public static void validateForUpdate(RoleForm roleForm) {
        // 验证角色ID非空
        if (roleForm.getRoleId() == null) {
            throw new BusException(CodeEnum.ROLE_UPDATE_ID_EMPTY);
        }

        // 验证角色名称非空及长度
        if (roleForm.getRoleName() == null || roleForm.getRoleName().trim().isEmpty()) {
            throw new BusException(CodeEnum.ROLE_UPDATE_NAME_EMPTY);
        }
        if (roleForm.getRoleName().length() > 12) {
            throw new BusException(CodeEnum.ROLE_NAME_TOO_LONG);
        }

        // 验证角色介绍非空及长度
        if (roleForm.getRoleIntroduce() == null || roleForm.getRoleIntroduce().trim().isEmpty()) {
            throw new BusException(CodeEnum.ROLE_UPDATE_INTRODUCE_EMPTY);
        }
        if (roleForm.getRoleIntroduce().length() > 30) {
            throw new BusException(CodeEnum.ROLE_INTRODUCE_TOO_LONG);
        }
    }
}
