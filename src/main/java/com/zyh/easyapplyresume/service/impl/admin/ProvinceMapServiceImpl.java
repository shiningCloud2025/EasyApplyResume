package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.CityMapMapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.ProvinceMapMapper;
import com.zyh.easyapplyresume.model.pojo.admin.CityMap;
import com.zyh.easyapplyresume.model.pojo.admin.ProvinceMap;
import com.zyh.easyapplyresume.redis.constant.common.ProvinceMapCacheKey;
import com.zyh.easyapplyresume.redis.util.RedisCacheUtil;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.ProvinceMapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class ProvinceMapServiceImpl implements ProvinceMapService {
    @Autowired
    private ProvinceMapMapper provinceMapMapper;
    @Autowired
    private CityMapMapper cityMapMapper;
    @Autowired
    private RedisCacheUtil redisCacheUtil;
    @Override
    public List<ProvinceMap> getAllProvince() {
        Object cached = redisCacheUtil.get(ProvinceMapCacheKey.LIST);
        if (cached != null) {
            return (List<ProvinceMap>) cached;
        }
        
        List<ProvinceMap> result = provinceMapMapper.selectList(null);
        
        redisCacheUtil.set(ProvinceMapCacheKey.LIST, result, ProvinceMapCacheKey.LIST_TTL, TimeUnit.MINUTES);
        
        return result;
    }

    @Override
    public List<CityMap> getCityByProvinceId(Integer provinceMapId) {
        LambdaQueryWrapper<CityMap> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CityMap::getCityMapPid, provinceMapId);
        return cityMapMapper.selectList(lambdaQueryWrapper);
    }
}
