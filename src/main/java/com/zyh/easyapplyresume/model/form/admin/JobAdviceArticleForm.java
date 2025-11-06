package com.zyh.easyapplyresume.model.form.admin;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "求职攻略文章表单")
public class JobAdviceArticleForm {
    @Schema(description = "求职攻略文章id")
    private Integer jobAdviceArticleId;

    @Schema(description = "求职攻略文章标题")
    @NotNull(message = "求职攻略文章标题不能为空")
    private String jobAdviceArticleTitle;

    @Schema(description = "求职攻略文章正文")
    private String jobAdviceArticleContent;

    @Schema(description = "求职攻略文章分类")
    private String jobAdviceArticleCategory;

    @Schema(description = "求职攻略文章标签")
    private String jobAdviceArticleTags;

    @Schema(description = "求职攻略文章作者名称")
    private String jobAdviceArticleAuthorName;

    @Schema(description = "求职攻略文章状态")
    private Integer jobAdviceArticlePublishedStatus;
}
