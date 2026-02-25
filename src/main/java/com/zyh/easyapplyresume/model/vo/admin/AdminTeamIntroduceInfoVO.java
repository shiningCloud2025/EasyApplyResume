package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "团队介绍信息")
public class AdminTeamIntroduceInfoVO {
    @Schema(description = "团队介绍id")
    private Integer teamIntroduceId;

    @Schema(description = "团队介绍标题")
    private String teamIntroduceTitle;

    @Schema(description = "团队介绍内容")
    private String teamIntroduceContent;

    @Schema(description = "团队介绍图片")
    private String teamIntroduceImage;

    @Schema(description = "团队介绍更新时间")
    private Date teamIntroduceUpdatedTime;
}
