package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.pojo.admin.AreaMap;
import com.zyh.easyapplyresume.model.pojo.admin.StreetMap;
import com.zyh.easyapplyresume.model.query.admin.AreaMapQuery;
import com.zyh.easyapplyresume.model.vo.admin.AreaMapInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AreaMapPageVO;

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

    /**
     * 查询区县Map详情
     * @param areaMapId
     * @return
     */
    public AreaMapInfoVO findAreaMapById(Integer areaMapId);

    /**
     * 分页查询区县Map
     * @param pageNum
     * @param pageSize
     * @param areaMapQuery
     * @return
     */
    public Page<AreaMapPageVO> findAreaMapByPage(Integer pageNum, Integer pageSize, AreaMapQuery areaMapQuery);
}
