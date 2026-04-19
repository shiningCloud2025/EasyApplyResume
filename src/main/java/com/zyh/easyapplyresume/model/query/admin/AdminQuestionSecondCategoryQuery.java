package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 题库小类查询参数
 * @author shiningCloud2025
 */
@Data
@Schema(description = "题库小类查询参数")
public class AdminQuestionSecondCategoryQuery {
    @Schema(description = "所属大类id")
    private Integer questionFirstCategoryId;

    @Schema(description = "小类名称")
    private String questionSecondCategoryName;

    @Schema(description = "小类介绍")
    private String questionSecondCategoryIntroduce;
}
