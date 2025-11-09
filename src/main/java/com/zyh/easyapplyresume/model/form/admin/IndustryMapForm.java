package com.zyh.easyapplyresume.model.form.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "行业Map表单")
public class IndustryMapForm {
    @Schema(description = "行业代码,主键")
    private Integer industryMapIndustryCode;

    @NotNull(message = "行业名称不能为空")
    @Schema(description = "行业名称")
    private String industryMapIndustryName;
}
