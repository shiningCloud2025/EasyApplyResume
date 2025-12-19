package com.zyh.easyapplyresume.model.query.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户保存的简历查询参数")
public class UserSaveResumeQuery {
    @Schema(description = "简历名称")
    private String userSaveResumeResumeName;

    @Schema(description = "简历行业")
    private Integer userSaveResumeIndustry;

}
