package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminFaqMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminFaqForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminFaq;
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
            faq.setFaqCreatedTime(new Date());
            faq.setFaqUpdatedTime(new Date());
            faq.setDeleted(0);
            int result = faqMapper.insert(faq);
            log.info("添加常见问题成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("添加常见问题失败");
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public Integer updateFaq(AdminFaqForm faqForm) {
        try {
            log.info("修改常见问题");
            // 校验表单字段
            AdminFaqValidator.validateForUpdate(faqForm);
            AdminFaq faq = new AdminFaq();
            BeanUtils.copyProperties(faqForm, faq);
            faq.setFaqUpdatedTime(new Date());
            int result = faqMapper.updateById(faq);
            log.info("修改常见问题成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("修改常见问题失败");
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public Integer deleteFaq(Integer faqId) {
        try {
            log.info("删除常见问题");
            AdminFaq faq = new AdminFaq();
            faq.setFaqId(faqId);
            faq.setDeleted(1);
            faq.setFaqUpdatedTime(new Date());
            int result = faqMapper.updateById(faq);
            log.info("删除常见问题成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("删除常见问题失败");
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public AdminFaqInfoVO getFaqInfo(Integer faqId) {
        try {
            log.info("获取常见问题信息");
            AdminFaq faq = faqMapper.selectById(faqId);
            if (faq == null || faq.getDeleted() == 1) {
                log.error("常见问题不存在");
                throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
            }
            AdminFaqInfoVO faqInfoVO = new AdminFaqInfoVO();
            BeanUtils.copyProperties(faq, faqInfoVO);
            log.info("获取常见问题信息成功");
            return faqInfoVO;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取常见问题信息失败");
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public Page<AdminFaqPageVO> getFaqPage(int size, int page) {
        try {
            log.info("分页查询常见问题");
            Page<AdminFaq> faqPage = new Page<>(page, size);
            Page<AdminFaq> result = faqMapper.selectPage(faqPage, null);
            // 转换为Page<AdminFaqPageVO>
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
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }
}

