package com.zyh.easyapplyresume.model.pojo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 招聘信息-通用
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_employmentInformation")
public class EmploymentInformation {
    /**
     * 招聘信息ID,主键
     */
    @TableId(value = "employmentInformation_id", type = IdType.AUTO)
    private Integer employmentInformationId;
    /**
     * 招聘信息编号
     */
    @TableField("employmentInformation_code")
    private Integer employmentInformationCode;
    /**
     * 公司名称
     */
    @TableField("employmentInformation_companyName")
    private String employmentInformationCompanyName;
    /**
     * 行业大类
     */
    @TableField("employmentInformation_industryCategories")
    private Integer employmentInformationIndustryCategories;
    /**
     * 企业性质
     */
    @TableField("employmentInformation_companyType")
    private Integer employmentInformationCompanyType;
    /**
     * 招聘批次
     */
    @TableField("employmentInformation_batch")
    private Integer employmentInformationBatch;
    /**
     * 招聘岗位
     */
    @TableField("employmentInformation_recruitPosition")
    private Integer employmentInformationRecruitPosition;
    /**
     * 招聘对象
     */
    @TableField("employmentInformation_recruitObject")
    private Integer employmentInformationRecruitObject;
    /**
     * 招聘地址(省级)
     */
    @TableField("employmentInformation_recruitLocationFirst")
    private Integer employmentInformationRecruitLocationFirst;
    /**
     * 招聘地址(市级)
     */
    @TableField("employmentInformation_recruitLocationSecond")
    private Integer employmentInformationRecruitLocationSecond;
    /**
     * 详细招聘地址
     */
    @TableField("employmentInformation_recruitLocationDetail")
    private String employmentInformationRecruitLocationDetail;
    /**
     * 创建时间
     */
    @TableField("employmentInformation_startTime")
    private Date employmentInformationStartTime;
    /**
     * 截止时间
     */
    @TableField("employmentInformation_stopTime")
    private Date employmentInformationStopTime;
    /**
     * 更新时间
     */
    @TableField("employmentInformation_updatedTime")
    private Date employmentInformationUpdatedTime;
    /**
     * 网申状态
     */
    @TableField("employmentInformation_onlineApplicationStatus")
    private String employmentInformationOnlineApplicationStatus;
    /**
     * 官方公告
     */
    @TableField("employmentInformation_officialAnnouncement")
    private String employmentInformationOfficialAnnouncement;
    /**
     * 投递方式
     */
    @TableField("employmentInformation_submissionWay")
    private String employmentInformationSubmissionWay;
    /**
     * 内推码
     */
    @TableField("employmentInformation_employeeReferralCode")
    private String employmentInformationEmployeeReferralCode;
    /**
     * 逻辑删除
     */
    @TableField("deleted")
    private Integer deleted;

}

