package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.AreaMapMapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.CityMapMapper;
import com.zyh.easyapplyresume.model.pojo.admin.AreaMap;
import com.zyh.easyapplyresume.model.pojo.admin.CityMap;
import com.zyh.easyapplyresume.redis.constant.common.CityMapCacheKey;
import com.zyh.easyapplyresume.redis.util.RedisCacheUtil;
import com.zyh.easyapplyresume.service.admin.CityMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private RedisCacheUtil redisCacheUtil;
    @Override
    public List<CityMap> getAllCity() {
        // 1. 先查缓存
        Object cached = redisCacheUtil.get(CityMapCacheKey.LIST);
        if (cached != null) {
            return (List<CityMap>) cached;
        }
        
        // 2. 缓存未命中，查数据库
        List<CityMap> result = cityMapMapper.selectList(null);
        
        // 3. 写入缓存
        redisCacheUtil.set(CityMapCacheKey.LIST, result, CityMapCacheKey.LIST_TTL, TimeUnit.MINUTES);
        
        return result;
    }

    @Override
    public List<AreaMap> getAllAreaByCityId(Integer cityId) {
        // 1. 生成缓存Key
        String cacheKey = CityMapCacheKey.GET_PREFIX + "_" + cityId;
        
        // 2. 先查缓存
        Object cached = redisCacheUtil.get(cacheKey);
        if (cached != null) {
            return (List<AreaMap>) cached;
        }
        
        // 3. 缓存未命中，查数据库
        LambdaQueryWrapper<AreaMap> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AreaMap::getAreaMapCid, cityId);
        List<AreaMap> result = areaMapMapper.selectList(lambdaQueryWrapper);
        
        // 4. 写入缓存
        redisCacheUtil.set(cacheKey, result, CityMapCacheKey.GET_TTL, TimeUnit.MINUTES);
        
        return result;
    }
}
