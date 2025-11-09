package com.zyh.easyapplyresume.model.pojo.admin;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 行业Map-通用(无删除)
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_industryMap")
public class IndustryMap {
    /**
     * 行业代码,主键
     */
    @TableId(value = "industryMap_industryCode", type = IdType.AUTO)
    private Integer industryMapIndustryCode;

    /**
     * 行业名称
     */
    @TableField("industryMap_industryName")
    private String industryMapIndustryName;

    /**
     * 创建时间
     */
    @TableField("industryMap_createdTime")
    private DateTime createdTime;

    /**
     * 修改时间
     */
    @TableField("industryMap_updatedTime")
    private DateTime updatedTime;
}
