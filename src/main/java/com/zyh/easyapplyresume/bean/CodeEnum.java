package com.zyh.easyapplyresume.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回状态码枚举类-管理端
 */
@Getter
@AllArgsConstructor
public enum CodeEnum {
    // 正常
    SUCCESS(200,"OK"),
    // 系统异常
    SYSTEM_ERROR(500,"系统异常"),
    // 验证码错误
    CHECKCODE_ERROR(601,"验证码错误"),
    // 账户或密码错误
    USERNAMEORPASSWORD_ERROR(602,"账户或者密码错误"),

    // ------------------- 新增：AdminForm表单验证错误码（从603开始）-------------------
    // 管理员名称过长
    ADMIN_USERNAME_TOO_LONG(603,"管理员名称不能超过15个字符"),
    // 邮箱过长
    ADMIN_EMAIL_TOO_LONG(604,"邮箱不能超过25个字符"),
    // 邮箱格式错误
    ADMIN_EMAIL_FORMAT_ERROR(605,"邮箱格式不正确"),
    // 手机号格式错误
    ADMIN_PHONE_FORMAT_ERROR(606,"手机号必须是11位有效数字"),
    // 管理员介绍过长
    ADMIN_INTRO_TOO_LONG(607,"管理员介绍不能超过200个字符"),


    // ------------------- 新增：修改场景-属性非空错误码 -------------------
    ADMIN_UPDATE_USERNAME_EMPTY(608,"管理员名称不能为空"),
    ADMIN_UPDATE_EMAIL_EMPTY(609,"管理员邮箱不能为空"),
    ADMIN_UPDATE_PHONE_EMPTY(610,"管理员手机不能为空"),
    ADMIN_UPDATE_PASSWORD_EMPTY(611,"管理员密码不能为空"),
    ADMIN_UPDATE_IMAGE_EMPTY(612,"管理员头像不能为空"),
    ADMIN_UPDATE_INTRO_EMPTY(613,"管理员介绍不能为空"),
    ADMIN_UPDATE_STATE_EMPTY(614,"管理员状态不能为空"),
    ADMIN_UPDATE_LOGINTIME_EMPTY(615,"管理员最近登录时间不能为空"),


    // ------------------- 新增：RoleForm表单验证错误码（从630开始）-------------------
    // 角色名称过长
    ROLE_NAME_TOO_LONG(630,"角色名称不能超过12个字符"),
    // 角色介绍过长
    ROLE_INTRODUCE_TOO_LONG(631,"角色介绍不能超过30个字符"),
    // 修改场景-角色ID为空
    ROLE_UPDATE_ID_EMPTY(632,"角色ID不能为空"),
    // 修改场景-角色名称为空
    ROLE_UPDATE_NAME_EMPTY(633,"角色名称不能为空"),
    // 修改场景-角色介绍为空
    ROLE_UPDATE_INTRODUCE_EMPTY(634,"角色介绍不能为空"),


    // ------------------- 新增：PermissionForm表单验证错误码（从640开始）-------------------
    PERMISSION_NAME_TOO_LONG(640,"权限名称不能超过20个字符"),    // 新增/修改共用
    PERMISSION_URL_TOO_LONG(641,"权限URL不能超过50个字符"),      // 新增/修改共用
    PERMISSION_UPDATE_ID_EMPTY(642,"权限ID不能为空"),            // 修改场景专用
    PERMISSION_UPDATE_NAME_EMPTY(643,"权限名称不能为空"),        // 修改场景专用
    PERMISSION_UPDATE_URL_EMPTY(644,"权限URL不能为空"),          // 修改场景专用

    // ------------------- 新增：ResumeTemplate校验错误码（655开始）-------------------
    RESUME_TEMPLATE_ID_EMPTY(655,"修改简历模板必须指定模板ID"),
    RESUME_TEMPLATE_NAME_EMPTY(656,"简历模板名称不能为空"),
    RESUME_TEMPLATE_NAME_TOO_LONG(657,"简历模板名称长度不能超过25个字符"),
    RESUME_TEMPLATE_REACT_CODE_EMPTY(658,"简历模板代码不能为空"),


    // ------------------- 新增：JobAdviceArticleForm表单验证错误码（从670开始）-------------------
    JOB_ADVICE_ID_EMPTY(670,"修改求职攻略文章必须指定文章ID"),
    JOB_ADVICE_TITLE_EMPTY(671,"求职攻略文章标题不能为空"),
    JOB_ADVICE_UPDATE_TITLE_EMPTY(672,"修改时求职攻略文章标题不能为空"),
    JOB_ADVICE_TITLE_TOO_LONG(673,"求职攻略文章标题不能超过30个字符"),
    JOB_ADVICE_CATEGORY_TOO_LONG(674,"求职攻略文章分类不能超过30个字符"),
    JOB_ADVICE_TAGS_TOO_LONG(675,"求职攻略文章标签不能超过30个字符"),
    JOB_ADVICE_AUTHOR_NAME_TOO_LONG(676,"求职攻略文章作者名称不能超过30个字符"),
    RESUME_TEMPLATE_INDUSTRY_EMPTY(677, "简历模版行业不能为空"),
    // 大模型应用用户输入为空
    LLM_USERINPUT_EMPTY(901,"用户输入为空")
    ;


    private final Integer code;
    private final String message;
}
