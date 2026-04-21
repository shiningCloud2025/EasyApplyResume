package com.zyh.easyapplyresume.model.query.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户-笔试题目分页查询参数
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户-笔试题目分页查询参数")
public class UserFirstCategoryQuestionBankQuery {
    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "题库大类名称")
    private String questionFirstCategoryName;

    @Schema(description = "题库小类名称")
    private String questionSecondCategoryName;
}
