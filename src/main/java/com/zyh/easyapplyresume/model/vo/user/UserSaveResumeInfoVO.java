package com.zyh.easyapplyresume.model.vo.user;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
public class UserSaveResumeInfoVO {
    @Schema(description = "简历主键ID")
    private Integer userSaveResumeId;

    @Schema(description = "简历名称")
    private String userSaveResumeResumeName;

    @Schema(description = "简历行业")
    private Integer userSaveResumeIndustry;

    @Schema(description = "简历React组件代码")
    private String userSaveResumeResumeReactCode;

    @Schema(description = "简历创建时间")
    private Date userSaveResumeCreatedTime;

    @Schema(description = "简历最后更新时间")
    private Date userSaveResumeUpdatedTime;

    @Schema(description = "简历排序序号")
    private Integer userSaveResumeSortedNum;

    @Schema(description = "简历所属用户")
    private Integer userSaveResumeUserId;
}
