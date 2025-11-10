package com.zyh.easyapplyresume.bean.locationenum;

import lombok.Getter;

@Getter
public enum StreetEnum1 {

;



    /** 数据库对应ID */
    private final int id;
    /** 街道/乡镇名称 */
    private final String name;

    StreetEnum1(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * 根据ID获取枚举
     * @param id 数据库ID
     * @return 对应的枚举，无匹配返回null
     */
    public static StreetEnum1 getById(int id) {
        for (StreetEnum1 street : values()) {
            if (street.getId() == id) {
                return street;
            }
        }
        return null;
    }

    /**
     * 根据名称获取枚举
     * @param name 街道/乡镇名称
     * @return 对应的枚举，无匹配返回null
     */
    public static StreetEnum1 getByName(String name) {
        for (StreetEnum1 street : values()) {
            if (street.getName().equals(name)) {
                return street;
            }
        }
        return null;
    }
}
