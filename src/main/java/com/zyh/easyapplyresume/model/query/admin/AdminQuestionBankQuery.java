package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 题库题目查询参数
 * @author shiningCloud2025
 */
@Data
@Schema(description = "题库题目查询参数")
public class AdminQuestionBankQuery {

    @Schema(description = "题目描述")
    private String questionBankDescription;

    @Schema(description = "题目类型")
    private Integer questionBankType;

    @Schema(description = "题目大类id")
    private Integer questionFirstCategoryId;

    @Schema(description = "题目小类id")
    private Integer questionSecondCategoryId;

    @Schema(description = "题目难度")
    private Integer questionBankDifficulty;

    @Schema(description = "题目状态")
    private Integer questionBankState;
}