package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 简历评分模型版本分页查询条件
 *
 * @author shiningCloud2025
 */
@Data
@Schema(description = "简历评分模型版本分页查询条件")
public class AdminScoreModelVersionQuery {

    /**
     * 模型名称
     */
    @Schema(description = "模型名称")
    private String scoreModelVersionModelName;

    /**
     * 模型类型
     */
    @Schema(description = "模型类型")
    private String scoreModelVersionModelType;
}
