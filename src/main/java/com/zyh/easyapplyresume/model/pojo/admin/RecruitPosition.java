package com.zyh.easyapplyresume.model.pojo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 招聘岗位实体类-通用(无删除)
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_recruitPosition")
public class RecruitPosition {
    /**
     * 招聘岗位主键ID
     */
    @TableId(value ="recruitPosition_id " ,type = IdType.AUTO)
    private Integer recruitPositionId;
    /**
     * 招聘岗位名称
     */
    @TableField("recruitPosition_name")
    private String recruitPositionName;
    /**
     * 招聘岗位创建时间
     */
    @TableField("recruitPosition_createdTime")
    private Date createdTime;
    /**
     * 招聘岗位更新时间
     */
    @TableField("recruitPosition_updatedTime")
    private Date updatedTime;
    /**
     * 招聘岗位所属行业代码
     */
    @TableField("recruitPosition_industryCode")
    private Integer recruitPositionIndustryCode;
    /**
     * 招聘岗位最低月薪(单位元)
     */
    @TableField("recruitPosition_minMonthSalary")
    private BigDecimal minMonthSalary;
    /**
     * 招聘岗位最高月薪(单位元)
     */
    @TableField("recruitPosition_maxMonthSalary")
    private BigDecimal maxMonthSalary;
    /**
     * 招聘岗位工作周工作日数
     */
    @TableField("recruitPosition_weekWorkDayNum")
    private Integer weekWorkDayNum;
    /**
     * 招聘岗位福利待遇
     */
    @TableField("recruitPosition_goodWelfare")
    private String goodWelfare;
}
