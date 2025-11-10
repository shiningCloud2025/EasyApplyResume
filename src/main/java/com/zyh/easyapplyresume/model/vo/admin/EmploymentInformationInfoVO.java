package com.zyh.easyapplyresume.model.vo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "招聘信息信息")
public class EmploymentInformationInfoVO {
    @Schema(description = "招聘信息ID")
    private Integer employmentInformationId;

    @TableField("employmentInformation_code")
    @Schema(description = "招聘信息编号")
    private Integer employmentInformationCode;

    @Schema(description = "公司名称")
    private String employmentInformationCompanyName;

    @Schema(description = "行业大类")
    private String employmentInformationIndustryCategoriesName;

    @Schema(description = "企业性质")
    private Integer employmentInformationCompanyType;

    @Schema(description = "招聘批次")
    private Integer employmentInformationBatch;

    @Schema(description = "招聘岗位")
    private Integer employmentInformationRecruitPosition;

    @Schema(description = "招聘对象")
    private Integer employmentInformationRecruitObject;

    @Schema(description = "招聘地址(省级)")
    private List<String> employmentInformationRecruitLocationFirstName;

    @Schema(description = "招聘地址(市级)")
    private List<String> employmentInformationRecruitLocationSecondName;

    @Schema(description = "详细招聘地址")
    private List<String> employmentInformationRecruitLocationDetail;

    @Schema(description = "创建时间")
    private Date employmentInformationStartTime;

    @Schema(description = "截止时间")
    private Date employmentInformationStopTime;

    @Schema(description = "更新时间")
    private Date employmentInformationUpdatedTime;

    @Schema(description = "网申状态")
    private String employmentInformationOnlineApplicationStatus;

    @Schema(description = "官方公告")
    private String employmentInformationOfficialAnnouncement;

    @Schema(description = "投递方式")
    private String employmentInformationSubmissionWay;

    @Schema(description = "内推码")
    private String employmentInformationEmployeeReferralCode;
}
