package com.zyh.easyapplyresume.model.query.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户端题库题目分页查询参数
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户端题库题目分页查询参数")
public class UserQuestionBankQuery {

    @Schema(description = "题目大类id")
    private Integer questionFirstCategoryId;

    @Schema(description = "题目小类id")
    private Integer questionSecondCategoryId;

    @Schema(description = "题目类型")
    private Integer questionBankType;

    @Schema(description = "题目难度")
    private Integer questionBankDifficulty;

    @Schema(description = "答题状态(0未开始 1进行中 2已完成)")
    private Integer questionBankAnswerStatus;

    @Schema(description = "题目描述")
    private String questionBankDescription;
}
