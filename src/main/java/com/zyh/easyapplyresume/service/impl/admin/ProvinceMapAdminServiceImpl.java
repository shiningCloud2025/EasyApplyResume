package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.mapper.mysql.admin.ProvinceMapMapper;
import com.zyh.easyapplyresume.model.pojo.admin.ProvinceMap;
import com.zyh.easyapplyresume.model.query.admin.ProvinceMapQuery;
import com.zyh.easyapplyresume.model.vo.admin.ProvinceMapInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.ProvinceMapPageVO;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.ProvinceMapAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.lambdaQuery;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class ProvinceMapAdminServiceImpl implements ProvinceMapAdminService {
    @Autowired
    private ProvinceMapMapper provinceMapMapper;

    @Override
    public ProvinceMapInfoVO findProvinceMapById(Integer provinceMapId) {
        ProvinceMap provinceMap = provinceMapMapper.selectById(provinceMapId);
        if (provinceMap == null) {
            throw new RuntimeException("省份Map信息不存在");
        }
        return BeanUtil.copyProperties(provinceMap, ProvinceMapInfoVO.class);
    }

    @Override
    public Page<ProvinceMapPageVO> findProvinceMapByPage(Integer pageNum, Integer pageSize, ProvinceMapQuery provinceMapQuery) {
        LambdaQueryWrapper<ProvinceMap> lambdaQueryWrapper = lambdaQuery(ProvinceMap.class);

        if (provinceMapQuery != null) {
            if (provinceMapQuery.getProvinceMapPname() != null
                    && !provinceMapQuery.getProvinceMapPname().trim().isEmpty()) {
                lambdaQueryWrapper.like(
                        ProvinceMap::getProvinceMapPname,
                        provinceMapQuery.getProvinceMapPname().trim()
                );
            }
        }

        lambdaQueryWrapper.orderByAsc(ProvinceMap::getProvinceMapPid);

        Page<ProvinceMap> provinceMapPage = provinceMapMapper.selectPage(
                new Page<>(pageNum, pageSize),
                lambdaQueryWrapper
        );

        List<ProvinceMapPageVO> voList = provinceMapPage.getRecords().stream()
                .map(provinceMap -> {
                    ProvinceMapPageVO pageVO = new ProvinceMapPageVO();
                    BeanUtils.copyProperties(provinceMap, pageVO);
                    return pageVO;
                })
                .collect(Collectors.toList());

        Page<ProvinceMapPageVO> provinceMapVOPage = new Page<>();
        provinceMapVOPage.setRecords(voList);
        provinceMapVOPage.setCurrent(provinceMapPage.getCurrent());
        provinceMapVOPage.setSize(provinceMapPage.getSize());
        provinceMapVOPage.setTotal(provinceMapPage.getTotal());
        provinceMapVOPage.setPages(provinceMapPage.getPages());

        return provinceMapVOPage;
    }
}
