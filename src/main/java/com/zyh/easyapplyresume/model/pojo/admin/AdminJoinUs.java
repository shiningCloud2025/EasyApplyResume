package com.zyh.easyapplyresume.model.pojo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 加入我们实体类
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_joinUs")
public class AdminJoinUs {
    /**
     * 加入我们id（主键，自增）
     */
    @TableId(value = "joinUs_id", type = IdType.AUTO)
    private Integer joinUsId;
    /**
     * 加入我们标题
     */
    @TableField("joinUs_title")
    private String joinUsTitle;
    /**
     * 加入我们内容
     */
    @TableField("joinUs_content")
    private String joinUsContent;
    /**
     * 修改时间
     */
    @TableField("joinUs_updatedTime")
    private Date joinUsUpdatedTime;
}
