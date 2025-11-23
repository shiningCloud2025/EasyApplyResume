package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.CodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.IndustryMapEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.IndustryMapMapper;
import com.zyh.easyapplyresume.model.form.admin.IndustryMapForm;
import com.zyh.easyapplyresume.model.pojo.admin.IndustryMap;
import com.zyh.easyapplyresume.model.query.admin.IndustryMapQuery;
import com.zyh.easyapplyresume.model.vo.admin.IndustryMapInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.IndustryMapPageVO;
import com.zyh.easyapplyresume.service.admin.IndustryMapService;
import com.zyh.easyapplyresume.utils.Validator.IndustryMapFormValidator;
import org.commonmark.node.Code;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
public class IndustryMapServiceImpl implements IndustryMapService {
    @Autowired
    private IndustryMapMapper industryMapMapper;
    @Override
    public Integer addIndustryMap(IndustryMapForm industryMapForm) {
        IndustryMapFormValidator.validateForAdd(industryMapForm);
        IndustryMap industryMapEntity = new IndustryMap();
        BeanUtils.copyProperties(industryMapForm, industryMapEntity);
        industryMapEntity.setCreatedTime(new DateTime());
        industryMapEntity.setUpdatedTime(new DateTime());
        try {
            return industryMapMapper.insert(industryMapEntity);
        }catch (DataAccessException e){
            throw resolveDbException(e);
        }
    }

    @Override
    public Integer updateIndustryMap(IndustryMapForm industryMapForm) {
        IndustryMapFormValidator.validateForUpdate(industryMapForm);
        IndustryMap industryMapEntity = new IndustryMap();
        BeanUtils.copyProperties(industryMapForm, industryMapEntity);
        industryMapEntity.setUpdatedTime(new DateTime());
        try {
           return industryMapMapper.updateById(industryMapEntity);
        }catch (DataAccessException e){
            throw resolveDbException(e);
        }
    }

    /**
     * 解析数据库异常
     * 将数据库底层异常转换为自定义的业务异常
     * @param e 原始异常
     * @return 自定义的业务异常
     */
    private BusException resolveDbException(Exception e) {
        String errorMsg = e.getMessage();
        // 1. 处理唯一约束冲突（DuplicateKeyException 或包含 "Duplicate entry" 的异常）
        if (errorMsg != null && errorMsg.contains("Duplicate entry") || e instanceof org.springframework.dao.DuplicateKeyException) {
            // 为了增加匹配的准确性，可以同时判断字段名和索引名
            if (errorMsg.contains("industryMap_industryName") || errorMsg.contains("idx_industry_name")) {
                return new BusException(IndustryMapEnum.INDUSTRY_NAME_DUPLICATE);
            }
        }

        // 如果以上都不匹配，则抛出一个“转换失败”的通用异常
        return new BusException(CodeEnum.DB_EXCEPTION_TRANSFORM_FAIL_EXCEPTION);
    }

    @Override
    public IndustryMapInfoVO findIndustryMapById(Integer industryMapId) {
        IndustryMap industryMapEntity = industryMapMapper.selectById(industryMapId);
        return BeanUtil.copyProperties(industryMapEntity, IndustryMapInfoVO.class);
    }

    @Override
    public Page<IndustryMapPageVO> findIndustryMapByPage(Integer pageNum, Integer pageSize, IndustryMapQuery industryMapQuery) {
        // 1. 构建 LambdaQueryWrapper（修正为实体类名：IndustryMap）
        LambdaQueryWrapper<IndustryMap> lambdaQueryWrapper = lambdaQuery(IndustryMap.class);

        // 2. 判空过滤：构建查询条件（行业代码精确查询，行业名称模糊查询）
        // 行业代码：非空则精确匹配（编码是唯一标识，适合精确查询）
        if (industryMapQuery.getIndustryMapIndustryCode() != null) {
            lambdaQueryWrapper.eq(IndustryMap::getIndustryMapIndustryCode, industryMapQuery.getIndustryMapIndustryCode());
        }

        // 行业名称：非空且非空字符串则模糊查询（like %关键词%），trim()避免纯空格查询
        if (industryMapQuery.getIndustryMapIndustryName() != null && !industryMapQuery.getIndustryMapIndustryName().trim().isEmpty()) {
            lambdaQueryWrapper.like(IndustryMap::getIndustryMapIndustryName, industryMapQuery.getIndustryMapIndustryName().trim());
        }

        // 3. 调用 Mapper 分页查询（修正为 IndustryMapMapper，继承 BaseMapper 即可）
        Page<IndustryMap> industryMapPage = industryMapMapper.selectPage(
                new Page<>(pageNum, pageSize),  // 分页参数：当前页、每页条数
                lambdaQueryWrapper              // 构建好的查询条件
        );

        // 4. 实体转换：IndustryMap（数据库实体）→ IndustryMapPageVO（返回给前端的 VO）
        List<IndustryMapPageVO> voList = industryMapPage.getRecords().stream()
                .map(industryMap -> {
                    IndustryMapPageVO pageVO = new IndustryMapPageVO();
                    // 复制同名字段（VO 和 IndustryMap 字段名+类型一致即可，如 industryMapIndustryCode/Name）
                    BeanUtils.copyProperties(industryMap, pageVO);

                    return pageVO;
                })
                .collect(Collectors.toList());

        // 5. 封装 VO 分页对象（复制原始分页参数，保证分页逻辑正确）
        Page<IndustryMapPageVO> industryMapVOPage = new Page<>();
        industryMapVOPage.setRecords(voList);         // 转换后的 VO 列表
        industryMapVOPage.setCurrent(industryMapPage.getCurrent()); // 当前页码
        industryMapVOPage.setSize(industryMapPage.getSize());       // 每页条数
        industryMapVOPage.setTotal(industryMapPage.getTotal());     // 总数据量
        industryMapVOPage.setPages(industryMapPage.getPages());     // 总页数

        return industryMapVOPage;
    }

    @Override
    public List<IndustryMapInfoVO> findAllIndustryMap() {
        List<IndustryMap> industryMaps = industryMapMapper.selectList(null);
        List<IndustryMapInfoVO> industryMapInfoVOs = BeanUtil.copyToList(industryMaps, IndustryMapInfoVO.class);
        return industryMapInfoVOs;
    }
}
