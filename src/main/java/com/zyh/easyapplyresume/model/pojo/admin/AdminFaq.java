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
 * 常见问题实体类
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_faq")
public class AdminFaq {
    /**
     * 常见问题id（主键，自增）
     */
    @TableId(value = "faq_id", type = IdType.AUTO)
    private Integer faqId;
    /**
     * 常见问题标题
     */
    @TableField("faq_title")
    private String faqTitle;
    /**
     * 常见问题内容
     */
    @TableField("faq_content")
    private String faqContent;
    /**
     * 逻辑删除标识
     */
    @TableField("deleted")
    private Integer deleted;
    /**
     * 创建时间
     */
    @TableField("faq_createdTime")
    private LocalDateTime faqCreatedTime;
    /**
     * 修改时间
     */
    @TableField("faq_updatedTime")
    private LocalDateTime faqUpdatedTime;
}
