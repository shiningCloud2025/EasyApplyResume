package com.zyh.easyapplyresume.model.form.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 题库小类表单
 * @author shiningCloud2025
 */
@Data
@Schema(description = "题库小类表单参数")
public class AdminQuestionSecondCategoryForm {
    @Schema(description = "题库小类id")
    private Integer questionSecondCategoryId;

    @Schema(description = "所属大类id")
    private Integer questionFirstCategoryId;

    @Schema(description = "小类名称")
    private String questionSecondCategoryName;

    @Schema(description = "小类介绍")
    private String questionSecondCategoryIntroduce;
}
