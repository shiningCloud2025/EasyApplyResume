package com.zyh.easyapplyresume.service.admin;

import com.zyh.easyapplyresume.model.pojo.admin.CityMap;
import com.zyh.easyapplyresume.model.pojo.admin.ProvinceMap;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface ProvinceMapService {
    /**
     * 获取所有省份信息
     * @return
     */
    public List<ProvinceMap> getAllProvince();
    /**
     * 根据省份id获取所有市
     * @param provinceMapId
     * @return
     */
    public List<CityMap> getCityByProvinceId(Integer provinceMapId);
}
