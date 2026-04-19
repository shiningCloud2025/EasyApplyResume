package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.mapper.mysql.admin.StreetMapMapper;
import com.zyh.easyapplyresume.model.pojo.admin.StreetMap;
import com.zyh.easyapplyresume.model.query.admin.StreetMapQuery;
import com.zyh.easyapplyresume.model.vo.admin.StreetMapInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.StreetMapPageVO;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.StreetMapService;
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
public class StreetMapServiceImpl implements StreetMapService {
    @Autowired
    private StreetMapMapper streetMapMapper;
    @Override
    public List<StreetMap> getAllStreet() {
        return streetMapMapper.selectList(null);
    }

    @Override
    public StreetMapInfoVO findStreetMapById(Integer streetMapId) {
        StreetMap streetMap = streetMapMapper.selectById(streetMapId);
        if (streetMap == null) {
            throw new RuntimeException("街道Map信息不存在");
        }
        return BeanUtil.copyProperties(streetMap, StreetMapInfoVO.class);
    }

    @Override
    public Page<StreetMapPageVO> findStreetMapByPage(Integer pageNum, Integer pageSize, StreetMapQuery streetMapQuery) {
        LambdaQueryWrapper<StreetMap> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (streetMapQuery != null) {
            if (streetMapQuery.getStreetMapSname() != null
                    && !streetMapQuery.getStreetMapSname().trim().isEmpty()) {
                lambdaQueryWrapper.like(
                        StreetMap::getStreetMapSname,
                        streetMapQuery.getStreetMapSname().trim()
                );
            }
        }

        lambdaQueryWrapper.orderByAsc(StreetMap::getStreetMapSid);

        Page<StreetMap> streetMapPage = streetMapMapper.selectPage(
                new Page<>(pageNum, pageSize),
                lambdaQueryWrapper
        );

        List<StreetMapPageVO> voList = streetMapPage.getRecords().stream()
                .map(streetMap -> {
                    StreetMapPageVO pageVO = new StreetMapPageVO();
                    BeanUtils.copyProperties(streetMap, pageVO);
                    return pageVO;
                })
                .collect(Collectors.toList());

        Page<StreetMapPageVO> streetMapVOPage = new Page<>();
        streetMapVOPage.setRecords(voList);
        streetMapVOPage.setCurrent(streetMapPage.getCurrent());
        streetMapVOPage.setSize(streetMapPage.getSize());
        streetMapVOPage.setTotal(streetMapPage.getTotal());
        streetMapVOPage.setPages(streetMapPage.getPages());

        return streetMapVOPage;
    }
}
