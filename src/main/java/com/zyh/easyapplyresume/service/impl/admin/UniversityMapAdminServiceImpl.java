package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.mapper.mysql.user.UniversityMapMapper;
import com.zyh.easyapplyresume.model.pojo.user.UniversityMap;
import com.zyh.easyapplyresume.model.query.admin.UniversityMapQuery;
import com.zyh.easyapplyresume.model.vo.admin.UniversityMapInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.UniversityMapPageVO;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.UniversityMapAdminService;
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
public class UniversityMapAdminServiceImpl implements UniversityMapAdminService {
    @Autowired
    private UniversityMapMapper universityMapMapper;

    @Override
    public UniversityMapInfoVO findUniversityMapById(Integer universityMapId) {
        UniversityMap universityMap = universityMapMapper.selectById(universityMapId);
        if (universityMap == null) {
            throw new RuntimeException("大学Map信息不存在");
        }
        return BeanUtil.copyProperties(universityMap, UniversityMapInfoVO.class);
    }

    @Override
    public Page<UniversityMapPageVO> findUniversityMapByPage(Integer pageNum, Integer pageSize, UniversityMapQuery universityMapQuery) {
        LambdaQueryWrapper<UniversityMap> lambdaQueryWrapper = lambdaQuery(UniversityMap.class);

        if (universityMapQuery != null) {

            if (universityMapQuery.getUniversityMapName() != null
                    && !universityMapQuery.getUniversityMapName().trim().isEmpty()) {
                lambdaQueryWrapper.like(
                        UniversityMap::getUniversityMapName,
                        universityMapQuery.getUniversityMapName().trim()
                );
            }
        }

        Page<UniversityMap> universityMapPage = universityMapMapper.selectPage(
                new Page<>(pageNum, pageSize),
                lambdaQueryWrapper
        );

        List<UniversityMapPageVO> voList = universityMapPage.getRecords().stream()
                .map(universityMap -> {
                    UniversityMapPageVO pageVO = new UniversityMapPageVO();
                    BeanUtils.copyProperties(universityMap, pageVO);
                    return pageVO;
                })
                .collect(Collectors.toList());

        Page<UniversityMapPageVO> universityMapVOPage = new Page<>();
        universityMapVOPage.setRecords(voList);
        universityMapVOPage.setCurrent(universityMapPage.getCurrent());
        universityMapVOPage.setSize(universityMapPage.getSize());
        universityMapVOPage.setTotal(universityMapPage.getTotal());
        universityMapVOPage.setPages(universityMapPage.getPages());

        return universityMapVOPage;
    }
}
