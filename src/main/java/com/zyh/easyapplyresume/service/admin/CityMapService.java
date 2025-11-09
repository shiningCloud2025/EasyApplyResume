package com.zyh.easyapplyresume.service.admin;

import com.zyh.easyapplyresume.model.pojo.admin.AreaMap;
import com.zyh.easyapplyresume.model.pojo.admin.CityMap;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface CityMapService {
    /**
     * 获取所有城市信息
     * @return
     */
    public List<CityMap> getAllCity();
    /**
     * 根据城市id获取所有区县
     * @param cityId
     * @return
     */
    public List<AreaMap> getAllAreaByCityId(Integer cityId);
}
