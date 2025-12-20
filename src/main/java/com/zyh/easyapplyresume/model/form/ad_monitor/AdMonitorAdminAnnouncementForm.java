package com.zyh.easyapplyresume.model.form.ad_monitor;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "管理员公告表单")
public class AdMonitorAdminAnnouncementForm {
    @Schema(description = "公告id")
    private Integer announcementId;

    @Schema(description = "公告标题")
    private String announcementTitle;

    @Schema(description = "公告内容")
    private String announcementContent;
}
