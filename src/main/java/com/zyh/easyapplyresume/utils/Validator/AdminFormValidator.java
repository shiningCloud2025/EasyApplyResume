package com.zyh.easyapplyresume.utils.Validator;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.CodeEnum;
import com.zyh.easyapplyresume.model.form.admin.AdminForm;

import java.util.regex.Pattern;

/**
 * AdminForm检查工具类，用于验证表单数据并设置默认值
 * 核心规则：
 * - 新增：账号名、手机号、邮箱 均为必填；其他字段有默认值；
 * - 修改：所有字段必须非空+全量校验（不能留空）
 * @author shiningCloud2025
 */
public class AdminFormValidator {

    // 邮箱正则表达式
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    // 手机号正则表达式(11位数字)
    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";
    // 账号名长度限制（1-15位）
    private static final int USERNAME_MAX_LENGTH = 15;
    // 邮箱长度限制（1-25位）
    private static final int EMAIL_MAX_LENGTH = 25;
    // 介绍长度限制（1-200位）
    private static final int INTRO_MAX_LENGTH = 200;
    // 密码长度限制（6-20位）
    private static final int PASSWORD_MIN_LENGTH = 6;
    private static final int PASSWORD_MAX_LENGTH = 20;

    /**
     * 新增管理员校验规则：
     * - 强制必填：账号名、手机号、邮箱（均不能为空）
     * - 辅助字段：密码（默认123456）、头像（默认图）、介绍（默认文案+自定义≤200字）、状态（默认1+校验合法性）
     */
    public static void validateForAdd(AdminForm adminForm) {
        // 1. 强制必填：账号名（非空+长度1-15位）→ ADMIN_ADD_USERNAME_EMPTY(609)
        if (adminForm.getAdminUsername() == null || adminForm.getAdminUsername().trim().isEmpty()) {
            throw new BusException(CodeEnum.ADMIN_ADD_USERNAME_EMPTY);
        }
        String username = adminForm.getAdminUsername().trim();
        if (username.length() > USERNAME_MAX_LENGTH) {
            throw new BusException(CodeEnum.ADMIN_USERNAME_TOO_LONG); // 605
        }
        adminForm.setAdminUsername(username); // 去空格后存值

        // 2. 强制必填：手机号（非空+格式正确）→ ADMIN_ADD_PHONE_EMPTY(610) / ADMIN_PHONE_FORMAT_ERROR(603)
        if (adminForm.getAdminPhone() == null || adminForm.getAdminPhone().trim().isEmpty()) {
            throw new BusException(CodeEnum.ADMIN_ADD_PHONE_EMPTY);
        }
        String phone = adminForm.getAdminPhone().trim();
        if (!Pattern.matches(PHONE_REGEX, phone)) {
            throw new BusException(CodeEnum.ADMIN_PHONE_FORMAT_ERROR);
        }
        adminForm.setAdminPhone(phone); // 去空格后存值

        // 3. 强制必填：邮箱（非空+格式正确+长度≤25位）→ ADMIN_ADD_EMAIL_EMPTY(611) / ADMIN_EMAIL_TOO_LONG(606) / ADMIN_EMAIL_FORMAT_ERROR(604)
        if (adminForm.getAdminEmail() == null || adminForm.getAdminEmail().trim().isEmpty()) {
            throw new BusException(CodeEnum.ADMIN_ADD_EMAIL_EMPTY); // 新增邮箱必填校验
        }
        String email = adminForm.getAdminEmail().trim();
        if (email.length() > EMAIL_MAX_LENGTH) {
            throw new BusException(CodeEnum.ADMIN_EMAIL_TOO_LONG);
        }
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            throw new BusException(CodeEnum.ADMIN_EMAIL_FORMAT_ERROR);
        }
        adminForm.setAdminEmail(email); // 去空格后存值

        // 4. 辅助字段：密码（默认123456，填了则校验长度）→ ADMIN_PASSWORD_LENGTH_ERROR(608)
        if (adminForm.getAdminPassword() == null || adminForm.getAdminPassword().trim().isEmpty()) {
            adminForm.setAdminPassword("123456");
        } else {
            validatePassword(adminForm.getAdminPassword().trim());
        }

        // 5. 辅助字段：头像（默认图）
        if (adminForm.getAdminImage() == null || adminForm.getAdminImage().trim().isEmpty()) {
            adminForm.setAdminImage("https://ts1.tc.mm.bing.net/th/id/R-C.928ef8908b5eb3666b2a27a1f6cfbe17?rik=h2FXLv1HNaxbTg&riu=http%3a%2f%2fp0.so.qhmsg.com%2ft018b5eb3666b2a27a1.jpg&ehk=QnGPPvZKq3cPW6%2bdkG%2b3zIRvAGXRsgYVTirfbvOBTaU%3d&risl=&pid=ImgRaw&r=0");
        }

        // 6. 辅助字段：介绍（默认文案+自定义≤200字）→ ADMIN_INTRO_TOO_LONG(607)
        String intro = adminForm.getAdminIntroduce();
        if (intro == null || intro.trim().isEmpty()) {
            adminForm.setAdminIntroduce("这个人很懒,没有留下任何介绍");
        } else {
            intro = intro.trim();
            if (intro.length() > INTRO_MAX_LENGTH) {
                throw new BusException(CodeEnum.ADMIN_INTRO_TOO_LONG);
            }
            adminForm.setAdminIntroduce(intro); // 去空格后存值
        }

