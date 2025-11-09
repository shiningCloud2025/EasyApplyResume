package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyh.easyapplyresume.mapper.admin.AreaMapMapper;
import com.zyh.easyapplyresume.mapper.admin.StreetMapMapper;
import com.zyh.easyapplyresume.model.pojo.admin.AreaMap;
import com.zyh.easyapplyresume.model.pojo.admin.StreetMap;
import com.zyh.easyapplyresume.service.admin.AreaMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shiningCloud2025
 */
@Service
@Transactional
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
}
