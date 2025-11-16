package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户分页信息")
public class UserPageVO {

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "用户名称")
    private String userUsername;

    @Schema(description = "用户邮箱")
    private String userEmail;

    @Schema(description = "用户手机")
    private String userPhone;

    @Schema(description = "创建时间")
    private Date userCreatedTime;

    @Schema(description = "用户最近登录时间")
    private Date userLoginTime;

}
