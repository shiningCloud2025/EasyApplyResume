package com.zyh.easyapplyresume.bean.usallyexceptionandEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回状态码枚举类-管理端
 * @author shiningCloud2025
 */
// 0-10000
@Getter
@AllArgsConstructor
public enum AdminCodeEnum {
    // 正常
    SUCCESS(200,"OK"),
    // 系统异常
    SYSTEM_ERROR(500,"系统异常"),
    // ==================== 管理员相关(603-629)  ====================
    // ==================== 通用格式错误（新增/修改均适用）====================
    ADMIN_PHONE_FORMAT_ERROR(603, "手机号必须是11位有效数字（示例：13800138000）"),
    ADMIN_EMAIL_FORMAT_ERROR(604, "邮箱格式不正确（示例：admin@company.com）"),
    // ==================== 通用长度超限（新增/修改均适用）====================
    ADMIN_USERNAME_TOO_LONG(605, "账号名长度不能超过15个字符"),
    ADMIN_EMAIL_TOO_LONG(606, "邮箱长度不能超过25个字符"),
    ADMIN_INTRO_TOO_LONG(607, "管理员介绍长度不能超过200个字符"),
    ADMIN_PASSWORD_LENGTH_ERROR(608, "密码长度必须为6-20位（建议字母+数字组合）"),
    // ==================== 新增场景：必填项为空（账号名+手机号+邮箱）====================
    ADMIN_ADD_USERNAME_EMPTY(609, "新增管理员：账号名不能为空"),
    ADMIN_ADD_PHONE_EMPTY(610, "新增管理员：手机号不能为空"),
    ADMIN_ADD_EMAIL_EMPTY(611, "新增管理员：邮箱不能为空"), // 新增邮箱必填错误码
    // ==================== 修改场景：全字段非空（强制必填）====================
    ADMIN_UPDATE_USERNAME_EMPTY(612, "修改管理员：账号名不能为空"),
    ADMIN_UPDATE_PHONE_EMPTY(613, "修改管理员：手机号不能为空"),
    ADMIN_UPDATE_EMAIL_EMPTY(614, "修改管理员：邮箱不能为空"),
    ADMIN_UPDATE_PASSWORD_EMPTY(615, "修改管理员：密码不能为空"),
    ADMIN_UPDATE_IMAGE_EMPTY(616, "修改管理员：头像路径不能为空"),
    ADMIN_UPDATE_INTRO_EMPTY(617, "修改管理员：介绍不能为空"),
    ADMIN_UPDATE_STATE_EMPTY(618, "修改管理员：状态不能为空"),
    // ==================== 通用非法值（新增/修改均适用）====================
    ADMIN_STATE_ILLEGAL(619, "管理员状态非法（仅支持：0=禁用，1=正常）"),
    ADMIN_USERNAME_DUPLICATE(620, "账号名已存在"),
    ADMIN_PHONE_DUPLICATE(621, "手机号已存在"),
    ADMIN_EMAIL_DUPLICATE(622, "邮箱已存在"),
    DB_EXCEPTION_TRANSFORM_FAIL_EXCEPTION(623, "数据库异常没有转换成业务异常异常"),
    NO_DELETE_SUPER_ADMIN(624, "超级管理员不能被删除"),
    NO_UPDATE_SUPER_ADMIN(625, "超级管理员不能被修改"),

    // ==================== 角色相关（630-639）====================
    // 新增场景必填项为空
    ROLE_ADD_NAME_EMPTY(630, "新增角色：角色名称不能为空"),

    // 修改场景必填项为空
    ROLE_UPDATE_ID_EMPTY(631, "修改角色：角色ID不能为空"),
    ROLE_UPDATE_NAME_EMPTY(632, "修改角色：角色名称不能为空"),
    ROLE_UPDATE_INTRODUCE_EMPTY(633, "修改角色：角色简介不能为空"),

    // 长度超限
    ROLE_NAME_TOO_LONG(634, "角色名称长度不能超过12个字符"),
    ROLE_INTRODUCE_TOO_LONG(635, "角色简介长度不能超过30个字符"),
    ROLE_NAME_DUPLICATE(636, "角色名称已存在"),
    NO_UPDATE_SUPER_ADMNIN_ROLE(634, "超级管理员角色不能被修改"),
    NO_DELETE_SUPER_ADMNIN_ROLE(635, "超级管理员角色不能被删除"),


