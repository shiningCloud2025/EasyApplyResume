package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "常见问题查询")
public class AdminFaqQuery {
    @Schema(description = "常见问题标题")
    private String faqTitle;
}
