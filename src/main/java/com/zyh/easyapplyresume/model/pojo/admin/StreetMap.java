package com.zyh.easyapplyresume.model.pojo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 街道Map-通用(只有查询版)
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_streetMap")
public class StreetMap {
    /**
     * 街道id,主键
     */
    @TableId(value = "streetMap_sid", type = IdType.AUTO)
    private Integer streetMapSid;
    /**
     * 街道名称
     */
    @TableField("streetMap_sname")
    private String streetMapSname;
    /**
     * 所属区县id
     */
    @TableField("streetMap_aid")
    private Integer streetMapAid;
}