    // ==================== 权限相关（640-648）====================
    // 修改场景必填项为空（640-645）
    PERMISSION_UPDATE_ID_EMPTY(640, "修改权限：权限ID不能为空"),
    PERMISSION_UPDATE_NAME_EMPTY(643, "修改权限：权限名称不能为空"),
    PERMISSION_UPDATE_URL_EMPTY(644, "修改权限：权限URL不能为空"),
    PERMISSION_UPDATE_INTRO_EMPTY(645, "修改权限：权限简介不能为空"),

    // 新增场景必填项为空（641-642）
    PERMISSION_ADD_NAME_EMPTY(641, "新增权限：权限名称不能为空"),
    PERMISSION_ADD_URL_EMPTY(642, "新增权限：权限URL不能为空"),

    // 长度超限（646-648）
    PERMISSION_NAME_TOO_LONG(646, "权限名称长度不能超过20个字符"),
    PERMISSION_URL_TOO_LONG(647, "权限URL长度不能超过50个字符"),
    PERMISSION_INTRO_TOO_LONG(648, "权限简介长度不能超过30个字符"),
    PERMISSION_NAME_DUPLICATE(649, "权限名称已存在"), // 新增：名称唯一冲突
    PERMISSION_URL_DUPLICATE(650, "权限URL已存在"),   // 新增：URL唯一冲突


    // ------------------- 新增：ResumeTemplate校验错误码（655开始）-------------------
    RESUME_TEMPLATE_ID_EMPTY(655,"修改简历模板必须指定模板ID"),
    RESUME_TEMPLATE_NAME_EMPTY(656,"简历模板名称不能为空"),
    RESUME_TEMPLATE_NAME_TOO_LONG(657,"简历模板名称长度不能超过25个字符"),
    RESUME_TEMPLATE_REACT_CODE_EMPTY(658,"简历模板代码不能为空"),
    // 简历名称唯一约束错误
    RESUME_TEMPLATE_NAME_DUPLICATE(659, "简历模板名称已存在"),


    // ------------------- 新增：JobAdviceArticleForm表单验证错误码（从670开始）-------------------
    JOB_ADVICE_ID_EMPTY(670,"修改求职攻略文章必须指定文章ID"),
    JOB_ADVICE_TITLE_EMPTY(671,"求职攻略文章标题不能为空"),
    JOB_ADVICE_UPDATE_TITLE_EMPTY(672,"修改时求职攻略文章标题不能为空"),
    JOB_ADVICE_TITLE_TOO_LONG(673,"求职攻略文章标题不能超过30个字符"),
    JOB_ADVICE_CATEGORY_TOO_LONG(674,"求职攻略文章分类不能超过30个字符"),
    JOB_ADVICE_TAGS_TOO_LONG(675,"求职攻略文章标签不能超过30个字符"),
    JOB_ADVICE_AUTHOR_NAME_TOO_LONG(676,"求职攻略文章作者名称不能超过30个字符"),
    RESUME_TEMPLATE_INDUSTRY_EMPTY(677, "简历模版行业不能为空"),

    // 招聘岗位校验相关错误码（685-692）
    RECRUIT_UPDATE_ID_EMPTY(685, "修改招聘岗位必须指定岗位ID"),
    RECRUIT_POSITION_NAME_EMPTY(686, "招聘岗位名称不能为空"),
    RECRUIT_INDUSTRY_CODE_EMPTY(687, "招聘岗位所属行业代码不能为空"),
    RECRUIT_MIN_SALARY_EMPTY(688, "招聘岗位最低月薪不能为空"),
    RECRUIT_MAX_SALARY_EMPTY(689, "招聘岗位最高月薪不能为空"),
    RECRUIT_WORK_DAY_EMPTY(690, "招聘岗位每周工作天数不能为空"),
    RECRUIT_POSITION_NAME_TOO_LONG(691, "招聘岗位名称不能超过30个字符"),
    RECRUIT_GOOD_WELFARE_TOO_LONG(692, "福利待遇不能超过200个字符"),
    NOT_DELETE_RECRUIT_POSITION(693, "该岗位正在被使用，请勿删除"),
    RECRUIT_POSITION_DUPLICATE(694, "同一行业下该招聘岗位名称已存在"),

