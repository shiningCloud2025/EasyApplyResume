package com.zyh.easyapplyresume.model.pojo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 区县Map-通用(只有查询版)
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_areaMap")
public class AreaMap {
    /**
     * 区县id,主键
     */
    @TableId(value = "areaMap_id", type = IdType.AUTO)
    private Integer areaMapId;
    /**
     * 区县名称
     */
    @TableField("areaMap_aname")
    private String areaMapAname;
    /**
     * 所属城市id
     */
    @TableField("areaMap_cid")
    private Integer areaMapCid;
}
