package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "项目介绍信息")
public class AdminProjectIntroduceInfoVO {
    @Schema(description = "项目介绍id")
    private Integer projectIntroduceId;

    @Schema(description = "项目介绍标题")
    private String projectIntroduceTitle;

    @Schema(description = "项目介绍内容")
    private String projectIntroduceContent;

    @Schema(description = "项目介绍更新时间")
    private Date projectIntroduceUpdatedTime;
}
