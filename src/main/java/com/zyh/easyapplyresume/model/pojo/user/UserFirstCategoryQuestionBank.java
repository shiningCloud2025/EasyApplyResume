package com.zyh.easyapplyresume.model.pojo.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户-题目答题第一大类映射表
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_firstCategory_questionBank_user")
public class UserFirstCategoryQuestionBank {
    /**
     * 用户id（逻辑外键）
     */
    @TableField("firstCategoryQuestionBankUser_userId")
    private Integer firstCategoryQuestionBankUserUserId;

    /**
     * 题目id（逻辑外键）
     */
    @TableField("firstCategoryQuestionBankUser_questionBankId")
    private Integer firstCategoryQuestionBankUserQuestionBankId;

    /**
     * 题库大类id（逻辑外键）
     */
    @TableField("firstCategoryQuestionBankUser_questionFirstCategoryId")
    private Integer firstCategoryQuestionBankUserQuestionFirstCategoryId;

    /**
     * 题库大类名称（冗余）
     */
    @TableField("firstCategoryQuestionBankUser_questionFirstCategoryName")
    private String firstCategoryQuestionBankUserQuestionFirstCategoryName;

    /**
     * 题库小类id（逻辑外键）
     */
    @TableField("firstCategoryQuestionBankUser_questionSecondCategoryId")
    private Integer firstCategoryQuestionBankUserQuestionSecondCategoryId;

    /**
     * 题库小类名称（冗余）
     */
    @TableField("firstCategoryQuestionBankUser_questionSecondCategoryName")
    private String firstCategoryQuestionBankUserQuestionSecondCategoryName;

    /**
     * 答题状态(0未作答 1已做错 2已做对)
     */
    @TableField("firstCategoryQuestionBankUser_answerStatus")
    private Integer firstCategoryQuestionBankUserAnswerStatus;

    /**
     * 创建时间
     */
    @TableField("firstCategoryQuestionBankUser_createTime")
    private LocalDateTime firstCategoryQuestionBankUserCreateTime;

    /**
     * 更新时间
     */
    @TableField("firstCategoryQuestionBankUser_updateTime")
    private LocalDateTime firstCategoryQuestionBankUserUpdateTime;
}