    // ------------------- 新增：EmploymentInformationForm表单验证错误码（705开始）-------------------
    EMPLOYMENT_UPDATE_ID_EMPTY(705, "修改招聘信息必须指定招聘信息ID"),
    EMPLOYMENT_COMPANY_NAME_EMPTY(706, "公司名称不能为空"),
    EMPLOYMENT_COMPANY_NAME_TOO_LONG(707, "公司名称长度不能超过30个字符"),
    EMPLOYMENT_INDUSTRY_CATEGORIES_EMPTY(708, "行业大类不能为空"),
    EMPLOYMENT_COMPANY_TYPE_EMPTY(709, "企业性质不能为空"),
    EMPLOYMENT_BATCH_EMPTY(710, "招聘批次不能为空"),
    EMPLOYMENT_RECRUIT_POSITION_EMPTY(711, "招聘岗位不能为空"),
    EMPLOYMENT_RECRUIT_OBJECT_EMPTY(712, "招聘对象不能为空"),
    EMPLOYMENT_RECRUIT_LOCATION_FIRST_EMPTY(713, "招聘地址(省级)不能为空"),
    EMPLOYMENT_RECRUIT_LOCATION_FIRST_ELEMENT_NULL(714, "省级地址ID存在空值"),
    EMPLOYMENT_RECRUIT_LOCATION_SECOND_EMPTY(715, "招聘地址(市级)不能为空"),
    EMPLOYMENT_RECRUIT_LOCATION_SECOND_ELEMENT_NULL(716, "市级地址ID存在空值"),
    EMPLOYMENT_STOP_TIME_EMPTY(717, "截止时间不能为空"),
    EMPLOYMENT_ONLINE_APP_STATUS_EMPTY(718, "网申状态不能为空"),
    EMPLOYMENT_ONLINE_APP_STATUS_TOO_LONG(719, "网申状态长度不能超过30个字符"),
    EMPLOYMENT_SUBMISSION_WAY_EMPTY(720, "投递方式不能为空"),
    EMPLOYMENT_SUBMISSION_WAY_TOO_LONG(721, "投递方式长度不能超过1024个字符"),
    EMPLOYMENT_OFFICIAL_ANNOUNCEMENT_TOO_LONG(722, "官方公告长度不能超过1024个字符"),
    EMPLOYMENT_REFERRAL_CODE_TOO_LONG(723, "内推码长度不能超过255个字符"),
    EMPLOYMENT_RECRUIT_LOCATION_DETAIL_TOO_LONG(724, "详细招聘地址长度不能超过255个字符"),
    EMPLOYMENT_LOCATION_LENGTH_NOT_MATCH(725, "招聘地址(省级)和(市级)长度不一致"),
    EMPLOYMENT_COMPANY_NAME_DUPLICATE(726, "该公司名称已存在"),
    EMPLOYMENT_SUBMISSION_WAY_DUPLICATE(727, "该投递方式已存在"),


    // -------------------- 新增：邮箱验证相关状态 (从750开始)-------------------
    EMAIL_NO_EFFECT(750, "请输入有效的邮箱地址"),
    EMAIL_SEND_FREQUENCY(751, "验证码已发送，请3分钟后再试"), // 带参数的提示
    EMAIL_VERIFY_SEND_FAIL(752, "验证码发送失败，请检查邮箱地址或稍后重试"),
    EMAIL_VERIFY_CODE_INVALID(753, "验证码无效或已过期"),
    EMAIL_VERIFY_CODE_SUCCESS(754, "验证码验证成功"),
    SENDER_EMAIL_NOT_NULL(755, "您还没有绑定邮箱,请去绑定邮箱"),
    RECIVEDER_EMAIL_NOT_NULL(756, "请输入接收人邮箱"),
    SEND_TITLE_NOT_NULL(757, "请输入发送标题"),
    SEND_CONTENT_NOT_NULL(758, "请输入发送内容"),
    SEND_COMMUNICATION_EMAIL_FAIL(759, "发送沟通邮件失败"),

    // ==================== 短信相关枚举（从 770 开始）====================
    SMS_PHONE_FORMAT_ERROR(770, "手机号格式不正确，请输入11位有效手机号"),
    SMS_SEND_FREQUENCY(771, "短信发送过于频繁，请3分钟后再试"),
    SMS_SEND_FAIL(772, "短信发送失败，请稍后重试"),
    SMS_VERIFY_CODE_INVALID(773, "短信验证码无效或已过期"),
    SMS_CONFIG_ERROR(774, "短信服务配置缺失，请联系管理员"),

    // ===================== 新增：登录业务（从785开始）=====================
    ACCOUNT_OR_PASSWORD_ERROR(785, "账号或密码错误"),
    NO_REGISTER_ERROR(786, "管理员未注册"),
    GENERATE_ACCOUNT_FAIL(787, "生成管理员账号失败"),
    ADMIN_ADD_ACCOUNT_EMPTY(788, "请输入管理员账号"),
    ADMIN_ACCOUNT_LENGTH_ERROR(789, "管理员账号长度要在7-10位之间"),

