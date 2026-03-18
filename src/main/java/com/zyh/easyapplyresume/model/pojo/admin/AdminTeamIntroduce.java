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
 * 团队介绍实体类
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_teamIntroduce")
public class AdminTeamIntroduce {
    /**
     * 团队介绍id（主键，自增）
     */
    @TableId(value = "teamIntroduce_id", type = IdType.AUTO)
    private Integer teamIntroduceId;
    /**
     * 团队介绍标题
     */
    @TableField("teamIntroduce_title")
    private String teamIntroduceTitle;
    /**
     * 团队介绍内容
     */
    @TableField("teamIntroduce_content")
    private String teamIntroduceContent;
    /**
     * 修改时间
     */
    @TableField("teamIntroduce_updatedTime")
    private LocalDateTime teamIntroduceUpdatedTime;
}
