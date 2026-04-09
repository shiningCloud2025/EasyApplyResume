package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.pojo.admin.AreaMap;
import com.zyh.easyapplyresume.model.pojo.admin.CityMap;
import com.zyh.easyapplyresume.model.query.admin.CityMapQuery;
import com.zyh.easyapplyresume.model.vo.admin.CityMapInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.CityMapPageVO;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface CityMapService {
    /**
     * 获取所有城市信息
     * @return
     */
    List<CityMap> getAllCity();

    /**
     * 根据城市id获取所有区县
     * @param cityId
     * @return
     */
    List<AreaMap> getAllAreaByCityId(Integer cityId);

    /**
     * 查询城市Map详情
     */
    CityMapInfoVO findCityMapById(Integer cityMapId);

    /**
     * 分页查询城市Map
     */
    Page<CityMapPageVO> findCityMapByPage(Integer pageNum, Integer pageSize, CityMapQuery cityMapQuery);
}
