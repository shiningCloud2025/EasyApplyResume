package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "人工客服信息")
public class AdminCustomerServiceInfoVO {
    @Schema(description = "人工客服id")
    private Integer customerServiceId;

    @Schema(description = "人工客服标题")
    private String customerServiceTitle;

    @Schema(description = "人工客服内容")
    private String customerServiceContent;

    @Schema(description = "人工客服更新时间")
    private LocalDateTime customerServiceUpdatedTime;
}
