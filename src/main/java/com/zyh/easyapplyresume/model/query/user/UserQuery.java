package com.zyh.easyapplyresume.model.query.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户查询表单")
public class UserQuery {

    @Schema(description = "用户名称")
    private String userUsername;

    @Schema(description = "用户邮箱")
    private String userEmail;

    @Schema(description = "用户手机")
    private String userPhone;


}
