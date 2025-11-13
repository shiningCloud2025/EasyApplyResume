package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.AreaMapMapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.CityMapMapper;
import com.zyh.easyapplyresume.model.pojo.admin.AreaMap;
import com.zyh.easyapplyresume.model.pojo.admin.CityMap;
import com.zyh.easyapplyresume.service.admin.CityMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shiningCloud2025
 */
@Service
@Transactional
public class CityMapServiceImpl implements CityMapService {
    @Autowired
    private CityMapMapper cityMapMapper;
    @Autowired
    private AreaMapMapper areaMapMapper;
    @Override
    public List<CityMap> getAllCity() {
        return cityMapMapper.selectList(null);
    }

    @Override
    public List<AreaMap> getAllAreaByCityId(Integer cityId) {
        LambdaQueryWrapper<AreaMap> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AreaMap::getAreaMapCid, cityId);
        return areaMapMapper.selectList(lambdaQueryWrapper);
    }
}
