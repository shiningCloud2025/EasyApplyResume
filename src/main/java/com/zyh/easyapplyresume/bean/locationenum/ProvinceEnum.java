package com.zyh.easyapplyresume.bean.locationenum;

import lombok.Getter;

/**
 * 省份/地区枚举（含直辖市、自治区、港澳、台湾、全国）
 * 对应数据库 province 表数据
 */
@Getter
public enum ProvinceEnum {
    BEIJING(1, "北京"),
    SHANGHAI(2, "上海"),
    TIANJIN(3, "天津"),
    CHONGQING(4, "重庆"),
    HEBEI(5, "河北"),
    SHANXI(6, "山西"),
    HENAN(7, "河南"),
    LIAONING(8, "辽宁"),
    JILIN(9, "吉林"),
    HEILONGJIANG(10, "黑龙江"),
    INNER_MONGOLIA(11, "内蒙古"),
    JIANGSU(12, "江苏"),
    SHANDONG(13, "山东"),
    ANHUI(14, "安徽"),
    ZHEJIANG(15, "浙江"),
    FUJIAN(16, "福建"),
    HUBEI(17, "湖北"),
    HUNAN(18, "湖南"),
    GUANGDONG(19, "广东"),
    GUANGXI(20, "广西"),
    JIANGXI(21, "江西"),
    SICHUAN(22, "四川"),
    HAINAN(23, "海南"),
    GUIZHOU(24, "贵州"),
    YUNNAN(25, "云南"),
    TIBET(26, "西藏"),
    SHAANXI(27, "陕西"),
    GANSU(28, "甘肃"),
    QINGHAI(29, "青海"),
    NINGXIA(30, "宁夏"),
    XINJIANG(31, "新疆"),
    HONG_KONG_MACAO(52993, "港澳"),
    TAIWAN(32, "台湾"),
    NATIONWIDE(33, "全国");

    /** 数据库对应ID */
    private final int id;
    /** 省份/地区名称 */
    private final String name;

    ProvinceEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * 根据ID获取枚举
     * @param id 数据库ID
     * @return 对应的枚举，无匹配返回null
     */
    public static ProvinceEnum getById(int id) {
        for (ProvinceEnum province : values()) {
            if (province.getId() == id) {
                return province;
            }
        }
        return null;
    }

    /**
     * 根据名称获取枚举
     * @param name 省份/地区名称
     * @return 对应的枚举，无匹配返回null
     */
    public static ProvinceEnum getByName(String name) {
        for (ProvinceEnum province : values()) {
            if (province.getName().equals(name)) {
                return province;
            }
        }
        return null;
    }
}