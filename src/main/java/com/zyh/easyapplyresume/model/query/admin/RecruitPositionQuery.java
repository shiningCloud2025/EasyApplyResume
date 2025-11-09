package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "招聘岗位查询表单")
public class RecruitPositionQuery {

    @Schema(description = "招聘岗位名称")
    private String recruitPositionName;

    @Schema(description = "招聘岗位所属行业代码")
    private Integer recruitPositionIndustryCode;

    @Schema(description = "招聘岗位最低月薪(单位元)")
    private BigDecimal minMonthSalary;

    @Schema(description = "招聘岗位最高月薪(单位元)")
    private BigDecimal maxMonthSalary;

    @Schema(description = "招聘岗位工作周工作日数")
    private Integer weekWorkDayNum;

}
