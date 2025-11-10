package com.zyh.easyapplyresume.bean.locationenum;

import lombok.Getter;

/**
 * 城市/地区枚举（对应省份下的市级行政单位）
 * @author shiningCloud2025
 */
@Getter
public enum AreaEnum {

    ;


    /** 数据库对应ID */
    private final int id;
    /** 城市/地区名称 */
    private final String name;

    AreaEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * 根据ID获取枚举
     * @param id 数据库ID
     * @return
     */
    public static AreaEnum getById(int id) {
        for (AreaEnum area : values()) {
            if (area.getId() == id) {
                return area;
            }
        }
        return null;
    }

    /**
     * 根据名称获取枚举
     * @param name 城市/地区名称（完全匹配）
     * @return
     */
    public static AreaEnum getByName(String name) {
        for (AreaEnum area : values()) {
            if (area.getName().equals(name)) {
                return area;
            }
        }
        return null;
    }
}