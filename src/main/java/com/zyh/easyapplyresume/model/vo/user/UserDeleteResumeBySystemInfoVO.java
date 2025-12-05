package com.zyh.easyapplyresume.model.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户删除简历信息（系统回收版）")
public class UserDeleteResumeBySystemInfoVO {

    @Schema(description = "系统删除简历id")
    private Integer userDeleteResumeBySystemId;

    @Schema(description = "系统删除简历名称")
    private String userDeleteResumeBySystemResumeName;

    @Schema(description = "系统删除简历行业")
    private String userDeleteResumeBySystemIndustryName;

    @Schema(description = "系统删除简历React组件代码")
    private String userDeleteResumeBySystemResumeReactCode;

    @Schema(description = "系统删除简历创建时间")
    private Date userDeleteResumeBySystemCreatedTime;

    @Schema(description = "系统删除简历最后更新时间")
    private Date userDeleteResumeBySystemUpdatedTime;

    @Schema(description = "系统删除简历排序序号")
    private Integer userDeleteResumeBySystemSortedNum;

    @Schema(description = "系统删除简历所属用户")
    private Integer userDeleteResumeBySystemUserId;

    @Schema(description = "简历回收时间")
    private Date userDeleteResumeBySystemRecycleTime;
}
