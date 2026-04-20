package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.admin.AdminScoreModelTrainCodeForm;
import com.zyh.easyapplyresume.model.query.admin.AdminScoreModelTrainCodeQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminScoreModelTrainCodeInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminScoreModelTrainCodePageVO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 简历评分模型训练代码服务
 * @author shiningCloud2025
 */
public interface AdminScoreModelTrainCodeService {

    /**
     * 新增训练代码
     */
    Integer addScoreModelTrainCode(AdminScoreModelTrainCodeForm form);

    /**
     * 修改训练代码
     */
    Integer updateScoreModelTrainCode(AdminScoreModelTrainCodeForm form);

    /**
     * 删除训练代码
     */
    Integer deleteScoreModelTrainCode(Integer scoreModelTrainCodeId);

    /**
     * 查询训练代码详情
     */
    AdminScoreModelTrainCodeInfoVO findScoreModelTrainCodeById(Integer scoreModelTrainCodeId);

    /**
     * 分页查询训练代码
     */
    Page<AdminScoreModelTrainCodePageVO> findScoreModelTrainCodeByPage(Integer pageNum, Integer pageSize, AdminScoreModelTrainCodeQuery query);

    /**
     * 查询所有训练代码
     */
    List<AdminScoreModelTrainCodePageVO> findAllScoreModelTrainCode();

    /**
     * 下载训练代码压缩包
     */
    void downloadScoreModelTrainCode(Integer scoreModelTrainCodeId, HttpServletResponse response);
}
