package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "招聘岗位分页信息")
public class RecruitPositionPageVO {
    @Schema(description = "招聘岗位id")
    private Integer recruitPositionId;

    @Schema(description = "招聘岗位名称")
    private String recruitPositionName;

    @Schema(description = "招聘岗位创建时间")
    private Date createdTime;

    @Schema(description = "招聘岗位更新时间")
    private Date updatedTime;

    @Schema(description = "招聘岗位所属行业代码")
    private Integer recruitPositionIndustryCode;

    @Schema(description = "招聘岗位所属行业名称")
    private Integer recruitPositionIndustryName;

    @Schema(description = "招聘岗位最低月薪(单位元)")
    private BigDecimal minMonthSalary;

    @Schema(description = "招聘岗位最高月薪(单位元)")
    private BigDecimal maxMonthSalary;

    @Schema(description = "招聘岗位工作周工作日数")
    private Integer weekWorkDayNum;

    @Schema(description = "招聘岗位福利待遇")
    private String goodWelfare;
}
