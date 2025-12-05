package com.zyh.easyapplyresume.model.vo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户删除简历信息")
public class UserDeleteResumeInfoVO {

    @Schema(description = "删除简历名称")
    private String userDeleteResumeResumeName;

    @Schema(description = "删除简历行业")
    private String userDeleteResumeIndustryName;

    @Schema(description = "删除简历React组件代码")
    private String userDeleteResumeResumeReactCode;

    @Schema(description = "删除简历创建时间")
    private Date userDeleteResumeCreatedTime;

    @Schema(description = "删除简历最后更新时间")
    private Date userDeleteResumeUpdatedTime;

    @Schema(description = "删除简历排序序号")
    private Integer userDeleteResumeSortedNum;

    @Schema(description = "简历所属用户")
    private Integer userDeleteResumeUserId;

    @Schema(description = "简历删除时间")
    private Date userDeleteResumeDeleteTime;
}
