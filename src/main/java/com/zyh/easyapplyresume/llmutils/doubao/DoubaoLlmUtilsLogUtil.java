package com.zyh.easyapplyresume.llmutils.doubao;

import com.zyh.easyapplyresume.model.form.admin.AdminLlmUtilsInfoForm;
import com.zyh.easyapplyresume.service.admin.AdminLlmUtilsInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 豆包大模型调用日志工具类
 * 用于统一记录doubao包下各类AI工具的调用日志
 * @author shiningCloud2025
 */
@Slf4j
@Component
public class DoubaoLlmUtilsLogUtil {

    @Autowired
    private AdminLlmUtilsInfoService adminLlmUtilsInfoService;


    /**
     * 记录成功日志
     * @param toolClass 工具类名
     * @param toolDescription 工具描述
     * @param inputContent 输入内容
     * @param outputResult 输出结果
     * @param startTime 开始时间戳
     */
    public void saveSuccessLog(String toolClass, String toolDescription, String inputContent, String outputResult, long startTime) {
        try {
            AdminLlmUtilsInfoForm form = new AdminLlmUtilsInfoForm();
            form.setLlmUtilsInfoToolClass(toolClass);
            form.setLlmUtilsInfoToolDescription(toolDescription);
            form.setLlmUtilsInfoModelProvider("火山方舟");
            form.setLlmUtilsInfoModelName("Doubao-1.5-pro-32k");
            form.setLlmUtilsInfoInputContent(defaultText(inputContent, "无输入内容"));
            form.setLlmUtilsInfoOutputResult(defaultText(outputResult, "无输出结果"));
            form.setLlmUtilsInfoLatencyMs(calculateLatencyMs(startTime));
            form.setLlmUtilsInfoStatus("SUCCESS");
            form.setLlmUtilsInfoErrorMessage("无");
            adminLlmUtilsInfoService.addAdminLlmUtilsInfo(form);
        } catch (Exception e) {
            log.error("记录豆包大模型成功调用日志失败", e);
        }
    }

    /**
     * 记录失败日志
     *
     * @param toolClass 工具类名
     * @param toolDescription 工具描述
     * @param inputContent 输入内容
     * @param outputResult 输出结果
     * @param startTime 开始时间戳
     * @param errorMessage 错误信息
     */
    public void saveFailLog(String toolClass, String toolDescription, String inputContent, String outputResult, long startTime, String errorMessage) {
        try {
            AdminLlmUtilsInfoForm form = new AdminLlmUtilsInfoForm();
            form.setLlmUtilsInfoToolClass(toolClass);
            form.setLlmUtilsInfoToolDescription(toolDescription);
            form.setLlmUtilsInfoModelProvider("火山方舟");
            form.setLlmUtilsInfoModelName("Doubao-1.5-pro-32k");
            form.setLlmUtilsInfoInputContent(defaultText(inputContent, "无输入内容"));
            form.setLlmUtilsInfoOutputResult(defaultText(outputResult, "无输出结果"));
            form.setLlmUtilsInfoLatencyMs(calculateLatencyMs(startTime));
            form.setLlmUtilsInfoStatus("FAILED");
            form.setLlmUtilsInfoErrorMessage(defaultText(errorMessage, "未知错误"));
            adminLlmUtilsInfoService.addAdminLlmUtilsInfo(form);
        } catch (Exception e) {
            log.error("记录豆包大模型失败调用日志失败", e);
        }
    }

    /**
     * 计算调用耗时（毫秒）
     *
     * @param startTime 开始时间戳
     * @return 调用耗时
     */
    private Integer calculateLatencyMs(long startTime) {
        long latency = System.currentTimeMillis() - startTime;
        if (latency < 0) {
            latency = 0;
        }
        if (latency > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) latency;
    }

    /**
     * 文本兜底
     *
     * @param text 原始文本
     * @param defaultValue 默认值
     * @return 兜底后的文本
     */
    private String defaultText(String text, String defaultValue) {
        if (text == null || text.trim().isEmpty()) {
            return defaultValue;
        }
        return text.trim();
    }


}
