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
 * 用户端广告实体类-监测与广告端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admonitor_userAdvertisement")
public class AdmonitorUserAdvertisement {
    /**
     * 广告id（主键自增）
     */
    @TableId(value = "userAdvertisement_id", type = IdType.AUTO)
    private Integer advertisementId;

    /**
     * 广告名称
     */
    @TableField("userAdvertisement_name")
    private String advertisementName;

    /**
     * 广告URL（存储广告内容/图片等地址）
     */
    @TableField("userAdvertisement_url")
    private String advertisementUrl;

    /**
     * 广告图标超链接（跳转地址）
     */
    @TableField("userAdvertisement_link")
    private String advertisementLink;

    /**
     * 广告开始时间
     */
    @TableField("userAdvertisement_startedTime")
    private Date advertisementStartedTime;

    /**
     * 广告结束时间
     */
    @TableField("userAdvertisement_endTime")
    private Date advertisementEndTime;

    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    @TableField("deleted")
    private Integer deleted;
}
