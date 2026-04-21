package com.zyh.easyapplyresume.model.pojo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 简历评分模型版本表
 *
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admin_scoreModelVersion")
public class AdminScoreModelVersion {

    /**
     * 模型版本id
     */
    @TableId(value = "scoreModelVersion_id", type = IdType.AUTO)
    private Integer scoreModelVersionId;

    /**
     * 模型名称
     */
    @TableField("scoreModelVersion_modelName")
    private String scoreModelVersionModelName;

    /**
     * 模型版本号
     */
    @TableField("scoreModelVersion_version")
    private String scoreModelVersionVersion;

    /**
     * 模型类型，如xgboost
     */
    @TableField("scoreModelVersion_modelType")
    private String scoreModelVersionModelType;

    /**
     * 模型访问地址
     */
    @TableField("scoreModelVersion_modelUrl")
    private String scoreModelVersionModelUrl;

    /**
     * 使用的embedding模型，如bge-m3
     */
    @TableField("scoreModelVersion_embeddingModel")
    private String scoreModelVersionEmbeddingModel;

    /**
     * 训练样本数量
     */
    @TableField("scoreModelVersion_sampleCount")
    private Integer scoreModelVersionSampleCount;

    /**
     * 训练耗时毫秒
     */
    @TableField("scoreModelVersion_trainCostMs")
    private Long scoreModelVersionTrainCostMs;

    /**
     * 评估指标JSON
     */
    @TableField("scoreModelVersion_metricJson")
    private String scoreModelVersionMetricJson;

    /**
     * 是否当前启用版本
     */
    @TableField("scoreModelVersion_isActive")
    private Integer scoreModelVersionIsActive;

    /**
     * 创建时间
     */
    @TableField("scoreModelVersion_createTime")
    private LocalDateTime scoreModelVersionCreateTime;

    /**
     * 逻辑删除0存在，1删除
     */
    @TableField("deleted")
    private Integer deleted;
}