    // ===================== 新增：公告（从800开始）=====================
    ADMIN_ALREADY_ADD_ANNOUNCEMENT(800, "管理员已添加过该公告"),
    ADMIN_ADD_ANNOUNCEMENT_FAIL(801, "管理员添加公告失败"),
    ANNOUNCEMENT_TITLE_EMPTY(802, "公告标题不能为空"),
    ANNOUNCEMENT_TITLE_TOO_LONG(803, "公告标题不能超过35个字符"),
    ANNOUNCEMENT_CONTENT_EMPTY(804, "修改公告时内容不能为空"),
    ADMIN_UPDATE_ANNOUNCEMENT_FAIL(805, "管理员修改公告失败"),
    ADMIN_GET_ANNOUNCEMENT_INFO_FAIL(806, "管理员获取公告信息失败"),

    // ===================== 新增：反馈业务（从810开始）=====================
    ADMIN_FEEDBACK_TITLE_NOT_NULL(810, "反馈标题不能为空"),
    ADMIN_FEEDBACK_CONTENT_NOT_NULL(811, "反馈内容不能为空"),
    ADMIN_FEEDBACK_CONTENT_EMPTY(812, "反馈内容不能为空"),
    ADMIN_FEEDBACK_ADMIN_ID_EMPTY(813, "提交反馈的管理员ID不能为空"),
    ADMIN_FEEDBACK_TITLE_TOO_LONG(814, "反馈标题不能超过35个字符"),

    // ===================== 人工客服相关（从820开始）=====================
    CUSTOMER_SERVICE_ALREADY_ADD(820, "已添加过人工客服信息"),
    CUSTOMER_SERVICE_ADD_FAIL(821, "添加人工客服失败"),
    CUSTOMER_SERVICE_TITLE_EMPTY(822, "人工客服标题不能为空"),
    CUSTOMER_SERVICE_TITLE_TOO_LONG(823, "人工客服标题不能超过35个字符"),
    CUSTOMER_SERVICE_CONTENT_EMPTY(824, "修改人工客服时内容不能为空"),
    CUSTOMER_SERVICE_UPDATE_FAIL(825, "修改人工客服失败"),
    CUSTOMER_SERVICE_GET_INFO_FAIL(826, "获取人工客服信息失败"),

    // ===================== 发展历程相关（从830开始）=====================
    DEVELOP_HISTORY_ALREADY_ADD(830, "已添加过发展历程信息"),
    DEVELOP_HISTORY_ADD_FAIL(831, "添加发展历程失败"),
    DEVELOP_HISTORY_TITLE_EMPTY(832, "发展历程标题不能为空"),
    DEVELOP_HISTORY_TITLE_TOO_LONG(833, "发展历程标题不能超过35个字符"),
    DEVELOP_HISTORY_CONTENT_EMPTY(834, "修改发展历程时内容不能为空"),
    DEVELOP_HISTORY_UPDATE_FAIL(835, "修改发展历程失败"),
    DEVELOP_HISTORY_GET_INFO_FAIL(836, "获取发展历程信息失败"),

    // ===================== 加入我们相关（从840开始）=====================
    JOIN_US_ALREADY_ADD(840, "已添加过加入我们信息"),
    JOIN_US_ADD_FAIL(841, "添加加入我们失败"),
    JOIN_US_TITLE_EMPTY(842, "加入我们标题不能为空"),
    JOIN_US_TITLE_TOO_LONG(843, "加入我们标题不能超过35个字符"),
    JOIN_US_CONTENT_EMPTY(844, "修改加入我们时内容不能为空"),
    JOIN_US_UPDATE_FAIL(845, "修改加入我们失败"),
    JOIN_US_GET_INFO_FAIL(846, "获取加入我们信息失败"),

    // ===================== 媒体报道相关（从850开始）=====================
    MEDIA_REPORT_ALREADY_ADD(850, "已添加过媒体报道信息"),
    MEDIA_REPORT_ADD_FAIL(851, "添加媒体报道失败"),
    MEDIA_REPORT_TITLE_EMPTY(852, "媒体报道标题不能为空"),
    MEDIA_REPORT_TITLE_TOO_LONG(853, "媒体报道标题不能超过35个字符"),
    MEDIA_REPORT_CONTENT_EMPTY(854, "修改媒体报道时内容不能为空"),
    MEDIA_REPORT_UPDATE_FAIL(855, "修改媒体报道失败"),
    MEDIA_REPORT_GET_INFO_FAIL(856, "获取媒体报道信息失败"),

