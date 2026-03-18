package com.zyh.easyapplyresume.model.pojo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 使用指南实体类
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_userGuide")
public class AdminUserGuide {
    /**
     * 使用指南id（主键，自增）
     */
    @TableId(value = "userGuide_id", type = IdType.AUTO)
    private Integer userGuideId;
    /**
     * 使用指南标题
     */
    @TableField("userGuide_title")
    private String userGuideTitle;
    /**
     * 使用指南内容
     */
    @TableField("userGuide_content")
    private String userGuideContent;
    /**
     * 逻辑删除标识
     */
    @TableField("deleted")
    private Integer deleted;
    /**
     * 创建时间
     */
    @TableField("userGuide_createdTime")
    private LocalDateTime userGuideCreatedTime;
    /**
     * 修改时间
     */
    @TableField("userGuide_updatedTime")
    private LocalDateTime userGuideUpdatedTime;
}
