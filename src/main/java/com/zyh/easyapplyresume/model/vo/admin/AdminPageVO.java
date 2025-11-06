package com.zyh.easyapplyresume.model.vo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "管理员分页信息")
public class AdminPageVO {
    @Schema(description = "管理员id")
    private Integer adminId;

    @Schema(description = "管理员名称")
    private String adminUsername;

    @Schema(description = "管理员邮箱")
    private String adminEmail;

    @Schema(description = "管理员手机")
    private String adminPhone;

    @Schema(description = "管理员状态")
    private Integer adminState;

    @Schema(description = "管理员最近登录时间")
    private Date adminLoginTime;

}
