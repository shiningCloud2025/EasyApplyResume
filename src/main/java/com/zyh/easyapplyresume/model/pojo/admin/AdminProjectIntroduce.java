package com.zyh.easyapplyresume.model.pojo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 项目介绍实体类
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_projectIntroduce")
public class AdminProjectIntroduce {
    /**
     * 项目介绍id（主键，自增）
     */
    @TableId(value = "projectIntroduce_id", type = IdType.AUTO)
    private Integer projectIntroduceId;
    /**
     * 项目介绍标题
     */
    @TableField("projectIntroduce_title")
    private String projectIntroduceTitle;
    /**
     * 项目介绍内容
     */
    @TableField("projectIntroduce_content")
    private String projectIntroduceContent;
    /**
     * 修改时间
     */
    @TableField("projectIntroduce_updatedTime")
    private LocalDateTime projectIntroduceUpdatedTime;
}
