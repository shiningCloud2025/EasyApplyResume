package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 题库大类查询参数
 * @author shiningCloud2025
 */
@Data
@Schema(description = "题库大类查询参数")
public class AdminQuestionFirstCategoryQuery {
    @Schema(description = "大类名称")
    private String questionFirstCategoryName;

    @Schema(description = "大类介绍")
    private String questionFirstCategoryIntroduce;
}
