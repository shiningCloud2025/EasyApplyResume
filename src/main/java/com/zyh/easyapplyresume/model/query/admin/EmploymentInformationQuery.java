package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "招聘信息查询表单")
public class EmploymentInformationQuery {
    @Schema(description = "公司名称")
    private String employmentInformationCompanyName;

    @Schema(description = "行业大类")
    private Integer employmentInformationIndustryCategories;

    @Schema(description = "企业性质")
    private Integer employmentInformationCompanyType;

    @Schema(description = "招聘批次")
    private Integer employmentInformationBatch;

    @Schema(description = "招聘岗位")
    private Integer employmentInformationRecruitPosition;

    @Schema(description = "招聘对象")
    private Integer employmentInformationRecruitObject;

    @Schema(description = "招聘地址(省级)")
    private Integer employmentInformationRecruitLocationFirst;

    @Schema(description = "招聘地址(市级)")
    private Integer employmentInformationRecruitLocationSecond;

    @Schema(description = "详细招聘地址")
    private String employmentInformationRecruitLocationDetail;

    @Schema(description = "截止时间")
    private Date employmentInformationStopTime;

    @Schema(description = "网申状态")
    private String employmentInformationOnlineApplicationStatus;

    @Schema(description = "官方公告")
    private String employmentInformationOfficialAnnouncement;

    @Schema(description = "投递方式")
    private String employmentInformationSubmissionWay;

    @Schema(description = "内推码")
    private String employmentInformationEmployeeReferralCode;
}
