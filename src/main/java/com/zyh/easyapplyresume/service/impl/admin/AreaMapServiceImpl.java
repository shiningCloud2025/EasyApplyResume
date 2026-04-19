package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.mapper.mysql.admin.AreaMapMapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.StreetMapMapper;
import com.zyh.easyapplyresume.model.pojo.admin.AreaMap;
import com.zyh.easyapplyresume.model.pojo.admin.StreetMap;
import com.zyh.easyapplyresume.model.query.admin.AreaMapQuery;
import com.zyh.easyapplyresume.model.vo.admin.AreaMapInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AreaMapPageVO;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.AreaMapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AreaMapServiceImpl implements AreaMapService {
    @Autowired
    private AreaMapMapper areaMapMapper;
    @Autowired
    private StreetMapMapper streetMapMapper;
    @Override
    public List<AreaMap> getAllArea() {
        return areaMapMapper.selectList(null);
    }

    @Override
    public List<StreetMap> getStreetByAreaId(Integer areaMapId) {
        LambdaQueryWrapper<StreetMap> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StreetMap::getStreetMapAid, areaMapId);
        return streetMapMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public AreaMapInfoVO findAreaMapById(Integer areaMapId) {
        AreaMap areaMap = areaMapMapper.selectById(areaMapId);
        if (areaMap == null) {
            throw new RuntimeException("区县Map信息不存在");
        }
        return BeanUtil.copyProperties(areaMap, AreaMapInfoVO.class);
    }

    @Override
    public Page<AreaMapPageVO> findAreaMapByPage(Integer pageNum, Integer pageSize, AreaMapQuery areaMapQuery) {
        LambdaQueryWrapper<AreaMap> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (areaMapQuery != null) {
            if (areaMapQuery.getAreaMapAname() != null
                    && !areaMapQuery.getAreaMapAname().trim().isEmpty()) {
                lambdaQueryWrapper.like(
                        AreaMap::getAreaMapAname,
                        areaMapQuery.getAreaMapAname().trim()
                );
            }
        }

        lambdaQueryWrapper.orderByAsc(AreaMap::getAreaMapId);

        Page<AreaMap> areaMapPage = areaMapMapper.selectPage(
                new Page<>(pageNum, pageSize),
                lambdaQueryWrapper
        );

        List<AreaMapPageVO> voList = areaMapPage.getRecords().stream()
                .map(areaMap -> {
                    AreaMapPageVO pageVO = new AreaMapPageVO();
                    BeanUtils.copyProperties(areaMap, pageVO);
                    return pageVO;
                })
                .collect(Collectors.toList());

        Page<AreaMapPageVO> areaMapVOPage = new Page<>();
        areaMapVOPage.setRecords(voList);
        areaMapVOPage.setCurrent(areaMapPage.getCurrent());
        areaMapVOPage.setSize(areaMapPage.getSize());
        areaMapVOPage.setTotal(areaMapPage.getTotal());
        areaMapVOPage.setPages(areaMapPage.getPages());

        return areaMapVOPage;
    }
}
