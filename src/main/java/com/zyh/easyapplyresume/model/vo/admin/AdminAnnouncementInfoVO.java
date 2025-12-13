package com.zyh.easyapplyresume.model.vo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "管理员公告信息")
public class AdminAnnouncementInfoVO {
    /**
     * 公告id（主键，自增）
     */
    @TableId(value = "announcement_id", type = IdType.AUTO)
    private Integer announcementId;
    /**
     * 公告标题
     */
    @TableField("announcement_title")
    private String announcementTitle;
    /**
     * 公告内容
     */
    @TableField("announcement_content")
    private String announcementContent;
    /**
     * 修改时间
     */
    @TableField("announcement_updatedTime")
    private Date announcementUpdatedTime;
}
