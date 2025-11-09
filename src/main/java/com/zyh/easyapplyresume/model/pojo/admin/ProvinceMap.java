package com.zyh.easyapplyresume.model.pojo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 省份Map-通用(只有查询版)
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("general_provinceMap")
public class ProvinceMap {
    /**
     * 省份ID,主键
     */
    @TableId(value = "provinceMap_pid", type = IdType.AUTO)
    private Integer provinceMapPid;
    /**
     * 省份名称
     */
    @TableField("provinceMap_pname")
    private String provinceMapPname;
}
