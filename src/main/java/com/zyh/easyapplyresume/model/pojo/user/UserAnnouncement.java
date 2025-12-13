package com.zyh.easyapplyresume.model.pojo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户公告实体类-用户端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_announcement")
public class UserAnnouncement {
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
