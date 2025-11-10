package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.mapper.admin.ResumeTemplateMapper;
import com.zyh.easyapplyresume.model.form.admin.ResumeTemplateForm;
import com.zyh.easyapplyresume.model.pojo.admin.ResumeTemplate;
import com.zyh.easyapplyresume.model.query.admin.ResumeTemplateQuery;
import com.zyh.easyapplyresume.model.vo.admin.ResumeTemplateInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.ResumeTemplatePageVO;
import com.zyh.easyapplyresume.service.admin.IndustryMapService;
import com.zyh.easyapplyresume.utils.Validator.ResumeTemplateFormValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zyh.easyapplyresume.service.admin.ResumeTemplateService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shiningCloud2025
 */
@Service
@Transactional
public class ResumeTemplateServiceImpl implements ResumeTemplateService {
    @Autowired
    private ResumeTemplateMapper resumeTemplateMapper;
    @Autowired
    private IndustryMapService industryMapService;

    @Override
    public void addResumeTemplate(ResumeTemplateForm resumeTemplateForm) {
        ResumeTemplateFormValidator.validateForAdd(resumeTemplateForm);
        ResumeTemplate resumeTemplate = new ResumeTemplate();
        BeanUtils.copyProperties(resumeTemplateForm, resumeTemplate);
        resumeTemplate.setResumeTemplateIsActive(1);
        resumeTemplate.setResumeTemplateCreatedTime(new DateTime());
        resumeTemplate.setResumeTemplateUpdatedTime(new DateTime());
        resumeTemplate.setDeleted(0);
        resumeTemplateMapper.insert(resumeTemplate);
    }

    @Override
    public void updateResumeTemplate(ResumeTemplateForm resumeTemplateForm) {
        ResumeTemplateFormValidator.validateForUpdate(resumeTemplateForm);
        ResumeTemplate resumeTemplate = new ResumeTemplate();
        BeanUtils.copyProperties(resumeTemplateForm, resumeTemplate);
        resumeTemplate.setResumeTemplateUpdatedTime(new DateTime());
        resumeTemplateMapper.updateById(resumeTemplate);
    }

    @Override
    public void deleteResumeTemplate(Integer resumeTemplateId) {
        ResumeTemplate resumeTemplate = new ResumeTemplate();
        resumeTemplate.setDeleted(1);
        resumeTemplateMapper.updateById(resumeTemplate);
    }

    @Override
    public ResumeTemplateInfoVO findResumeTemplateById(Integer resumeTemplateId) {
        LambdaQueryWrapper<ResumeTemplate> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ResumeTemplate::getResumeTemplateId, resumeTemplateId);
        lambdaQueryWrapper.eq(ResumeTemplate::getDeleted, 0);
        ResumeTemplate resumeTemplate = resumeTemplateMapper.selectOne(lambdaQueryWrapper);
        ResumeTemplateInfoVO resumeTemplateInfoVO = BeanUtil.copyProperties(resumeTemplate, ResumeTemplateInfoVO.class);
        resumeTemplateInfoVO.setIndustryMapIndustryName(industryMapService.findIndustryMapById(resumeTemplate.getResumeTemplateIndustry()).getIndustryMapIndustryName());
        return resumeTemplateInfoVO;
    }

    @Override
    public List<ResumeTemplateInfoVO> findResumeTemplateByPage(Integer pageNum, Integer pageSize, ResumeTemplateQuery resumeTemplateQuery) {
        // 1. 构建 LambdaQueryWrapper（指定 ResumeTemplate 实体类）
        LambdaQueryWrapper<ResumeTemplate> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ResumeTemplate::getDeleted, 0);

        // 2. 判空过滤：模版名称不为空则模糊查询（和原方法判空逻辑一致）
        if (resumeTemplateQuery != null && resumeTemplateQuery.getResumeTemplateName() != null && !resumeTemplateQuery.getResumeTemplateName().isEmpty()) {
            lambdaQueryWrapper.like(ResumeTemplate::getResumeTemplateName, resumeTemplateQuery.getResumeTemplateName());
        }
        if(resumeTemplateQuery!=null&&resumeTemplateQuery.getResumeTemplateIndustry()!=null){
            lambdaQueryWrapper.eq(ResumeTemplate::getResumeTemplateIndustry,resumeTemplateQuery.getResumeTemplateIndustry());
        }

        // 3. 调用 Mapper 分页查询（依赖 ResumeTemplateMapper 继承 BaseMapper/IService）
        Page<ResumeTemplate> resumeTemplatePage = resumeTemplateMapper.selectPage(
                new Page<>(pageNum, pageSize),  // 分页参数：当前页、每页条数
                lambdaQueryWrapper              // 模糊查询条件
        );

        // 4. 实体转换：ResumeTemplate（数据库实体）→ ResumeTemplateInfoVO（前端返回VO）
        List<ResumeTemplateInfoVO> voList = resumeTemplatePage.getRecords().stream()
                .map(resumeTemplate -> {
                    ResumeTemplateInfoVO infoVO = new ResumeTemplateInfoVO();
                    // 复制同名字段（要求字段名+数据类型一致，如 resumeTemplateId、resumeTemplateName 等）
                    BeanUtils.copyProperties(resumeTemplate, infoVO);
                    infoVO.setIndustryMapIndustryName(industryMapService.findIndustryMapById(resumeTemplate.getResumeTemplateIndustry()).getIndustryMapIndustryName());
                    // 若 VO 与实体字段名/格式不一致，需手动补充映射（示例如下，根据实际 VO 结构调整）
                    // 示例1：实体日期字段（DateTime）转 VO 字符串格式 → infoVO.setCreateTimeStr(DateUtil.format(resumeTemplate.getResumeTemplateCreatedTime(), "yyyy-MM-dd HH:mm:ss"));
                    // 示例2：实体字段名不同 → infoVO.setTemplateName(resumeTemplate.getResumeTemplateName());
                    return infoVO;
                })
                .collect(Collectors.toList());

        // 5. 直接返回 VO 列表（按方法定义返回 List，分页逻辑已通过 Page 对象实现）
        return voList;
    }
    @Override
    public List<ResumeTemplatePageVO> findAllResumeTemplate() {
        List<ResumeTemplate> resumeTemplates = resumeTemplateMapper.selectList(null);
        List<ResumeTemplatePageVO> resumeTemplatePageVOs = BeanUtil.copyToList(resumeTemplates, ResumeTemplatePageVO.class);
        return resumeTemplatePageVOs;
    }
}