    // ===================== 合作伙伴相关（从860开始）=====================
    PARTNER_INTRODUCE_ALREADY_ADD(860, "已添加过合作伙伴信息"),
    PARTNER_INTRODUCE_ADD_FAIL(861, "添加合作伙伴失败"),
    PARTNER_INTRODUCE_TITLE_EMPTY(862, "合作伙伴标题不能为空"),
    PARTNER_INTRODUCE_TITLE_TOO_LONG(863, "合作伙伴标题不能超过35个字符"),
    PARTNER_INTRODUCE_CONTENT_EMPTY(864, "修改合作伙伴时内容不能为空"),
    PARTNER_INTRODUCE_UPDATE_FAIL(865, "修改合作伙伴失败"),
    PARTNER_INTRODUCE_GET_INFO_FAIL(866, "获取合作伙伴信息失败"),

    // ===================== 项目介绍相关（从870开始）=====================
    PROJECT_INTRODUCE_ALREADY_ADD(870, "已添加过项目介绍信息"),
    PROJECT_INTRODUCE_ADD_FAIL(871, "添加项目介绍失败"),
    PROJECT_INTRODUCE_TITLE_EMPTY(872, "项目介绍标题不能为空"),
    PROJECT_INTRODUCE_TITLE_TOO_LONG(873, "项目介绍标题不能超过35个字符"),
    PROJECT_INTRODUCE_CONTENT_EMPTY(874, "修改项目介绍时内容不能为空"),
    PROJECT_INTRODUCE_UPDATE_FAIL(875, "修改项目介绍失败"),
    PROJECT_INTRODUCE_GET_INFO_FAIL(876, "获取项目介绍信息失败"),

    // ===================== 团队介绍相关（从880开始）=====================
    TEAM_INTRODUCE_ALREADY_ADD(880, "已添加过团队介绍信息"),
    TEAM_INTRODUCE_ADD_FAIL(881, "添加团队介绍失败"),
    TEAM_INTRODUCE_TITLE_EMPTY(882, "团队介绍标题不能为空"),
    TEAM_INTRODUCE_TITLE_TOO_LONG(883, "团队介绍标题不能超过35个字符"),
    TEAM_INTRODUCE_CONTENT_EMPTY(884, "修改团队介绍时内容不能为空"),
    TEAM_INTRODUCE_UPDATE_FAIL(885, "修改团队介绍失败"),
    TEAM_INTRODUCE_GET_INFO_FAIL(886, "获取团队介绍信息失败"),

    // ===================== 常见问题相关（从890开始）=====================
    FAQ_TITLE_EMPTY(890, "常见问题标题不能为空"),
    FAQ_TITLE_TOO_LONG(891, "常见问题标题不能超过35个字符"),
    FAQ_CONTENT_EMPTY(892, "修改常见问题时内容不能为空"),
    FAQ_ADD_FAIL(893, "添加常见问题失败"),
    FAQ_UPDATE_FAIL(894, "修改常见问题失败"),
    FAQ_DELETE_FAIL(895, "删除常见问题失败"),
    FAQ_GET_INFO_FAIL(896, "获取常见问题信息失败"),
    FAQ_GET_PAGE_FAIL(897, "分页查询常见问题失败"),
    FAQ_NOT_FOUND(898, "常见问题不存在"),

    // ===================== 使用指南相关（从900开始）=====================
    USER_GUIDE_TITLE_EMPTY(900, "使用指南标题不能为空"),
    USER_GUIDE_TITLE_TOO_LONG(901, "使用指南标题不能超过35个字符"),
    USER_GUIDE_CONTENT_EMPTY(902, "修改使用指南时内容不能为空"),
    USER_GUIDE_ADD_FAIL(903, "添加使用指南失败"),
    USER_GUIDE_UPDATE_FAIL(904, "修改使用指南失败"),
    USER_GUIDE_DELETE_FAIL(905, "删除使用指南失败"),
    USER_GUIDE_GET_INFO_FAIL(906, "获取使用指南信息失败"),
    USER_GUIDE_GET_PAGE_FAIL(907, "分页查询使用指南失败"),
    USER_GUIDE_NOT_FOUND(908, "使用指南不存在"),

