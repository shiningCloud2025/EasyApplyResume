package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.query.admin.UniversityMapQuery;
import com.zyh.easyapplyresume.model.vo.admin.UniversityMapInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.UniversityMapPageVO;

/**
 * @author shiningCloud2025
 */
public interface UniversityMapAdminService {
    /**
     * 查询大学Map详情
     */
    UniversityMapInfoVO findUniversityMapById(Integer universityMapId);

    /**
     * 分页查询大学Map
     */
    Page<UniversityMapPageVO> findUniversityMapByPage(Integer pageNum, Integer pageSize, UniversityMapQuery universityMapQuery);
}
