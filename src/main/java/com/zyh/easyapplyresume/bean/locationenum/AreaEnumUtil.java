package com.zyh.easyapplyresume.bean.locationenum;

import com.zyh.easyapplyresume.bean.locationenum.AreaEnum;
import com.zyh.easyapplyresume.bean.locationenum.AreaEnum1;
/**
 * 区县枚举通用类(因为做了数据拆分)
 * @author shiningCloud2025
 */
public class AreaEnumUtil {

    // 根据ID查询：先查AreaEnum，再查AreaEnum1
    public static Object getById(int id) {
        // 先查第一个枚举
        AreaEnum area = AreaEnum.getById(id);
        if (area != null) {
            return area;
        }
        // 第一个没查到，查第二个
        return AreaEnum1.getById(id);
    }

    // 根据名称查询：先查AreaEnum，再查AreaEnum1
    public static Object getByName(String name) {
        // 先查第一个枚举
        AreaEnum area = AreaEnum.getByName(name);
        if (area != null) {
            return area;
        }
        // 第一个没查到，查第二个
        return AreaEnum1.getByName(name);
    }

    // 简化：直接根据ID拿名称（常用）
    public static String getNameById(int id) {
        Object area = getById(id);
        if (area instanceof AreaEnum) {
            return ((AreaEnum) area).getName();
        } else if (area instanceof AreaEnum1) {
            return ((AreaEnum1) area).getName();
        }
        return null;
    }

    // 简化：直接根据名称拿ID
    public static Integer getIdByName(String name) {
        Object area = getByName(name);
        if (area instanceof AreaEnum) {
            return ((AreaEnum) area).getId();
        } else if (area instanceof AreaEnum1) {
            return ((AreaEnum1) area).getId();
        }
        return null;
    }
}