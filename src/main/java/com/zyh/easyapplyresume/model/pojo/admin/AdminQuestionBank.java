package com.zyh.easyapplyresume.model.pojo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 题库题目
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_questionBank")
public class AdminQuestionBank {
    /**
     * 题目id,主键
     */
    @TableId(value = "questionBank_id", type = IdType.AUTO)
    private Integer questionBankId;
    /**
     * 题目描述
     */
    @TableField("questionBank_description")
    private String questionBankDescription;
    /**
     * 题目选项A
     */
    @TableField("questionBank_optionA")
    private String questionBankOptionA;
    /**
     * 题目选项B
     */
    @TableField("questionBank_optionB")
    private String questionBankOptionB;
    /**
     * 题目选项C
     */
    @TableField("questionBank_optionC")
    private String questionBankOptionC;
    /**
     * 题目选项D
     */
    @TableField("questionBank_optionD")
    private String questionBankOptionD;
    /**
     * 题目正确答案
     */
    @TableField("questionBank_correctAnswer")
    private String questionBankCorrectAnswer;
    /**
     * 题目图片
     */
    @TableField("questionBank_image")
    private String questionBankImage;
    /**
     * 题目代码段
     */
    @TableField("questionBank_code")
    private String questionBankCode;
    /**
     * 题目类型(1单选 2多选 3判断 4填空 5简答 6编程)
     */
    @TableField("questionBank_type")
    private Integer questionBankType;
    /**
     * 题目大类id（逻辑外键）
     */
    @TableField("questionFirstCategory_id")
    private Integer questionFirstCategoryId;
    /**
     * 题目大类名称（冗余）
     */
    @TableField("questionFirstCategory_name")
    private String questionFirstCategoryName;
    /**
     * 题目小类id（逻辑外键）
     */
    @TableField("questionSecondCategory_id")
    private Integer questionSecondCategoryId;
    /**
     * 题目小类名称（冗余）
     */
    @TableField("questionSecondCategory_name")
    private String questionSecondCategoryName;
    /**
     * 题目参考答案
     */
    @TableField("questionBank_referenceAnswer")
    private String questionBankReferenceAnswer;
    /**
     * 题目解析
     */
    @TableField("questionBank_analysis")
    private String questionBankAnalysis;
    /**
     * 题目难度(0默认 1简单 2中等 3困难)
     */
    @TableField("questionBank_difficulty")
    private Integer questionBankDifficulty;
    /**
     * 题目状态(0禁用 1启用)
     */
    @TableField("questionBank_state")
    private Integer questionBankState;
    /**
     * 题目创建时间
     */
    @TableField("questionBank_createTime")
    private LocalDateTime questionBankCreateTime;
    /**
     * 题目更新时间
     */
    @TableField("questionBank_updateTime")
    private LocalDateTime questionBankUpdateTime;
    /**
     * 逻辑删除0存在，1删除
     */
    @TableField("deleted")
    private Integer deleted;
}
