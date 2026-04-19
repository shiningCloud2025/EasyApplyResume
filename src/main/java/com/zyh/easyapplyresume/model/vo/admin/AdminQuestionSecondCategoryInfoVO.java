package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 题库小类详情信息
 * @author shiningCloud2025
 */
@Data
@Schema(description = "题库小类详情信息")
public class AdminQuestionSecondCategoryInfoVO {
    @Schema(description = "题库小类id")
    private Integer questionSecondCategoryId;

    @Schema(description = "所属大类id")
    private Integer questionFirstCategoryId;

    @Schema(description = "小类名称")
    private String questionSecondCategoryName;

    @Schema(description = "小类介绍")
    private String questionSecondCategoryIntroduce;

    @Schema(description = "小类创建时间")
    private LocalDateTime questionSecondCategoryCreateTime;
}
