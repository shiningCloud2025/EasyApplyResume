package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "常见问题分页")
public class AdminFaqPageVO {
    @Schema(description = "常见问题id")
    private Integer faqId;

    @Schema(description = "常见问题标题")
    private String faqTitle;

    @Schema(description = "常见问题内容")
    private String faqContent;

    @Schema(description = "常见问题创建时间")
    private Date faqCreatedTime;

    @Schema(description = "常见问题更新时间")
    private Date faqUpdatedTime;
}
