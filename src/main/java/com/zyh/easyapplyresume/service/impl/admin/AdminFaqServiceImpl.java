package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminFaqMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminFaqForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminFaq;
import com.zyh.easyapplyresume.model.query.admin.AdminFaqQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminFaqInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminFaqPageVO;
import com.zyh.easyapplyresume.service.admin.AdminFaqService;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.utils.adminvalidator.AdminFaqValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 常见问题Service实现类
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminFaqServiceImpl implements AdminFaqService {
    @Autowired
    private AdminFaqMapper faqMapper;

    @Override
    public Integer addFaq(AdminFaqForm faqForm) {
        try {
            log.info("添加常见问题");
            // 校验表单字段
            AdminFaqValidator.validateForAdd(faqForm);
            AdminFaq faq = new AdminFaq();
            BeanUtils.copyProperties(faqForm, faq);
            faq.setFaqCreatedTime(LocalDateTime.now());
            faq.setFaqUpdatedTime(LocalDateTime.now());
            faq.setDeleted(0);
            int result = faqMapper.insert(faq);
            log.info("添加常见问题成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("添加常见问题失败");
            throw new BusException(AdminCodeEnum.FAQ_ADD_FAIL);
        }
    }

    @Override
    public Integer updateFaq(AdminFaqForm faqForm) {
        try {
            log.info("修改常见问题");
            AdminFaqValidator.validateForUpdate(faqForm);
            AdminFaq faq = new AdminFaq();
            BeanUtils.copyProperties(faqForm, faq);
            faq.setFaqUpdatedTime(LocalDateTime.now());
            // 使用 LambdaUpdateWrapper，避免 updateById 在全局逻辑删除配置下生成非法 SQL
            LambdaUpdateWrapper<AdminFaq> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(AdminFaq::getFaqId, faq.getFaqId());
            wrapper.eq(AdminFaq::getDeleted, 0);
            if (faq.getFaqTitle() != null)
                wrapper.set(AdminFaq::getFaqTitle, faq.getFaqTitle());
            if (faq.getFaqContent() != null)
                wrapper.set(AdminFaq::getFaqContent, faq.getFaqContent());
            wrapper.set(AdminFaq::getFaqUpdatedTime, faq.getFaqUpdatedTime());
            int result = faqMapper.update(null, wrapper);
            log.info("修改常见问题成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("修改常见问题失败");
            throw new BusException(AdminCodeEnum.FAQ_UPDATE_FAIL);
        }
    }

    @Override
    public Integer deleteFaq(Integer faqId) {
        try {
            log.info("删除常见问题");
            // 使用 LambdaUpdateWrapper，避免 updateById 在全局逻辑删除配置下生成非法 SQL
            LambdaUpdateWrapper<AdminFaq> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(AdminFaq::getFaqId, faqId);
            wrapper.eq(AdminFaq::getDeleted, 0);
            wrapper.set(AdminFaq::getDeleted, 1);
            wrapper.set(AdminFaq::getFaqUpdatedTime, LocalDateTime.now());
            int result = faqMapper.update(null, wrapper);
            log.info("删除常见问题成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("删除常见问题失败");
            throw new BusException(AdminCodeEnum.FAQ_DELETE_FAIL);
        }
    }

    @Override
    public AdminFaqInfoVO getFaqInfo(Integer faqId) {
        try {
            log.info("获取常见问题信息");
            AdminFaq faq = faqMapper.selectById(faqId);
            if (faq == null || faq.getDeleted() == 1) {
                log.error("常见问题不存在");
                throw new BusException(AdminCodeEnum.FAQ_NOT_FOUND);
            }
            AdminFaqInfoVO faqInfoVO = new AdminFaqInfoVO();
            BeanUtils.copyProperties(faq, faqInfoVO);
            log.info("获取常见问题信息成功");
            return faqInfoVO;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取常见问题信息失败");
            throw new BusException(AdminCodeEnum.FAQ_GET_INFO_FAIL);
        }
    }

    @Override
    public Page<AdminFaqPageVO> getFaqPage(int size, int page, AdminFaqQuery faqQuery) {
        try {
            log.info("分页查询常见问题");
            Page<AdminFaq> faqPage = new Page<>(page, size);
            LambdaQueryWrapper<AdminFaq> wrapper = new LambdaQueryWrapper<>();
            // 只查询未删除
            wrapper.eq(AdminFaq::getDeleted, 0);
            // 按标题模糊查询
            if (faqQuery != null && faqQuery.getFaqTitle() != null && !faqQuery.getFaqTitle().trim().isEmpty()) {
                wrapper.like(AdminFaq::getFaqTitle, faqQuery.getFaqTitle().trim());
            }
            Page<AdminFaq> result = faqMapper.selectPage(faqPage, wrapper);
            Page<AdminFaqPageVO> pageVO = new Page<>(page, size);
            pageVO.setTotal(result.getTotal());
            pageVO.setRecords(result.getRecords().stream().map(faq -> {
                AdminFaqPageVO vo = new AdminFaqPageVO();
                BeanUtils.copyProperties(faq, vo);
                return vo;
            }).toList());
            log.info("分页查询常见问题成功");
            return pageVO;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("分页查询常见问题失败");
            throw new BusException(AdminCodeEnum.FAQ_GET_PAGE_FAIL);
        }
    }
}

