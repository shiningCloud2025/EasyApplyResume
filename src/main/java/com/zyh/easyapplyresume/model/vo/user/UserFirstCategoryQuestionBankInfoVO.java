package com.zyh.easyapplyresume.model.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户-笔试题目详情
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户-笔试题目详情")
public class UserFirstCategoryQuestionBankInfoVO {
    @Schema(description = "用户id")
    private Integer firstCategoryQuestionBankUserUserId;

    @Schema(description = "题目id")
    private Integer firstCategoryQuestionBankUserQuestionBankId;

    @Schema(description = "题库大类id")
    private Integer firstCategoryQuestionBankUserQuestionFirstCategoryId;

    @Schema(description = "题库大类名称")
    private String firstCategoryQuestionBankUserQuestionFirstCategoryName;

    @Schema(description = "题库小类id")
    private Integer firstCategoryQuestionBankUserQuestionSecondCategoryId;

    @Schema(description = "题库小类名称")
    private String firstCategoryQuestionBankUserQuestionSecondCategoryName;

    @Schema(description = "答题状态(0未开始 1进行中 2已完成)")
    private Integer firstCategoryQuestionBankUserAnswerStatus;

    @Schema(description = "创建时间")
    private LocalDateTime firstCategoryQuestionBankUserCreateTime;

    @Schema(description = "更新时间")
    private LocalDateTime firstCategoryQuestionBankUserUpdateTime;
}
