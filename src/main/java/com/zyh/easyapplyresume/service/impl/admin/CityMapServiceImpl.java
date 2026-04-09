package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.mapper.mysql.admin.AreaMapMapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.CityMapMapper;
import com.zyh.easyapplyresume.model.pojo.admin.AreaMap;
import com.zyh.easyapplyresume.model.pojo.admin.CityMap;
import com.zyh.easyapplyresume.model.query.admin.CityMapQuery;
import com.zyh.easyapplyresume.model.vo.admin.CityMapInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.CityMapPageVO;
import com.zyh.easyapplyresume.redis.constant.common.CityMapCacheKey;
import com.zyh.easyapplyresume.redis.util.RedisCacheUtil;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.CityMapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
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

    @Override
    public CityMapInfoVO findCityMapById(Integer cityMapId) {
        CityMap cityMap = cityMapMapper.selectById(cityMapId);
        if (cityMap == null) {
            throw new RuntimeException("城市Map信息不存在");
        }
        return BeanUtil.copyProperties(cityMap, CityMapInfoVO.class);
    }

    @Override
    public Page<CityMapPageVO> findCityMapByPage(Integer pageNum, Integer pageSize, CityMapQuery cityMapQuery) {
        LambdaQueryWrapper<CityMap> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (cityMapQuery != null) {
            if (cityMapQuery.getCityMapCname() != null
                    && !cityMapQuery.getCityMapCname().trim().isEmpty()) {
                lambdaQueryWrapper.like(
                        CityMap::getCityMapCname,
                        cityMapQuery.getCityMapCname().trim()
                );
            }
        }

        lambdaQueryWrapper.orderByAsc(CityMap::getCityMapCid);

        Page<CityMap> cityMapPage = cityMapMapper.selectPage(
                new Page<>(pageNum, pageSize),
                lambdaQueryWrapper
        );

        List<CityMapPageVO> voList = cityMapPage.getRecords().stream()
                .map(cityMap -> {
                    CityMapPageVO pageVO = new CityMapPageVO();
                    BeanUtils.copyProperties(cityMap, pageVO);
                    return pageVO;
                })
                .collect(Collectors.toList());

        Page<CityMapPageVO> cityMapVOPage = new Page<>();
        cityMapVOPage.setRecords(voList);
        cityMapVOPage.setCurrent(cityMapPage.getCurrent());
        cityMapVOPage.setSize(cityMapPage.getSize());
        cityMapVOPage.setTotal(cityMapPage.getTotal());
        cityMapVOPage.setPages(cityMapPage.getPages());

        return cityMapVOPage;
    }
}