    // ===================== 题库大类相关（从910开始）=====================
    QUESTION_FIRST_CATEGORY_NAME_EMPTY(910, "题库大类名称不能为空"),
    QUESTION_FIRST_CATEGORY_NAME_TOO_LONG(911, "题库大类名称不能超过20个字符"),
    QUESTION_FIRST_CATEGORY_INTRO_EMPTY(912, "题库大类介绍不能为空"),
    QUESTION_FIRST_CATEGORY_INTRO_TOO_LONG(913, "题库大类介绍不能超过60个字符"),
    QUESTION_FIRST_CATEGORY_NAME_DUPLICATE(914, "题库大类名称已存在"),
    QUESTION_FIRST_CATEGORY_NOT_FOUND(919, "题库大类不存在"),
    QUESTION_FIRST_CATEGORY_ADD_FAIL(920, "新增题库大类失败"),
    QUESTION_FIRST_CATEGORY_UPDATE_FAIL(921, "修改题库大类失败"),
    QUESTION_FIRST_CATEGORY_DELETE_FAIL(922, "删除题库大类失败"),
    QUESTION_FIRST_CATEGORY_PAGE_FAIL(923, "分页查询题库大类失败"),
    QUESTION_FIRST_CATEGORY_INFO_FAIL(924, "查询题库大类详情失败"),

    // ===================== 题库小类相关（从930开始）=====================
    QUESTION_SECOND_CATEGORY_FIRST_CATEGORY_ID_EMPTY(930, "题库小类所属大类不能为空"),
    QUESTION_SECOND_CATEGORY_NAME_EMPTY(931, "题库小类名称不能为空"),
    QUESTION_SECOND_CATEGORY_NAME_TOO_LONG(932, "题库小类名称不能超过20个字符"),
    QUESTION_SECOND_CATEGORY_INTRO_EMPTY(933, "题库小类介绍不能为空"),
    QUESTION_SECOND_CATEGORY_INTRO_TOO_LONG(934, "题库小类介绍不能超过60个字符"),
    QUESTION_SECOND_CATEGORY_NAME_DUPLICATE(935, "题库小类名称已存在"),
    QUESTION_SECOND_CATEGORY_NOT_FOUND(936, "题库小类不存在"),
    QUESTION_SECOND_CATEGORY_ADD_FAIL(937, "新增题库小类失败"),
    QUESTION_SECOND_CATEGORY_UPDATE_FAIL(938, "修改题库小类失败"),
    QUESTION_SECOND_CATEGORY_DELETE_FAIL(939, "删除题库小类失败"),
    QUESTION_SECOND_CATEGORY_PAGE_FAIL(940, "分页查询题库小类失败"),
    QUESTION_SECOND_CATEGORY_INFO_FAIL(941, "查询题库小类详情失败"),

    // ===================== 简历评分训练数据相关（从950开始）=====================
    SCORE_TRAINING_DATA_RESUME_NAME_EMPTY(950, "简历名称不能为空"),
    SCORE_TRAINING_DATA_RESUME_NAME_TOO_LONG(951, "简历名称不能超过25个字符"),
    SCORE_TRAINING_DATA_INDUSTRY_NAME_EMPTY(952, "行业名称不能为空"),
    SCORE_TRAINING_DATA_INDUSTRY_NAME_TOO_LONG(953, "行业名称不能超过35个字符"),
    SCORE_TRAINING_DATA_RESUME_CONTENT_EMPTY(954, "简历内容不能为空"),
    SCORE_TRAINING_DATA_LABEL_SCORE_EMPTY(955, "训练标签分数不能为空"),
    SCORE_TRAINING_DATA_DATA_SOURCE_EMPTY(956, "数据来源不能为空"),
    SCORE_TRAINING_DATA_DATA_SOURCE_INVALID(957, "数据来源不合法"),
    SCORE_TRAINING_DATA_NOT_FOUND(958, "简历评分训练数据不存在"),

    // ===================== 简历评分模型训练代码相关（从960开始）=====================
    SCORE_MODEL_TRAIN_CODE_ID_EMPTY(960, "训练代码id不能为空"),
    SCORE_MODEL_TRAIN_CODE_NAME_EMPTY(961, "训练代码名称不能为空"),
    SCORE_MODEL_TRAIN_CODE_NAME_TOO_LONG(962, "训练代码名称不能超过64个字符"),
    SCORE_MODEL_TRAIN_CODE_VERSION_EMPTY(963, "训练代码版本号不能为空"),
    SCORE_MODEL_TRAIN_CODE_VERSION_TOO_LONG(964, "训练代码版本号不能超过64个字符"),
    SCORE_MODEL_TRAIN_CODE_LANGUAGE_EMPTY(965, "训练代码语言不能为空"),
    SCORE_MODEL_TRAIN_CODE_LANGUAGE_TOO_LONG(966, "训练代码语言不能超过32个字符"),
    SCORE_MODEL_TRAIN_CODE_CONTENT_EMPTY(967, "训练代码内容不能为空"),
    SCORE_MODEL_TRAIN_CODE_DESC_EMPTY(968, "训练代码描述不能为空"),
    SCORE_MODEL_TRAIN_CODE_DESC_TOO_LONG(969, "训练代码描述不能超过500个字符"),
    SCORE_MODEL_TRAIN_CODE_NOT_FOUND(970, "训练代码不存在"),
    SCORE_MODEL_TRAIN_CODE_FILE_NAME_EMPTY(971, "训练代码中的文件名不能为空"),
    SCORE_MODEL_TRAIN_CODE_FILE_BLOCK_INVALID(972, "训练代码内容格式不正确"),

