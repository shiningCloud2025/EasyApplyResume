package com.zyh.easyapplyresume.service.admin;

import com.zyh.easyapplyresume.model.pojo.admin.StreetMap;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface StreetMapService {
    /**
     * 获取所有街道信息
     * @return
     */
    public List<StreetMap> getAllStreet();
}
