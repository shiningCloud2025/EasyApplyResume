package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 题库大类表单
 * @author shiningCloud2025
 */
@Data
@Schema(description = "题库大类表单参数")
public class AdminQuestionFirstCategoryForm {
    @Schema(description = "题库大类id")
    private Integer questionFirstCategoryId;

    @Schema(description = "大类名称")
    private String questionFirstCategoryName;

    @Schema(description = "大类介绍")
    private String questionFirstCategoryIntroduce;
}
