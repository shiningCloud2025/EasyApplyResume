package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 题库大类分页信息
 * @author shiningCloud2025
 */
@Data
@Schema(description = "题库大类分页信息")
public class AdminQuestionFirstCategoryPageVO {
    @Schema(description = "题库大类id")
    private Integer questionFirstCategoryId;

    @Schema(description = "大类名称")
    private String questionFirstCategoryName;

    @Schema(description = "大类介绍")
    private String questionFirstCategoryIntroduce;

    @Schema(description = "大类创建时间")
    private LocalDateTime questionFirstCategoryCreateTime;
}
