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
 * 发展历程实体类
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_developHistory")
public class AdminDevelopHistory {
    /**
     * 发展历程id（主键，自增）
     */
    @TableId(value = "developHistory_id", type = IdType.AUTO)
    private Integer developHistoryId;
    /**
     * 发展历程标题
     */
    @TableField("developHistory_title")
    private String developHistoryTitle;
    /**
     * 发展历程内容
     */
    @TableField("developHistory_content")
    private String developHistoryContent;
    /**
     * 修改时间
     */
    @TableField("developHistory_updatedTime")
    private Date developHistoryUpdatedTime;
}
