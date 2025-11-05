package com.zyh.easyapplyresume.utils.Validator;

import com.zyh.easyapplyresume.bean.BusException;
import com.zyh.easyapplyresume.bean.CodeEnum;
import com.zyh.easyapplyresume.model.form.admin.PermissionForm;

/**
 * PermissionForm检查工具类，用于验证权限表单数据并设置默认值
 * 支持新增场景（默认值+长度校验）和修改场景（非空校验+长度校验）
 */
public class PermissionFormValidator {

    // 权限名称最大长度（新增/修改场景共用）
    private static final int PERMISSION_NAME_MAX_LENGTH = 20;
    // 权限URL最大长度（新增/修改场景共用）
    private static final int PERMISSION_URL_MAX_LENGTH = 50;

    /**
     * 权限新增场景校验：设置默认值 + 字段长度校验
     * @param permissionForm 待验证的权限表单对象
     * @throws BusException 字段长度超出限制时抛出（绑定CodeEnum错误码）
     */
    public static void validateForAdd(PermissionForm permissionForm) {
        // 1. 为空字段设置默认值
        if (permissionForm.getPermissionName() == null || permissionForm.getPermissionName().trim().isEmpty()) {
            permissionForm.setPermissionName("默认权限");
        }
        if (permissionForm.getPermissionUrl() == null || permissionForm.getPermissionUrl().trim().isEmpty()) {
            permissionForm.setPermissionUrl("默认URL");
        }

        // 2. 字段长度校验（trim()处理避免纯空格占用长度）
        String trimedName = permissionForm.getPermissionName().trim();
        String trimedUrl = permissionForm.getPermissionUrl().trim();
        if (trimedName.length() > PERMISSION_NAME_MAX_LENGTH) {
            throw new BusException(CodeEnum.PERMISSION_NAME_TOO_LONG);
        }
        if (trimedUrl.length() > PERMISSION_URL_MAX_LENGTH) {
            throw new BusException(CodeEnum.PERMISSION_URL_TOO_LONG);
        }

        // 3. 还原trim后的值（避免表单中保留多余空格）
        permissionForm.setPermissionName(trimedName);
        permissionForm.setPermissionUrl(trimedUrl);
    }

    /**
     * 权限修改场景校验：必传字段非空校验 + 字段长度校验
     * @param permissionForm 待验证的权限表单对象
     * @throws BusException 字段为空或长度超出限制时抛出（绑定CodeEnum错误码）
     */
    public static void validateForUpdate(PermissionForm permissionForm) {
        // 1. 权限ID非空校验（修改操作必须指定唯一标识）
        if (permissionForm.getPermissionId() == null) {
            throw new BusException(CodeEnum.PERMISSION_UPDATE_ID_EMPTY);
        }

        // 2. 权限名称非空 + 长度校验
        String permissionName = permissionForm.getPermissionName();
        if (permissionName == null || permissionName.trim().isEmpty()) {
            throw new BusException(CodeEnum.PERMISSION_UPDATE_NAME_EMPTY);
        }
        String trimedName = permissionName.trim();
        if (trimedName.length() > PERMISSION_NAME_MAX_LENGTH) {
            throw new BusException(CodeEnum.PERMISSION_NAME_TOO_LONG);
        }

        // 3. 权限URL非空 + 长度校验
        String permissionUrl = permissionForm.getPermissionUrl();
        if (permissionUrl == null || permissionUrl.trim().isEmpty()) {
            throw new BusException(CodeEnum.PERMISSION_UPDATE_URL_EMPTY);
        }
        String trimedUrl = permissionUrl.trim();
        if (trimedUrl.length() > PERMISSION_URL_MAX_LENGTH) {
            throw new BusException(CodeEnum.PERMISSION_URL_TOO_LONG);
        }

        // 4. 还原trim后的值（避免表单中保留多余空格）
        permissionForm.setPermissionName(trimedName);
        permissionForm.setPermissionUrl(trimedUrl);
    }
}