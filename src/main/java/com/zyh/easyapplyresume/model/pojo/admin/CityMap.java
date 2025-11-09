package com.zyh.easyapplyresume.model.pojo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 城市Map-通用(只有查询版)
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_cityMap")
public class CityMap {
    /**
     * 城市id,自增主键
     */
    @TableId(value = "cityMap_cid", type = IdType.AUTO)
    private Integer cityMapCid;
    /**
     * 城市名称
     */
    @TableField("cityMap_cname")
    private String cityMapCname;
    /**
     * 城市对应的省份id
     */
    @TableField("cityMap_pid")
    private Integer cityMapPid;

}
