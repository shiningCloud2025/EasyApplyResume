package com.zyh.easyapplyresume.model.pojo.admin;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 简历评分训练数据表
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admin_scoreTrainingData")
public class AdminScoreTrainingData {
    /**
     * 训练数据id
     */
    @TableId(value = "scoreTrainingData_id", type = IdType.AUTO)
    private Integer scoreTrainingDataId;
    /**
     * 简历名称
     */
    @TableField("scoreTrainingData_resumeName")
    private String scoreTrainingDataResumeName;
    /**
     * 行业名称
     */
    @TableField("scoreTrainingData_industryName")
    private String scoreTrainingDataIndustryName;
    /**
     * 简历内容（React代码/文本）
     */
    @TableField("scoreTrainingData_resumeContent")
    private String scoreTrainingDataResumeContent;
    /**
     * 训练标签分数
     */
    @TableField("scoreTrainingData_labelScore")
    private Double scoreTrainingDataLabelScore;
    /**
     * 数据来源：0人工，1使用模型
     */
    @TableField("scoreTrainingData_dataSource")
    private Integer scoreTrainingDataDataSource;
    /**
     * 创建时间
     */
    @TableField("scoreTrainingData_createTime")
    private LocalDateTime scoreTrainingDataCreateTime;
    /**
     * 逻辑删除0存在，1删除
     */
    @TableField("deleted")
    private Integer deleted;

}