        // 7. 辅助字段：状态（默认1-正常+校验合法性）→ ADMIN_STATE_ILLEGAL(619)
        if (adminForm.getAdminState() == null) {
            adminForm.setAdminState(1);
        }
    }

    /**
     * 修改管理员校验规则：
     * - 强制要求：所有字段必须非空（不能留空）+ 全量校验（格式/长度/合法性）
     */
    public static void validateForUpdate(AdminForm adminForm) {
        // 1. 账号名：必须非空 + 长度≤15位 → ADMIN_UPDATE_USERNAME_EMPTY(612) / ADMIN_USERNAME_TOO_LONG(605)
        if (adminForm.getAdminUsername() == null || adminForm.getAdminUsername().trim().isEmpty()) {
            throw new BusException(CodeEnum.ADMIN_UPDATE_USERNAME_EMPTY);
        }
        String username = adminForm.getAdminUsername().trim();
        if (username.length() > USERNAME_MAX_LENGTH) {
            throw new BusException(CodeEnum.ADMIN_USERNAME_TOO_LONG);
        }
        adminForm.setAdminUsername(username);

        // 2. 手机号：必须非空 + 11位有效数字 → ADMIN_UPDATE_PHONE_EMPTY(613) / ADMIN_PHONE_FORMAT_ERROR(603)
        if (adminForm.getAdminPhone() == null || adminForm.getAdminPhone().trim().isEmpty()) {
            throw new BusException(CodeEnum.ADMIN_UPDATE_PHONE_EMPTY);
        }
        String phone = adminForm.getAdminPhone().trim();
        if (!Pattern.matches(PHONE_REGEX, phone)) {
            throw new BusException(CodeEnum.ADMIN_PHONE_FORMAT_ERROR);
        }
        adminForm.setAdminPhone(phone);

        // 3. 邮箱：必须非空 + 格式正确 + 长度≤25位 → ADMIN_UPDATE_EMAIL_EMPTY(614) / ADMIN_EMAIL_TOO_LONG(606) / ADMIN_EMAIL_FORMAT_ERROR(604)
        if (adminForm.getAdminEmail() == null || adminForm.getAdminEmail().trim().isEmpty()) {
            throw new BusException(CodeEnum.ADMIN_UPDATE_EMAIL_EMPTY);
        }
        String email = adminForm.getAdminEmail().trim();
        if (email.length() > EMAIL_MAX_LENGTH) {
            throw new BusException(CodeEnum.ADMIN_EMAIL_TOO_LONG);
        }
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            throw new BusException(CodeEnum.ADMIN_EMAIL_FORMAT_ERROR);
        }
        adminForm.setAdminEmail(email);

        // 4. 密码：必须非空 + 长度6-20位 → ADMIN_UPDATE_PASSWORD_EMPTY(615) / ADMIN_PASSWORD_LENGTH_ERROR(608)
        if (adminForm.getAdminPassword() == null || adminForm.getAdminPassword().trim().isEmpty()) {
            throw new BusException(CodeEnum.ADMIN_UPDATE_PASSWORD_EMPTY);
        }
        String password = adminForm.getAdminPassword().trim();
        validatePassword(password);
        adminForm.setAdminPassword(password);

        // 5. 头像：必须非空（不能是空串）→ ADMIN_UPDATE_IMAGE_EMPTY(616)
        if (adminForm.getAdminImage() == null || adminForm.getAdminImage().trim().isEmpty()) {
            throw new BusException(CodeEnum.ADMIN_UPDATE_IMAGE_EMPTY);
        }

        // 6. 介绍：必须非空 + 长度≤200位 → ADMIN_UPDATE_INTRO_EMPTY(617) / ADMIN_INTRO_TOO_LONG(607)
        if (adminForm.getAdminIntroduce() == null || adminForm.getAdminIntroduce().trim().isEmpty()) {
            throw new BusException(CodeEnum.ADMIN_UPDATE_INTRO_EMPTY);
        }
        String intro = adminForm.getAdminIntroduce().trim();
        if (intro.length() > INTRO_MAX_LENGTH) {
            throw new BusException(CodeEnum.ADMIN_INTRO_TOO_LONG);
        }
        adminForm.setAdminIntroduce(intro);

        // 7. 状态：必须非空 + 只能是0/1 → ADMIN_UPDATE_STATE_EMPTY(618) / ADMIN_STATE_ILLEGAL(619)
        if (adminForm.getAdminState() == null) {
            throw new BusException(CodeEnum.ADMIN_UPDATE_STATE_EMPTY);
        }
        validateAdminState(adminForm.getAdminState());
    }

    /**
     * 私有辅助方法：校验密码长度（6-20位）
     */
    private static void validatePassword(String password) {
        if (password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH) {
            throw new BusException(CodeEnum.ADMIN_PASSWORD_LENGTH_ERROR);
        }
    }

    /**
     * 私有辅助方法：校验管理员状态（只能是0-禁用，1-正常）
     */
    private static void validateAdminState(Integer state) {
        if (state != 0 && state != 1) {
            throw new BusException(CodeEnum.ADMIN_STATE_ILLEGAL);
        }
    }
}