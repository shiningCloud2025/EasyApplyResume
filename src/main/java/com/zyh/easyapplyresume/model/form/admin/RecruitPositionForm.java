package com.zyh.easyapplyresume.model.form.admin;

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
@Schema(description = "招聘岗位表单")
public class RecruitPositionForm {
    @Schema(description = "招聘岗位主键ID")
    private Integer recruitPositionId;

    @Schema(description = "招聘岗位名称")
    @NotNull(message = "招聘岗位名称不能为空")
    private String recruitPositionName;

    @Schema(description = "招聘岗位所属行业代码")
    @NotNull(message = "招聘岗位所属行业代码不能为空")
    private Integer recruitPositionIndustryCode;

    @Schema(description = "招聘岗位最低月薪(单位元)")
    @NotNull(message = "招聘岗位最低月薪(单位元)不能为空")
    private BigDecimal minMonthSalary;

    @Schema(description = "招聘岗位最高月薪(单位元)")
    @NotNull(message = "招聘岗位最高月薪(单位元)不能为空")
    private BigDecimal maxMonthSalary;

    @Schema(description = "招聘岗位工作周工作日数")
    @NotNull(message = "招聘岗位工作周工作日数不能为空")
    private Integer weekWorkDayNum;

    @Schema(description = "招聘岗位福利待遇")
    private String goodWelfare;
}
