package com.zyh.easyapplyresume.service.admin;

import com.zyh.easyapplyresume.model.pojo.admin.AreaMap;
import com.zyh.easyapplyresume.model.pojo.admin.StreetMap;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface AreaMapService {
    /**
     * 获取所有区县信息
     * @return
     */
    public List<AreaMap> getAllArea();
    /**
     * 根据区县id获取街道信息
     * @param areaMapId
     * @return
     */
    public List<StreetMap> getStreetByAreaId(Integer areaMapId);
}
