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

    ;

    private final Integer code;
    private final String message;
}
