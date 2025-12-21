package com.zyh.easyapplyresume.model.vo.ad_monitor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "监测端广告分页")
public class AdmonitorAdvertisementPageVO {
    @Schema(description = "广告id（主键自增）")
    private Integer advertisementId;

    @Schema(description = "广告名称")
    private String advertisementName;

    @Schema(description = "广告URL（存储广告内容/图片等地址）")
    private String advertisementUrl;

    @Schema(description = "广告图标超链接（跳转地址）")
    private String advertisementLink;

    @Schema(description = "广告开始时间")
    private Date advertisementStartedTime;

    @Schema(description = "广告结束时间")
    private Date advertisementEndTime;
}
