package com.zyh.easyapplyresume.model.form.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "普通注册表单")
public class FormalRegisterForm {
    @NotNull(message = "用户账号不能为空")
    @Schema(description = "用户账号")
    private String userAccount;

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

    @Schema(description = "用户创建时间")
    private Date userCreateTime;

    @Schema(description = "用户最后登录时间")
    private Date userLoginTime;

    @Schema(description = "用户目标岗位")
    private Integer userDreamPosition;

    @Schema(description = "用户希望的最低月薪")
    private BigDecimal userDreamMinMonthSalary;

    @Schema(description = "用户希望的最高月薪")
    private BigDecimal userDreamMaxMonthSalary;

    @Schema(description = "用户希望的每周工作数")
    private Integer userDreamWeekWorkDayNum;

    @Schema(description = "用户希望的福利待遇")
    private String userDreamGoodWelfare;

    @Schema(description = "用户地址(省级)")
    private String userRecruitLocationFirst;

    @Schema(description = "用户地址(市级)")
    private String userRecruitLocationSecond;

    @Schema(description = "用户大学编码")
    private int userUniversityCode;

    @Schema(description = "手机验证码")
    private String phoneMessageCode;

    @Schema(description = "邮箱验证码")
    private String emailMessageCode;

}
