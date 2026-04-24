package com.zyh.easyapplyresume.mlmodel.scoremodel;

import com.zyh.easyapplyresume.model.pojo.admin.AdminScoreModelVersion;
import lombok.extern.slf4j.Slf4j;
import ml.dmlc.xgboost4j.java.Booster;
import ml.dmlc.xgboost4j.java.DMatrix;
import ml.dmlc.xgboost4j.java.XGBoost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * 模型评分预测器
 * @author shiningCloud2025
 */
@Slf4j
@Service
public class ResumeScorePredictor {
    @Value("${score-model.base-dir:./upload}")
    private String scoreModelBaseDir;

    @Autowired
    private DashScopeEmbeddingService dashScopeEmbeddingService;

    public Integer predict(AdminScoreModelVersion adminScoreModelVersion,
                           String resumeName,
                           String industryName,
                           String resumeContent) {
        try {
            if (adminScoreModelVersion == null) {
                throw new RuntimeException("模型版本不能为空");
            }
            String modelRelativePath = adminScoreModelVersion.getScoreModelVersionModelUrl();
            if (modelRelativePath == null || modelRelativePath.trim().isEmpty()) {
                throw new RuntimeException("模型路径不能为空");
            }

            Path baseDirPath = Paths.get(scoreModelBaseDir).toAbsolutePath().normalize();
            Path modelPath = baseDirPath.resolve(modelRelativePath).normalize();

            if (!modelPath.startsWith(baseDirPath)) {
                throw new RuntimeException("模型路径非法");
            }
            if (!Files.exists(modelPath)) {
                throw new RuntimeException("模型文件不存在");
            }

            String sampleText = buildSampleText(resumeName, industryName, resumeContent);
            float[] embeddingVector = dashScopeEmbeddingService.embed(
                    sampleText,
                    adminScoreModelVersion.getScoreModelVersionEmbeddingModel()
            );

            DMatrix dMatrix = new DMatrix(embeddingVector, 1, embeddingVector.length, Float.NaN);
            Booster booster = XGBoost.loadModel(modelPath.toString());
            float[][] predictResult = booster.predict(dMatrix);

            if (predictResult == null || predictResult.length == 0 || predictResult[0].length == 0) {
                throw new RuntimeException("模型预测结果为空");
            }

            return Math.round(predictResult[0][0]);
        } catch (Exception e) {
            log.error("模型预测失败", e);
            throw new RuntimeException("模型预测失败");
        }
    }

    private String buildSampleText(String resumeName, String industryName, String resumeContent) {
        return "[简历名称] " + safeText(resumeName) + "\n"
                + "[行业名称] " + safeText(industryName) + "\n"
                + "[简历内容] " + safeText(resumeContent);
    }

    private String safeText(String text) {
        return text == null ? "" : text.trim();
    }
}
