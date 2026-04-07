package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.LLMCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminLlmUtilsInfoMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminLlmUtilsInfoForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminLlmUtilsInfo;
import com.zyh.easyapplyresume.model.query.admin.AdminLlmUtilsInfoQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminLlmUtilsInfoPageVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminLlmUtilsInfoVO;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.AdminLlmUtilsInfoService;
import com.zyh.easyapplyresume.utils.adminvalidator.AdminLlmUtilsInfoFormValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminLlmUtilsInfoServiceImpl implements AdminLlmUtilsInfoService {

    @Autowired
    private AdminLlmUtilsInfoMapper adminLlmUtilsInfoMapper;

    @Override
    public Integer addAdminLlmUtilsInfo(AdminLlmUtilsInfoForm adminLlmUtilsInfoForm) {
        try {
            log.info("新增LLM工具类调用日志");
            AdminLlmUtilsInfoFormValidator.validateForAdd(adminLlmUtilsInfoForm);
            AdminLlmUtilsInfo adminLlmUtilsInfo = new AdminLlmUtilsInfo();
            adminLlmUtilsInfo.setLlmUtilsInfoCreatedTime(LocalDateTime.now());
            BeanUtils.copyProperties(adminLlmUtilsInfoForm, adminLlmUtilsInfo);
            int result = adminLlmUtilsInfoMapper.insert(adminLlmUtilsInfo);
            log.info("新增LLM工具类调用日志成功");
            return result;
        } catch (BusException e) {
            log.info("新增LLM工具类调用日志失败",e);
            throw e;
        } catch (Exception e) {
            log.error("新增LLM工具类调用日志失败", e);
            throw new RuntimeException("新增LLM工具类调用日志失败");
        }
    }

    @Override
    public AdminLlmUtilsInfoVO findAdminLlmUtilsInfoById(Long llmUtilsInfoId) {
        try {
            log.info("获取LLM工具类调用日志详情");
            AdminLlmUtilsInfo adminLlmUtilsInfo = adminLlmUtilsInfoMapper.selectById(llmUtilsInfoId);
            if (adminLlmUtilsInfo == null) {
                throw new BusException(LLMCodeEnum.LLM_USEPARAM_ERROR);
            }
            AdminLlmUtilsInfoVO adminLlmUtilsInfoVO = new AdminLlmUtilsInfoVO();
            BeanUtils.copyProperties(adminLlmUtilsInfo, adminLlmUtilsInfoVO);
            log.info("获取LLM工具类调用日志详情成功");
            return adminLlmUtilsInfoVO;
        } catch (BusException e) {
            log.info("获取LLM工具类调用日志详情失败", e);
            throw e;
        } catch (Exception e) {
            log.error("获取LLM工具类调用日志详情失败", e);
            throw new RuntimeException("获取LLM工具类调用日志详情失败");
        }
    }

    @Override
    public Page<AdminLlmUtilsInfoPageVO> findAdminLlmUtilsInfoByPage(Integer pageNum, Integer pageSize, AdminLlmUtilsInfoQuery adminLlmUtilsInfoQuery) {
        try {
            log.info("分页查询LLM工具类调用日志");
            Page<AdminLlmUtilsInfo> llmUtilsInfoPage = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<AdminLlmUtilsInfo> wrapper = new LambdaQueryWrapper<>();

            if (adminLlmUtilsInfoQuery != null) {
                if (adminLlmUtilsInfoQuery.getLlmUtilsInfoToolDescription() != null
                        && !adminLlmUtilsInfoQuery.getLlmUtilsInfoToolDescription().trim().isEmpty()) {
                    wrapper.like(AdminLlmUtilsInfo::getLlmUtilsInfoToolDescription,
                            adminLlmUtilsInfoQuery.getLlmUtilsInfoToolDescription().trim());
                }

                if (adminLlmUtilsInfoQuery.getLlmUtilsInfoModelProvider() != null
                        && !adminLlmUtilsInfoQuery.getLlmUtilsInfoModelProvider().trim().isEmpty()) {
                    wrapper.like(AdminLlmUtilsInfo::getLlmUtilsInfoModelProvider,
                            adminLlmUtilsInfoQuery.getLlmUtilsInfoModelProvider().trim());
                }

                if (adminLlmUtilsInfoQuery.getLlmUtilsInfoModelName() != null
                        && !adminLlmUtilsInfoQuery.getLlmUtilsInfoModelName().trim().isEmpty()) {
                    wrapper.like(AdminLlmUtilsInfo::getLlmUtilsInfoModelName,
                            adminLlmUtilsInfoQuery.getLlmUtilsInfoModelName().trim());
                }

                if (adminLlmUtilsInfoQuery.getLlmUtilsInfoStatus() != null
                        && !adminLlmUtilsInfoQuery.getLlmUtilsInfoStatus().trim().isEmpty()) {
                    wrapper.eq(AdminLlmUtilsInfo::getLlmUtilsInfoStatus,
                            adminLlmUtilsInfoQuery.getLlmUtilsInfoStatus().trim());
                }
            }

            wrapper.orderByDesc(AdminLlmUtilsInfo::getLlmUtilsInfoCreatedTime);

            Page<AdminLlmUtilsInfo> result = adminLlmUtilsInfoMapper.selectPage(llmUtilsInfoPage, wrapper);

            Page<AdminLlmUtilsInfoPageVO> pageVO = new Page<>(pageNum, pageSize);
            pageVO.setCurrent(result.getCurrent());
            pageVO.setSize(result.getSize());
            pageVO.setTotal(result.getTotal());
            pageVO.setPages(result.getPages());
            pageVO.setRecords(result.getRecords().stream().map(llmUtilsInfo -> {
                AdminLlmUtilsInfoPageVO vo = new AdminLlmUtilsInfoPageVO();
                BeanUtils.copyProperties(llmUtilsInfo, vo);
                return vo;
            }).toList());

            log.info("分页查询LLM工具类调用日志成功");
            return pageVO;
        } catch (BusException e) {
            log.info("分页查询LLM工具类调用日志失败", e);
            throw e;
        } catch (Exception e) {
            log.error("分页查询LLM工具类调用日志失败", e);
            throw new RuntimeException("分页查询LLM工具类调用日志失败");
        }
    }
}
