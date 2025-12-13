package com.zyh.easyapplyresume.model.form.admin;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "管理员公告表单")
public class AdminAnnouncementForm {
    @Schema(description = "公告id")
    private Integer announcementId;

    @Schema(description = "公告标题")
    private String announcementTitle;

    @Schema(description = "公告内容")
    private String announcementContent;
}
