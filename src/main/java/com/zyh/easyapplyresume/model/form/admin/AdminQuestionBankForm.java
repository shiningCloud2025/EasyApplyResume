package com.zyh.easyapplyresume.model.form.admin;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * 题库题目表单
 * @author shiningCloud2025
 */
@Data
@Schema(description = "题库题目表单参数")
public class AdminQuestionBankForm {
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

    @Schema(description = "题目正确答案")
    private String questionBankCorrectAnswer;

    @Schema(description = "题目图片")
    private String questionBankImage;

    @Schema(description = "题目代码段")
    private String questionBankCode;

    @Schema(description = "题目类型(1单选 2多选 3判断 4填空 5简答)")
    private Integer questionBankType;

    @Schema(description = "题目大类id")
    private Integer questionFirstCategoryId;

    @Schema(description = "题目大类名称")
    private String questionFirstCategoryName;

    @Schema(description = "题目小类id")
    private Integer questionSecondCategoryId;

    @Schema(description = "题目小类名称")
    private String questionSecondCategoryName;

    @Schema(description = "题目参考答案")
    private String questionBankReferenceAnswer;

    @Schema(description = "题目解析")
    private String questionBankAnalysis;

    @Schema(description = "题目难度(0默认 1简单 2中等 3困难)")
    private Integer questionBankDifficulty;

    @Schema(description = "题目状态(0禁用 1启用)")
    private Integer questionBankState;
}
