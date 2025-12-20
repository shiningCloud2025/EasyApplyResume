package com.zyh.easyapplyresume.model.pojo.ad_monitor;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户端公告实体类-监测与广告端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admonitor_userAnnouncement")
public class  AdMonitorUserAnnouncement {
    /**
     * 公告id（主键，自增）
     */
    @TableId(value = "userAnnouncement_id", type = IdType.AUTO)
    private Integer announcementId;
    /**
     * 公告标题
     */
    @TableField("userAnnouncement_title")
    private String announcementTitle;
    /**
     * 公告内容
     */
    @TableField("userAnnouncement_content")
    private String announcementContent;
    /**
     * 修改时间
     */
    @TableField("userAnnouncement_updatedTime")
    private Date announcementUpdatedTime;
}
