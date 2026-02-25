package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "使用指南查询")
public class AdminUserGuideQuery {
    @Schema(description = "使用指南标题")
    private String userGuideTitle;
}
