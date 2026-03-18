package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "加入我们信息")
public class AdminJoinUsInfoVO {
    @Schema(description = "加入我们id")
    private Integer joinUsId;

    @Schema(description = "加入我们标题")
    private String joinUsTitle;

    @Schema(description = "加入我们内容")
    private String joinUsContent;

    @Schema(description = "加入我们更新时间")
    private LocalDateTime joinUsUpdatedTime;
}
