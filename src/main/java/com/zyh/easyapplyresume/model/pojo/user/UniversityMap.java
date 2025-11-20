package com.zyh.easyapplyresume.model.pojo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * universityMap实体类-通用端
 * @author shiningCloud2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_universityMap")
public class UniversityMap {
    /**
     * 大学id
     */
    @TableId(value = "universityMap_id", type = IdType.AUTO)
    private Integer universityMapId;
    /**
     * 大学名称
     */
    @TableField("universityMap_name")
    private String universityMapName;
    /**
     * 大学地址
     */
    @TableField("universityMap_address")
    private String universityMapAddress;
    /**
     * 大学经度
     */
    @TableField("universityMap_lat")
    private String universityMapLat;
    /**
     * 大学纬度
     */
    @TableField("universityMap_lng")
    private String universityMapLng;
    /**
     * 是否有效
     */
    @TableField("universityMap_status")
    private Integer universityMapStatus;
}
