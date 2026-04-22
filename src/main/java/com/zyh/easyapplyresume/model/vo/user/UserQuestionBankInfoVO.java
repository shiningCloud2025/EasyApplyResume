package com.zyh.easyapplyresume.model.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户端题库题目详情信息
 * @author shiningCloud2025
 */
@Data
@Schema(description = "用户端题库题目详情信息")
public class UserQuestionBankInfoVO {

    @Schema(description = "题目id")
    private Integer questionBankId;

    @Schema(description = "题目描述")
    private String questionBankDescription;

    @Schema(description = "题目选项A")
    private String questionBankOptionA;

    @Schema(description = "题目选项B")
    private String questionBankOptionB;

    @Schema(description = "题目选项C")
    private String questionBankOptionC;

    @Schema(description = "题目选项D")
    private String questionBankOptionD;

    @Schema(description = "题目图片")
    private String questionBankImage;

    @Schema(description = "题目代码段")
    private String questionBankCode;

    @Schema(description = "题目类型")
    private Integer questionBankType;

    @Schema(description = "题目大类名称")
    private String questionFirstCategoryName;

    @Schema(description = "题目小类名称")
    private String questionSecondCategoryName;

    @Schema(description = "题目难度")
    private Integer questionBankDifficulty;
}
