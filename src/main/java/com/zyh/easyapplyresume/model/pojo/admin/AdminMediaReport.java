package com.zyh.easyapplyresume.model.pojo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 媒体报道实体类
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_mediaReport")
public class AdminMediaReport {
    /**
     * 媒体报道id（主键，自增）
     */
    @TableId(value = "mediaReport_id", type = IdType.AUTO)
    private Integer mediaReportId;
    /**
     * 媒体报道标题
     */
    @TableField("mediaReport_title")
    private String mediaReportTitle;
    /**
     * 媒体报道内容
     */
    @TableField("mediaReport_content")
    private String mediaReportContent;
    /**
     * 修改时间
     */
    @TableField("mediaReport_updatedTime")
    private Date mediaReportUpdatedTime;
}
