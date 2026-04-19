package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.pojo.admin.StreetMap;
import com.zyh.easyapplyresume.model.query.admin.StreetMapQuery;
import com.zyh.easyapplyresume.model.vo.admin.StreetMapInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.StreetMapPageVO;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface StreetMapService {
    /**
     * 获取所有街道信息
     * @return
     */
    public List<StreetMap> getAllStreet();

    /**
     * 查询街道Map详情
     * @param streetMapId
     * @return
     */
    public StreetMapInfoVO findStreetMapById(Integer streetMapId);

    /**
     * 分页查询街道Map
     * @param pageNum
     * @param pageSize
     * @param streetMapQuery
     * @return
     */
    public Page<StreetMapPageVO> findStreetMapByPage(Integer pageNum, Integer pageSize, StreetMapQuery streetMapQuery);
}
