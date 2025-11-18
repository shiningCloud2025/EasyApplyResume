package com.zyh.easyapplyresume.utils.Validator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.CodeEnum;
import com.zyh.easyapplyresume.model.form.admin.PermissionForm;

/**
 * PermissionForm检查工具类，用于验证权限表单数据并设置默认值
 * 核心规则：
 * - 新增：权限名称、权限URL必填；简介选填（未填→默认文案）；
 * - 修改：所有字段必须非空+全量校验（权限ID、名称、URL、简介均不能为空）；
 * - 长度限制：名称≤20字，URL≤50字，简介≤30字
 * @author shiningCloud2025
 */
public class PermissionFormValidator {

    // 权限名称长度限制（1-20位）
    private static final int PERMISSION_NAME_MAX_LENGTH = 20;
    // 权限URL长度限制（1-50位）
    private static final int PERMISSION_URL_MAX_LENGTH = 50;
    // 权限简介长度限制（1-30位）
    private static final int PERMISSION_INTRO_MAX_LENGTH = 30;
    // 权限简介默认值（仅新增场景未填写时使用）
    private static final String DEFAULT_PERMISSION_INTRO = "默认无权限";

    /**
     * 新增权限校验规则：
     * - 强制必填：权限名称、权限URL（均不能为空）
     * - 选填+默认：权限简介（未填→默认文案，填了→校验长度≤30字）
     */
    public static void validateForAdd(PermissionForm permissionForm) {
        // 1. 强制必填：权限名称（非空+长度≤20位）→ PERMISSION_ADD_NAME_EMPTY(641) / PERMISSION_NAME_TOO_LONG(646)
        if (permissionForm.getPermissionName() == null || permissionForm.getPermissionName().trim().isEmpty()) {
            throw new BusException(CodeEnum.PERMISSION_ADD_NAME_EMPTY);
        }
        String permissionName = permissionForm.getPermissionName().trim();
        if (permissionName.length() > PERMISSION_NAME_MAX_LENGTH) {
            throw new BusException(CodeEnum.PERMISSION_NAME_TOO_LONG);
        }
        permissionForm.setPermissionName(permissionName); // 去空格后存值

        // 2. 强制必填：权限URL（非空+长度≤50位）→ PERMISSION_ADD_URL_EMPTY(642) / PERMISSION_URL_TOO_LONG(647)
        if (permissionForm.getPermissionUrl() == null || permissionForm.getPermissionUrl().trim().isEmpty()) {
            throw new BusException(CodeEnum.PERMISSION_ADD_URL_EMPTY);
        }
        String permissionUrl = permissionForm.getPermissionUrl().trim();
        if (permissionUrl.length() > PERMISSION_URL_MAX_LENGTH) {
            throw new BusException(CodeEnum.PERMISSION_URL_TOO_LONG);
        }
        permissionForm.setPermissionUrl(permissionUrl); // 去空格后存值

        // 3. 选填+默认：权限简介（未填→默认文案，填了→校验长度≤30位）→ PERMISSION_INTRO_TOO_LONG(648)
        String permissionIntro = permissionForm.getPermissionIntroduce();
        if (permissionIntro == null || permissionIntro.trim().isEmpty()) {
            permissionForm.setPermissionIntroduce(DEFAULT_PERMISSION_INTRO); // 默认值
        } else {
            permissionIntro = permissionIntro.trim();
            if (permissionIntro.length() > PERMISSION_INTRO_MAX_LENGTH) {
                throw new BusException(CodeEnum.PERMISSION_INTRO_TOO_LONG);
            }
            permissionForm.setPermissionIntroduce(permissionIntro); // 去空格后存值
        }
    }

    /**
     * 修改权限校验规则：
     * - 强制要求：所有字段必须非空（权限ID、名称、URL、简介均不能为空）+ 全量校验
     */
    public static void validateForUpdate(PermissionForm permissionForm) {
        // 1. 强制必填：权限ID（非空）→ PERMISSION_UPDATE_ID_EMPTY(640)
        if (permissionForm.getPermissionId() == null) {
            throw new BusException(CodeEnum.PERMISSION_UPDATE_ID_EMPTY);
        }

        // 2. 强制必填：权限名称（非空+长度≤20位）→ PERMISSION_UPDATE_NAME_EMPTY(643) / PERMISSION_NAME_TOO_LONG(646)
        if (permissionForm.getPermissionName() == null || permissionForm.getPermissionName().trim().isEmpty()) {
            throw new BusException(CodeEnum.PERMISSION_UPDATE_NAME_EMPTY);
        }
        String permissionName = permissionForm.getPermissionName().trim();
        if (permissionName.length() > PERMISSION_NAME_MAX_LENGTH) {
            throw new BusException(CodeEnum.PERMISSION_NAME_TOO_LONG);
        }
        permissionForm.setPermissionName(permissionName); // 去空格后存值

        // 3. 强制必填：权限URL（非空+长度≤50位）→ PERMISSION_UPDATE_URL_EMPTY(644) / PERMISSION_URL_TOO_LONG(647)
        if (permissionForm.getPermissionUrl() == null || permissionForm.getPermissionUrl().trim().isEmpty()) {
            throw new BusException(CodeEnum.PERMISSION_UPDATE_URL_EMPTY);
        }
        String permissionUrl = permissionForm.getPermissionUrl().trim();
        if (permissionUrl.length() > PERMISSION_URL_MAX_LENGTH) {
            throw new BusException(CodeEnum.PERMISSION_URL_TOO_LONG);
        }
        permissionForm.setPermissionUrl(permissionUrl); // 去空格后存值

        // 4. 强制必填：权限简介（非空+长度≤30位）→ PERMISSION_UPDATE_INTRO_EMPTY(645) / PERMISSION_INTRO_TOO_LONG(648)
        if (permissionForm.getPermissionIntroduce() == null || permissionForm.getPermissionIntroduce().trim().isEmpty()) {
            throw new BusException(CodeEnum.PERMISSION_UPDATE_INTRO_EMPTY);
        }
        String permissionIntro = permissionForm.getPermissionIntroduce().trim();
        if (permissionIntro.length() > PERMISSION_INTRO_MAX_LENGTH) {
            throw new BusException(CodeEnum.PERMISSION_INTRO_TOO_LONG);
        }
        permissionForm.setPermissionIntroduce(permissionIntro); // 去空格后存值
    }
}