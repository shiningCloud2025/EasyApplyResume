package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.CityMapMapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.ProvinceMapMapper;
import com.zyh.easyapplyresume.model.pojo.admin.CityMap;
import com.zyh.easyapplyresume.model.pojo.admin.ProvinceMap;
import com.zyh.easyapplyresume.service.admin.ProvinceMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shiningCloud2025
 */
@Service
@Transactional
public class ProvinceMapServiceImpl implements ProvinceMapService {
    @Autowired
    private ProvinceMapMapper provinceMapMapper;
    @Autowired
    private CityMapMapper cityMapMapper;
    @Override
    public List<ProvinceMap> getAllProvince() {
        return provinceMapMapper.selectList(null);
    }

    @Override
    public List<CityMap> getCityByProvinceId(Integer provinceMapId) {
        LambdaQueryWrapper<CityMap> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CityMap::getCityMapPid, provinceMapId);
        return cityMapMapper.selectList(lambdaQueryWrapper);
    }
}
