package com.zyh.easyapplyresume.model.vo.admin;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
/**
 * 题库题目分页信息
 * @author shiningCloud2025
 */
@Data
@Schema(description = "题库题目分页信息")
public class AdminQuestionBankPageVO {
    @Schema(description = "题目描述")
    private String questionBankDescription;

    @Schema(description = "题目类型")
    private Integer questionBankType;

    @Schema(description = "题目大类名称")
    private String questionFirstCategoryName;

    @Schema(description = "题目小类名称")
    private String questionSecondCategoryName;

    @Schema(description = "题目难度")
    private Integer questionBankDifficulty;

    @Schema(description = "题目状态")
    private Integer questionBankState;

    @Schema(description = "题目创建时间")
    private LocalDateTime questionBankCreateTime;

    @Schema(description = "题目更新时间")
    private LocalDateTime questionBankUpdateTime;
}
