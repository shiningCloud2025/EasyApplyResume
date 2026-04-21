package com.zyh.easyapplyresume.model.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户端题库题目分页信息
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户端题库题目分页信息")
public class UserQuestionBankPageVO {

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

    @Schema(description = "答题状态(0未开始 1进行中 2已完成)")
    private Integer questionBankAnswerStatus;

    @Schema(description = "题目创建时间")
    private LocalDateTime questionBankCreateTime;

    @Schema(description = "题目更新时间")
    private LocalDateTime questionBankUpdateTime;
}
