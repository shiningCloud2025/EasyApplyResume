package com.zyh.easyapplyresume.model.query.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户删除简历查询-C端")
public class CPortUserDeleteResumeQuery {
    @Schema(description = "简历名称")
    private String userSaveResumeResumeName;

    @Schema(description = "简历行业")
    private Integer userSaveResumeIndustry;
}
