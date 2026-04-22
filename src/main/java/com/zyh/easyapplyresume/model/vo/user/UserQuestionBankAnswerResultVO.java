package com.zyh.easyapplyresume.model.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户端题库题目作答结果
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户端题库题目作答结果")
public class UserQuestionBankAnswerResultVO {

    @Schema(description = "题目id")
    private Integer questionBankId;

    @Schema(description = "是否答对(true答对 false答错)")
    private Boolean correct;

    @Schema(description = "正确答案")
    private String correctAnswer;
}
