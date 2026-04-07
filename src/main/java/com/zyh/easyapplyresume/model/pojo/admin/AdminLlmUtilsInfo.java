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
 * LLM工具类调用日志实体类
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_llmUtilsInfo")
public class AdminLlmUtilsInfo {

    /**
     * LLM工具信息ID，主键自增
     */
    @TableId(value = "llmUtilsInfo_id", type = IdType.AUTO)
    private Long llmUtilsInfoId;

    /**
     * 工具类名
     */
    @TableField("llmUtilsInfo_toolClass")
    private String llmUtilsInfoToolClass;

    /**
     * 工具描述/功能分类
     */
    @TableField("llmUtilsInfo_toolDescription")
    private String llmUtilsInfoToolDescription;

    /**
     * 模型厂商
     */
    @TableField("llmUtilsInfo_modelProvider")
    private String llmUtilsInfoModelProvider;

    /**
     * 模型名称
     */
    @TableField("llmUtilsInfo_modelName")
    private String llmUtilsInfoModelName;

    /**
     * 输入内容
     */
    @TableField("llmUtilsInfo_inputContent")
    private String llmUtilsInfoInputContent;

    /**
     * 输出结果（JSON）
     */
    @TableField("llmUtilsInfo_outputResult")
    private String llmUtilsInfoOutputResult;

    /**
     * 响应延迟（毫秒）
     */
    @TableField("llmUtilsInfo_latencyMs")
    private Integer llmUtilsInfoLatencyMs;

    /**
     * 调用状态（SUCCESS/FAILED）
     */
    @TableField("llmUtilsInfo_status")
    private String llmUtilsInfoStatus;

    /**
     * 错误信息
     */
    @TableField("llmUtilsInfo_errorMessage")
    private String llmUtilsInfoErrorMessage;

    /**
     * 调用时间
     */
    @TableField("llmUtilsInfo_createdTime")
    private LocalDateTime llmUtilsInfoCreatedTime;
}
