package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.admin.AdminScoreTrainingDataForm;
import com.zyh.easyapplyresume.model.query.admin.AdminScoreTrainingDataQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminScoreTrainingDataInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminScoreTrainingDataPageVO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 简历评分训练数据服务
 * @author shiningCloud2025
 */
public interface AdminScoreTrainingDataService {

    /**
     * 新增简历评分训练数据
     */
    Integer addScoreTrainingData(AdminScoreTrainingDataForm form);

    /**
     * 删除简历评分训练数据
     */
    Integer deleteScoreTrainingData(Integer scoreTrainingDataId);

    /**
     * 查询简历评分训练数据详情
     */
    AdminScoreTrainingDataInfoVO findScoreTrainingDataById(Integer scoreTrainingDataId);

    /**
     * 分页查询简历评分训练数据
     */
    Page<AdminScoreTrainingDataPageVO> findScoreTrainingDataByPage(Integer pageNum, Integer pageSize, AdminScoreTrainingDataQuery query);

    /**
     * 查询所有简历评分训练数据
     */
    List<AdminScoreTrainingDataPageVO> findAllScoreTrainingData();

    /**
     * 导出简历评分训练数据
     */
    void exportScoreTrainingData(AdminScoreTrainingDataQuery query, HttpServletResponse response);
}
