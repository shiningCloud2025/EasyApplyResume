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
 * 题库小类
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_questionSecondCategory")
public class AdminQuestionSecondCategory {
    /**
     * 题库小类id,主键
     */
    @TableId(value = "questionSecondCategory_id", type = IdType.AUTO)
    private Integer questionSecondCategoryId;

    /**
     * 所属大类id
     */
    @TableField("questionFirstCategory_id")
    private Integer questionFirstCategoryId;

    /**
     * 小类名称
     */
    @TableField("questionSecondCategory_name")
    private String questionSecondCategoryName;

    /**
     * 小类介绍
     */
    @TableField("questionSecondCategory_introduce")
    private String questionSecondCategoryIntroduce;

    /**
     * 小类创建时间
     */
    @TableField("questionSecondCategory_createTime")
    private LocalDateTime questionSecondCategoryCreateTime;

    /**
     * 逻辑删除
     */
    @TableField("deleted")
    private Integer deleted;
}