    // ===================== 题库题目相关（从980开始）=====================
    QUESTION_BANK_FORM_EMPTY(980, "题库题目表单不能为空"),
    QUESTION_BANK_ID_EMPTY(981, "题库题目id不能为空"),
    QUESTION_BANK_TYPE_EMPTY(982, "题目类型不能为空"),
    QUESTION_BANK_TYPE_INVALID(983, "题目类型不合法"),
    QUESTION_BANK_DESCRIPTION_EMPTY(984, "题目描述不能为空"),
    QUESTION_BANK_DESCRIPTION_TOO_LONG(985, "题目描述不能超过2000个字符"),
    QUESTION_BANK_OPTION_A_EMPTY(986, "题目选项A不能为空"),
    QUESTION_BANK_OPTION_B_EMPTY(987, "题目选项B不能为空"),
    QUESTION_BANK_OPTION_C_EMPTY(988, "题目选项C不能为空"),
    QUESTION_BANK_OPTION_D_EMPTY(989, "题目选项D不能为空"),
    QUESTION_BANK_OPTION_A_TOO_LONG(990, "题目选项A不能超过255个字符"),
    QUESTION_BANK_OPTION_B_TOO_LONG(991, "题目选项B不能超过255个字符"),
    QUESTION_BANK_OPTION_C_TOO_LONG(992, "题目选项C不能超过255个字符"),
    QUESTION_BANK_OPTION_D_TOO_LONG(993, "题目选项D不能超过255个字符"),
    QUESTION_BANK_CORRECT_ANSWER_EMPTY(994, "题目正确答案不能为空"),
    QUESTION_BANK_CORRECT_ANSWER_TOO_LONG(995, "题目正确答案不能超过100个字符"),
    QUESTION_BANK_CORRECT_ANSWER_INVALID(996, "题目正确答案格式不正确"),
    QUESTION_BANK_IMAGE_TOO_LONG(997, "题目图片不能超过1024个字符"),
    QUESTION_BANK_FIRST_CATEGORY_ID_EMPTY(998, "题目大类id不能为空"),
    QUESTION_BANK_FIRST_CATEGORY_NAME_EMPTY(999, "题目大类名称不能为空"),
    QUESTION_BANK_FIRST_CATEGORY_NAME_TOO_LONG(1000, "题目大类名称不能超过20个字符"),
    QUESTION_BANK_SECOND_CATEGORY_ID_EMPTY(1001, "题目小类id不能为空"),
    QUESTION_BANK_SECOND_CATEGORY_NAME_EMPTY(1002, "题目小类名称不能为空"),
    QUESTION_BANK_SECOND_CATEGORY_NAME_TOO_LONG(1003, "题目小类名称不能超过20个字符"),
    QUESTION_BANK_REFERENCE_ANSWER_EMPTY(1004, "题目参考答案不能为空"),
    QUESTION_BANK_REFERENCE_ANSWER_TOO_LONG(1005, "题目参考答案不能超过4000个字符"),
    QUESTION_BANK_REFERENCE_ANSWER_MUST_EMPTY(1006, "当前题型题目参考答案必须为空"),
    QUESTION_BANK_ANALYSIS_TOO_LONG(1007, "题目解析不能超过2000个字符"),
    QUESTION_BANK_DIFFICULTY_EMPTY(1008, "题目难度不能为空"),
    QUESTION_BANK_STATE_EMPTY(1009, "题目状态不能为空"),
    QUESTION_BANK_MULTIPLE_ANSWER_INVALID(1010, "多选题正确答案格式不正确，至少两个答案并使用中文逗号分隔"),
    QUESTION_BANK_JUDGE_OPTION_INVALID(1011, "判断题只能填写A、B两个选项，C、D必须为空"),
    QUESTION_BANK_OPTION_MUST_EMPTY(1012, "当前题型选项必须为空"),
    QUESTION_BANK_CORRECT_ANSWER_MUST_EMPTY(1013, "当前题型不能填写正确答案"),
    QUESTION_BANK_NOT_FOUND(1014, "题库题目不存在"),
    QUESTION_BANK_ADD_FAIL(1015, "新增题库题目失败"),
    QUESTION_BANK_UPDATE_FAIL(1016, "修改题库题目失败"),
    QUESTION_BANK_DELETE_FAIL(1017, "删除题库题目失败"),
    QUESTION_BANK_PAGE_FAIL(1018, "分页查询题库题目失败"),
    QUESTION_BANK_INFO_FAIL(1019, "查询题库题目详情失败"),

