package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.admin.AdminScoreModelVersionForm;
import com.zyh.easyapplyresume.model.query.admin.AdminScoreModelVersionQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminScoreModelVersionInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminScoreModelVersionPageVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 简历评分模型版本服务
 *
 * @author shiningCloud2025
 */
public interface AdminScoreModelVersionService {

    /**
     * 新增模型版本
     */
    Integer addScoreModelVersion(AdminScoreModelVersionForm form, MultipartFile modelFile);

    /**
     * 修改模型版本
     */
    Integer updateScoreModelVersion(AdminScoreModelVersionForm form, MultipartFile modelFile);

    /**
     * 删除模型版本
     */
    Integer deleteScoreModelVersion(Integer scoreModelVersionId);

    /**
     * 查询模型版本详情
     */
    AdminScoreModelVersionInfoVO findScoreModelVersionById(Integer scoreModelVersionId);

    /**
     * 分页查询模型版本
     */
    Page<AdminScoreModelVersionPageVO> findScoreModelVersionByPage(Integer pageNum, Integer pageSize, AdminScoreModelVersionQuery query);

    /**
     * 查询所有模型版本
     */
    List<AdminScoreModelVersionPageVO> findAllScoreModelVersion();
}
