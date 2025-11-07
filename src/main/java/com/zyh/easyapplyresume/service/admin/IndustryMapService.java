package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.admin.IndustryMapForm;
import com.zyh.easyapplyresume.model.query.admin.IndustryMapQuery;
import com.zyh.easyapplyresume.model.vo.admin.IndustryMapInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.IndustryMapPageVO;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface IndustryMapService {
    /**
     * 新增行业Map
     */
    public void addIndustryMap(IndustryMapForm industryMapForm);
    /**
     * 修改行业Map
     */
    public void updateIndustryMap(IndustryMapForm industryMapForm);
    /**
     * 查询行业Map
     */
    public IndustryMapInfoVO findIndustryMapById(Integer industryMapId);
    /**
     * 分页查询
     */
    public Page<IndustryMapPageVO> findIndustryMapByPage(Integer pageNum, Integer pageSize, IndustryMapQuery industryMapQuery);
    /**
     * 查询所有行业Map
     */
    public List<IndustryMapInfoVO> findAllIndustryMap();

}
