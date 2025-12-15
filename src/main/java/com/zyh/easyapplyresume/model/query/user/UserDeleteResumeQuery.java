package com.zyh.easyapplyresume.model.query.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户删除简历查询")
public class UserDeleteResumeQuery {
    @Schema(description = "系统删除简历名称")
    private String userDeleteResumeBySystemResumeName;


    @Schema(description = "系统删除简历所属用户")
    private String userDeleteResumeBySystemUserId;


    @Schema(description = "简历回收时间")
    private Date userDeleteResumeBySystemRecycleTime;
}
