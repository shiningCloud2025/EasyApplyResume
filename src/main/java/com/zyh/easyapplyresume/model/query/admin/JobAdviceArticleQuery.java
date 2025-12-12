package com.zyh.easyapplyresume.model.query.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
/**
 * @author shiningCloud2025
 */
@Data
@Schema(description = "求职攻略文章查询")
public class JobAdviceArticleQuery {
    @Schema(description = "求职攻略文章标题")
    private String jobAdviceArticleTitle;

    @Schema(description = "求职攻略文章分类")
    private String jobAdviceArticleCategory;

    @Schema(description = "求职攻略文章标签")
    private String jobAdviceArticleTags;

    @Schema(description = "求职攻略文章作者名称")
    private String jobAdviceArticleAuthorName;
}
