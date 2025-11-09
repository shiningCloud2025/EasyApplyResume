package com.zyh.easyapplyresume.model.form.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "招聘信息表单")
public class EmploymentInformationForm {
    @Schema(description = "招聘信息ID")
    private Integer employmentInformationId;

    @Schema(description = "招聘信息编号")
    private Integer employmentInformationCode;

    @NotNull(message = "公司名称不能为空")
    @Schema(description = "公司名称")
    private String employmentInformationCompanyName;

    @NotNull(message = "行业大类不能为空")
    @Schema(description = "行业大类")
    private Integer employmentInformationIndustryCategories;

    @NotNull(message = "企业性质不能为空")
    @Schema(description = "企业性质")
    private Integer employmentInformationCompanyType;

    @NotNull(message = "招聘批次不能为空")
    @Schema(description = "招聘批次")
    private Integer employmentInformationBatch;

    @NotNull(message = "招聘岗位不能为空")
    @Schema(description = "招聘岗位")
    private Integer employmentInformationRecruitPosition;

    @NotNull(message = "招聘对象不能为空")
    @Schema(description = "招聘对象")
    private Integer employmentInformationRecruitObject;

    @NotNull(message = "招聘地址(省级)不能为空")
    @Schema(description = "招聘地址(省级)")
    private List<Integer> employmentInformationRecruitLocationFirst;

    @NotNull(message = "招聘地址(市级)不能为空")
    @Schema(description = "招聘地址(市级)")
    private List<Integer> employmentInformationRecruitLocationSecond;

    @Schema(description = "详细招聘地址")
    private String employmentInformationRecruitLocationDetail;


    @NotNull(message = "截止时间不能为空")
    @Schema(description = "截止时间")
    private Date employmentInformationStopTime;

    @NotNull(message = "网申状态不能为空")
    @Schema(description = "网申状态")
    private String employmentInformationOnlineApplicationStatus;

    @Schema(description = "官方公告")
    private String employmentInformationOfficialAnnouncement;

    @NotNull(message = "投递方式不能为空")
    @Schema(description = "投递方式")
    private String employmentInformationSubmissionWay;

    @Schema(description = "内推码")
    private String employmentInformationEmployeeReferralCode;

}
