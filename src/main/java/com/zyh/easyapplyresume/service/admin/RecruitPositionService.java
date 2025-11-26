package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.admin.RecruitPositionForm;
import com.zyh.easyapplyresume.model.query.admin.RecruitPositionQuery;
import com.zyh.easyapplyresume.model.vo.admin.RecruitPositionInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.RecruitPositionPageVO;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface RecruitPositionService {
    /**
     * 新增招聘岗位
     */
    public void addRecruitPosition(RecruitPositionForm recruitPositionForm);
    /**
     * 修改招聘岗位
     */
    public void updateRecruitPosition(RecruitPositionForm recruitPositionForm);
    /**
     * 删除招聘岗位
     */
    public void deleteRecruitPosition(Integer recruitPositionId);
    /**
     * 查询招聘岗位
     */
    public RecruitPositionInfoVO queryRecruitPosition(Integer recruitPositionId);
    /**
     * 获取招聘岗位分页信息
     */
    public Page<RecruitPositionPageVO> queryRecruitPositionPage(Integer pageNum, Integer pageSize, RecruitPositionQuery recruitPositionQuery);
    /**
     * 查询所有岗位
     */
    public List<RecruitPositionInfoVO> queryAllRecruitPositionPage();
}
