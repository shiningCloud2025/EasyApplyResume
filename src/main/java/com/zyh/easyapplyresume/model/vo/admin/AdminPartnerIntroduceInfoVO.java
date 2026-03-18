package com.zyh.easyapplyresume.model.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "合作伙伴信息")
public class AdminPartnerIntroduceInfoVO {
    @Schema(description = "合作伙伴id")
    private Integer partnerIntroduceId;

    @Schema(description = "合作伙伴标题")
    private String partnerIntroduceTitle;

    @Schema(description = "合作伙伴内容")
    private String partnerIntroduceContent;

    @Schema(description = "合作伙伴更新时间")
    private LocalDateTime partnerIntroduceUpdatedTime;
}