    // ==================== 用户-笔试第一大类答题映射相关（1050开始）====================
    USER_FIRST_CATEGORY_QUESTION_BANK_USER_ID_EMPTY(1050, "用户id不能为空"),
    USER_FIRST_CATEGORY_QUESTION_BANK_ID_EMPTY(1051, "题目id不能为空"),
    USER_FIRST_CATEGORY_QUESTION_BANK_NOT_FOUND(1052, "用户笔试题目记录不存在"),
    USER_FIRST_CATEGORY_QUESTION_BANK_FIRST_CATEGORY_NAME_TOO_LONG(1053, "题库大类名称不能超过20个字符"),
    USER_FIRST_CATEGORY_QUESTION_BANK_SECOND_CATEGORY_NAME_TOO_LONG(1054, "题库小类名称不能超过20个字符"),
    USER_FIRST_CATEGORY_QUESTION_BANK_DELETE_FAIL(1055, "删除用户笔试题目记录失败"),
    USER_FIRST_CATEGORY_QUESTION_BANK_INFO_FAIL(1056, "查询用户笔试题目详情失败"),
    USER_FIRST_CATEGORY_QUESTION_BANK_PAGE_FAIL(1057, "分页查询用户笔试题目失败"),

    // ===================== 简历评分模型版本相关（从1080开始）=====================
    SCORE_MODEL_VERSION_ID_EMPTY(1080, "模型版本id不能为空"),
    SCORE_MODEL_VERSION_MODEL_NAME_EMPTY(1081, "模型名称不能为空"),
    SCORE_MODEL_VERSION_MODEL_NAME_TOO_LONG(1082, "模型名称不能超过64个字符"),
    SCORE_MODEL_VERSION_VERSION_EMPTY(1083, "模型版本号不能为空"),
    SCORE_MODEL_VERSION_VERSION_TOO_LONG(1084, "模型版本号不能超过64个字符"),
    SCORE_MODEL_VERSION_MODEL_TYPE_EMPTY(1085, "模型类型不能为空"),
    SCORE_MODEL_VERSION_MODEL_TYPE_TOO_LONG(1086, "模型类型不能超过32个字符"),
    SCORE_MODEL_VERSION_MODEL_TYPE_INVALID(1087, "模型类型格式不正确"),
    SCORE_MODEL_VERSION_EMBEDDING_MODEL_EMPTY(1088, "使用的embedding模型不能为空"),
    SCORE_MODEL_VERSION_EMBEDDING_MODEL_TOO_LONG(1089, "使用的embedding模型不能超过64个字符"),
    SCORE_MODEL_VERSION_SAMPLE_COUNT_EMPTY(1090, "训练样本数量不能为空"),
    SCORE_MODEL_VERSION_SAMPLE_COUNT_INVALID(1091, "训练样本数量不能小于0"),
    SCORE_MODEL_VERSION_TRAIN_COST_MS_EMPTY(1092, "训练耗时毫秒不能为空"),
    SCORE_MODEL_VERSION_TRAIN_COST_MS_INVALID(1093, "训练耗时毫秒不能小于0"),
    SCORE_MODEL_VERSION_METRIC_JSON_EMPTY(1094, "评估指标JSON不能为空"),
    SCORE_MODEL_VERSION_IS_ACTIVE_EMPTY(1095, "是否当前启用版本不能为空"),
    SCORE_MODEL_VERSION_IS_ACTIVE_INVALID(1096, "是否当前启用版本取值不合法"),
    SCORE_MODEL_VERSION_ACTIVE_DUPLICATE(1097, "当前仅允许一个启用中的模型版本"),
    SCORE_MODEL_VERSION_MODEL_FILE_EMPTY(1098, "模型文件不能为空"),
    SCORE_MODEL_VERSION_MODEL_FILE_NAME_EMPTY(1099, "模型文件名不能为空"),
    SCORE_MODEL_VERSION_MODEL_FILE_NAME_TOO_LONG(1100, "模型文件名不能超过512个字符"),
    SCORE_MODEL_VERSION_COUNT_LIMIT(1101, "模型文件最多只能保留10个"),
    SCORE_MODEL_VERSION_NOT_FOUND(1102, "模型版本不存在"),

    ;

    private final Integer code;
    private final String message;
}
