package com.zyh.easyapplyresume.model.form.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 用户端题库题目作答表单
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户端题库题目作答表单")
public class UserQuestionBankAnswerForm {

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "题目id")
    private Integer questionBankId;

    @Schema(description = "用户答案集合")
    private List<String> userAnswers;
}
