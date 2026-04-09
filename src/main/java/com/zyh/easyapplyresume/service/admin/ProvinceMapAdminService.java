package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.query.admin.ProvinceMapQuery;
import com.zyh.easyapplyresume.model.vo.admin.ProvinceMapInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.ProvinceMapPageVO;

/**
 * @author shiningCloud2025
 */
public interface ProvinceMapAdminService {
    /**
     * 查询省份Map详情
     */
    ProvinceMapInfoVO findProvinceMapById(Integer provinceMapId);

    /**
     * 分页查询省份Map
     */
    Page<ProvinceMapPageVO> findProvinceMapByPage(Integer pageNum, Integer pageSize, ProvinceMapQuery provinceMapQuery);
}
