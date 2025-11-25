package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.admin.ResumeTemplateForm;
import com.zyh.easyapplyresume.model.pojo.admin.ResumeTemplate;
import com.zyh.easyapplyresume.model.query.admin.ResumeTemplateQuery;
import com.zyh.easyapplyresume.model.vo.admin.ResumeTemplateInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.ResumeTemplatePageVO;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface ResumeTemplateService {
    /**
     * 新增简历模版
     */
    public Integer addResumeTemplate(ResumeTemplateForm resumeTemplateForm);
    /**
     * 修改简历模版
     */
    public Integer updateResumeTemplate(ResumeTemplateForm resumeTemplateForm);
    /**
     * 删除简历模版
     */
    public Integer deleteResumeTemplate(Integer resumeTemplateId);
    /**
     * 根据id查询简历模版
     */
    public ResumeTemplateInfoVO findResumeTemplateById(Integer resumeTemplateId);
    /**
     * 分页查询简历模版
     */
    public Page<ResumeTemplatePageVO> findResumeTemplateByPage(Integer pageNum, Integer pageSize, ResumeTemplateQuery resumeTemplateQuery);
    /**
     * 查询所有简历模版
     */
    public List<ResumeTemplatePageVO> findAllResumeTemplate();

}
