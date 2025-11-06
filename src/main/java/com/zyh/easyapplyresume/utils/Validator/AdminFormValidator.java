package com.zyh.easyapplyresume.utils.Validator;

import com.zyh.easyapplyresume.bean.BusException;
import com.zyh.easyapplyresume.bean.CodeEnum;
import com.zyh.easyapplyresume.model.form.admin.AdminForm;

import java.util.regex.Pattern;

/**
 * AdminForm检查工具类，用于验证表单数据并设置默认值
 * @author shiningCloud2025
 */
public class AdminFormValidator {

    // 邮箱正则表达式
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    // 手机号正则表达式(11位数字)
    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";

    /**
     * 验证并处理AdminForm数据（合并默认值设置和字段验证）
     * @param adminForm 待验证的表单对象
     * @throws BusException 任一属性为空时抛出（绑定CodeEnum错误码）
     */
    public static void validateForAdd(AdminForm adminForm) {
        // 1. 设置默认值（逻辑不变）
        if (adminForm.getAdminUsername() == null || adminForm.getAdminUsername().trim().isEmpty()) {
            adminForm.setAdminUsername("管理员");
        }
        if (adminForm.getAdminPassword() == null || adminForm.getAdminPassword().trim().isEmpty()) {
            adminForm.setAdminPassword("123456");
        }
        if (adminForm.getAdminImage() == null || adminForm.getAdminImage().trim().isEmpty()) {
            adminForm.setAdminImage("https://ts1.tc.mm.bing.net/th/id/R-C.928ef8908b5eb3666b2a27a1f6cfbe17?rik=h2FXLv1HNaxbTg&riu=http%3a%2f%2fp0.so.qhmsg.com%2ft018b5eb3666b2a27a1.jpg&ehk=QnGPPvZKq3cPW6%2bdkG%2b3zIRvAGXRsgYVTirfbvOBTaU%3d&risl=&pid=ImgRaw&r=0");
        }
        if (adminForm.getAdminIntroduce() == null || adminForm.getAdminIntroduce().trim().isEmpty()) {
            adminForm.setAdminIntroduce("这个人很懒,没有留下任何介绍");
        }
        if (adminForm.getAdminState() == null) {
            adminForm.setAdminState(1);
        }

        // 2. 字段验证（抛自定义BusinessException，绑定CodeEnum）
        // 验证名称长度
        if (adminForm.getAdminUsername().length() > 15) {
            throw new BusException(CodeEnum.ADMIN_USERNAME_TOO_LONG);
        }

        // 验证邮箱
        if (adminForm.getAdminEmail() != null) {
            if (adminForm.getAdminEmail().length() > 25) {
                throw new BusException(CodeEnum.ADMIN_EMAIL_TOO_LONG);
            }
            if (!Pattern.matches(EMAIL_REGEX, adminForm.getAdminEmail())) {
                throw new BusException(CodeEnum.ADMIN_EMAIL_FORMAT_ERROR);
            }
        }

        // 验证手机号
        if (adminForm.getAdminPhone() != null) {
            if (!Pattern.matches(PHONE_REGEX, adminForm.getAdminPhone())) {
                throw new BusException(CodeEnum.ADMIN_PHONE_FORMAT_ERROR);
            }
        }

        // 验证介绍长度
        if (adminForm.getAdminIntroduce().length() > 200) {
            throw new BusException(CodeEnum.ADMIN_INTRO_TOO_LONG);
        }
    }

    // ------------------- 新增：修改场景校验方法（全部属性非空） -------------------
    /**
     * 管理员修改场景校验：指定属性必须非空，否则抛对应异常
     * @param adminForm 修改用的表单对象
     * @throws BusException 任一属性为空时抛出（绑定CodeEnum错误码）
     */
    public static void validateForUpdate(AdminForm adminForm) {
        // 1. 校验【管理员名称】非空（字符串需排除空串）
        if (adminForm.getAdminUsername() == null || adminForm.getAdminUsername().trim().isEmpty()) {
            throw new BusException(CodeEnum.ADMIN_UPDATE_USERNAME_EMPTY);
        }

        // 2. 校验【管理员邮箱】非空 + 格式（复用原有格式校验逻辑，避免重复）
        if (adminForm.getAdminEmail() == null || adminForm.getAdminEmail().trim().isEmpty()) {
            throw new BusException(CodeEnum.ADMIN_UPDATE_EMAIL_EMPTY);
        }
        // 邮箱格式二次校验（修改时也需保证格式正确，可选但建议保留）
        if (!Pattern.matches(EMAIL_REGEX, adminForm.getAdminEmail().trim())) {
            throw new BusException(CodeEnum.ADMIN_EMAIL_FORMAT_ERROR);
        }

        // 3. 校验【管理员手机】非空 + 格式
        if (adminForm.getAdminPhone() == null || adminForm.getAdminPhone().trim().isEmpty()) {
            throw new BusException(CodeEnum.ADMIN_UPDATE_PHONE_EMPTY);
        }
        if (!Pattern.matches(PHONE_REGEX, adminForm.getAdminPhone().trim())) {
            throw new BusException(CodeEnum.ADMIN_PHONE_FORMAT_ERROR);
        }

        // 4. 校验【管理员密码】非空（字符串排除空串）
        if (adminForm.getAdminPassword() == null || adminForm.getAdminPassword().trim().isEmpty()) {
            throw new BusException(CodeEnum.ADMIN_UPDATE_PASSWORD_EMPTY);
        }

        // 5. 校验【管理员头像】非空（图片路径不能为null/空串）
        if (adminForm.getAdminImage() == null || adminForm.getAdminImage().trim().isEmpty()) {
            throw new BusException(CodeEnum.ADMIN_UPDATE_IMAGE_EMPTY);
        }

        // 6. 校验【管理员介绍】非空（字符串排除空串）
        if (adminForm.getAdminIntroduce() == null || adminForm.getAdminIntroduce().trim().isEmpty()) {
            throw new BusException(CodeEnum.ADMIN_UPDATE_INTRO_EMPTY);
        }
        // 介绍长度二次校验（可选，保持与新增场景一致）
        if (adminForm.getAdminIntroduce().trim().length() > 200) {
            throw new BusException(CodeEnum.ADMIN_INTRO_TOO_LONG);
        }

        // 7. 校验【管理员状态】非空（Integer类型只需判null）
        if (adminForm.getAdminState() == null) {
            throw new BusException(CodeEnum.ADMIN_UPDATE_STATE_EMPTY);
        }


    }

}