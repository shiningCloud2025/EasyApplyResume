package com.zyh.easyapplyresume.model.form.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户表单")
public class UserForm {
    @Schema(description = "用户id")
    private Integer userId;

    @NotNull(message = "用户名称不能为空")
    @Schema(description = "用户名称")
    private String userUsername;

    @NotNull(message = "用户邮箱不能为空")
    @Schema(description = "用户邮箱")
    private String userEmail;

    @NotNull(message = "用户手机不能为空")
    @Schema(description = "用户手机")
    private String userPhone;

    @NotNull(message = "用户密码不能为空")
    @Schema(description = "用户密码")
    private String userPassword;

    @Schema(description = "用户头像")
    private String userImage;

    @Schema(description = "用户介绍")
    private String userIntroduce;

    @Schema(description = "用户目标岗位")
    private int userDreamPosition;

    @Schema(description = "用户希望的最低月薪")
    private BigDecimal userDreamMinMonthSalary;

    @Schema(description = "用户希望的最高月薪")
    private BigDecimal userDreamMaxMonthSalary;

    @Schema(description = "用户希望的每周工作数")
    private int userDreamWeekWorkDayNum;

    @Schema(description = "用户希望的福利待遇")
    private String userDreamGoodWelfare;


}
