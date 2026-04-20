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
 * 简历评分模型训练代码表
 *
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admin_scoreModelTrainCode")
public class AdminScoreModelTrainCode {

    /**
     * 训练代码id
     */
    @TableId(value = "scoreModelTrainCode_id", type = IdType.AUTO)
    private Integer scoreModelTrainCodeId;

    /**
     * 训练代码名称
     */
    @TableField("scoreModelTrainCode_name")
    private String scoreModelTrainCodeName;

    /**
     * 训练代码版本号
     */
    @TableField("scoreModelTrainCode_version")
    private String scoreModelTrainCodeVersion;

    /**
     * 训练代码语言，如java、python
     */
    @TableField("scoreModelTrainCode_language")
    private String scoreModelTrainCodeLanguage;

    /**
     * 训练代码内容
     */
    @TableField("scoreModelTrainCode_content")
    private String scoreModelTrainCodeContent;

    /**
     * 训练代码描述
     */
    @TableField("scoreModelTrainCode_desc")
    private String scoreModelTrainCodeDesc;

    /**
     * 创建时间
     */
    @TableField("scoreModelTrainCode_createTime")
    private LocalDateTime scoreModelTrainCodeCreateTime;

    /**
     * 逻辑删除0存在，1删除
     */
    @TableField("deleted")
    private Integer deleted;
}
