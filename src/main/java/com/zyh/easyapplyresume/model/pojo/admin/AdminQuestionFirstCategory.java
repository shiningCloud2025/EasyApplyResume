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
 * 题库大类
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_questionFirstCategory")
public class AdminQuestionFirstCategory {
    /**
     * 题库大类id,主键
     */
    @TableId(value = "questionFirstCategory_id", type = IdType.AUTO)
    private Integer questionFirstCategoryId;

    /**
     * 大类名称
     */
    @TableField("questionFirstCategory_name")
    private String questionFirstCategoryName;

    /**
     * 大类介绍
     */
    @TableField("questionFirstCategory_introduce")
    private String questionFirstCategoryIntroduce;

    /**
     * 大类创建时间
     */
    @TableField("questionFirstCategory_createTime")
    private LocalDateTime questionFirstCategoryCreateTime;

    /**
     * 逻辑删除
     */
    @TableField("deleted")
    private Integer deleted;
}
