package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "大学Map查询参数")
public class UniversityMapQuery {
    @Schema(description = "大学名称")
    private String universityMapName;
}
