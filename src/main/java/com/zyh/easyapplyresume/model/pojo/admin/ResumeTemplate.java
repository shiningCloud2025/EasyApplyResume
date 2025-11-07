package com.zyh.easyapplyresume.model.pojo.admin;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 简历模板实体类-通用
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_resumeTemplate")
public class ResumeTemplate {
    /**
     * 简历模版id
     */
    @TableId(value = "resumeTemplate_id", type = IdType.AUTO)
    private Integer resumeTemplateId;
    /**
     * 简历模版名称
     */
    @TableField("resumeTemplate_name")
    private String resumeTemplateName;
    /**
     * 简历模版代码
     */
    @TableField("resumeTemplate_reactCode")
    private String resumeTemplateReactCode;
    /**
     * 简历行业
     */
    @TableField("resumeTemplate_industry")
    private Integer resumeTemplateIndustry;
    /**
     * 是否启用
     */
    @TableField("resumeTemplate_isActive")
    private Integer resumeTemplateIsActive;
    /**
     * 简历创建时间
     */
    @TableField("resumeTemplate_createdTime")
    private DateTime resumeTemplateCreatedTime;
    /**
     * 简历修改时间
     */
    @TableField("resumeTemplate_updatedTime")
    private DateTime resumeTemplateUpdatedTime;
    /**
     * 逻辑删除标识
     */
    @TableField("deleted")
    private Integer deleted;
}
