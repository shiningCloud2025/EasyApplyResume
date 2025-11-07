package com.zyh.easyapplyresume.model.vo.admin;

import cn.hutool.core.date.DateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "行业Map信息")
public class IndustryMapInfoVO {
    @Schema(description = "行业代码")
    private Integer industryMapIndustryCode;

    @Schema(description = "行业名称")
    private String industryMapIndustryName;

    @Schema(description = "创建时间")
    private DateTime createdTime;

    @Schema(description = "修改时间")
    private DateTime updatedTime;
}
