package com.zyh.easyapplyresume.utils.Validator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.CodeEnum;
import com.zyh.easyapplyresume.model.form.admin.RoleForm;

/**
 * RoleForm检查工具类，用于验证表单数据并设置默认值
 * 核心规则：
 * - 新增：角色名称必填；简介选填（未填→默认文案）；
 * - 修改：所有字段必须非空+全量校验（角色ID、角色名称、角色简介均不能为空）；
 * - 长度限制：角色名称≤12字，角色简介≤30字
 * @author shiningCloud2025
 */
public class RoleFormValidator {

    // 角色名称长度限制（1-12位）
    private static final int ROLE_NAME_MAX_LENGTH = 12;
    // 角色简介长度限制（1-30位）
    private static final int ROLE_INTRO_MAX_LENGTH = 30;
    // 角色简介默认值（仅新增场景未填写时使用）
    private static final String DEFAULT_ROLE_INTRO = "这个角色很懒，没有写任何介绍";

    /**
     * 新增角色校验规则：
     * - 强制必填：角色名称（不能为空）
     * - 选填+默认：角色简介（未填→默认文案，填了→校验长度≤30字）
     */
    public static void validateForAdd(RoleForm roleForm) {
        // 1. 强制必填：角色名称（非空+长度≤12位）→ ROLE_ADD_NAME_EMPTY / ROLE_NAME_TOO_LONG
        if (roleForm.getRoleName() == null || roleForm.getRoleName().trim().isEmpty()) {
            throw new BusException(CodeEnum.ROLE_ADD_NAME_EMPTY);
        }
        String roleName = roleForm.getRoleName().trim();
        if (roleName.length() > ROLE_NAME_MAX_LENGTH) {
            throw new BusException(CodeEnum.ROLE_NAME_TOO_LONG);
        }
        roleForm.setRoleName(roleName); // 去空格后存值

        // 2. 选填+默认：角色简介（未填→默认文案，填了→校验长度≤30位）→ ROLE_INTRODUCE_TOO_LONG
        String roleIntro = roleForm.getRoleIntroduce();
        if (roleIntro == null || roleIntro.trim().isEmpty()) {
            roleForm.setRoleIntroduce(DEFAULT_ROLE_INTRO); // 默认值
        } else {
            roleIntro = roleIntro.trim();
            if (roleIntro.length() > ROLE_INTRO_MAX_LENGTH) {
                throw new BusException(CodeEnum.ROLE_INTRODUCE_TOO_LONG);
            }
            roleForm.setRoleIntroduce(roleIntro); // 去空格后存值
        }
    }

    /**
     * 修改角色校验规则：
     * - 强制要求：所有字段必须非空（角色ID、角色名称、角色简介均不能为空）+ 全量校验
     */
    public static void validateForUpdate(RoleForm roleForm) {
        // 1. 强制必填：角色ID（非空）→ ROLE_UPDATE_ID_EMPTY
        if (roleForm.getRoleId() == null) {
            throw new BusException(CodeEnum.ROLE_UPDATE_ID_EMPTY);
        }

        // 2. 强制必填：角色名称（非空+长度≤12位）→ ROLE_UPDATE_NAME_EMPTY / ROLE_NAME_TOO_LONG
        if (roleForm.getRoleName() == null || roleForm.getRoleName().trim().isEmpty()) {
            throw new BusException(CodeEnum.ROLE_UPDATE_NAME_EMPTY);
        }
        String roleName = roleForm.getRoleName().trim();
        if (roleName.length() > ROLE_NAME_MAX_LENGTH) {
            throw new BusException(CodeEnum.ROLE_NAME_TOO_LONG);
        }
        roleForm.setRoleName(roleName); // 去空格后存值

        // 3. 强制必填：角色简介（非空+长度≤30位）→ ROLE_UPDATE_INTRODUCE_EMPTY / ROLE_INTRODUCE_TOO_LONG
        if (roleForm.getRoleIntroduce() == null || roleForm.getRoleIntroduce().trim().isEmpty()) {
            throw new BusException(CodeEnum.ROLE_UPDATE_INTRODUCE_EMPTY);
        }
        String roleIntro = roleForm.getRoleIntroduce().trim();
        if (roleIntro.length() > ROLE_INTRO_MAX_LENGTH) {
            throw new BusException(CodeEnum.ROLE_INTRODUCE_TOO_LONG);
        }
        roleForm.setRoleIntroduce(roleIntro); // 去空格后存值
    }
}