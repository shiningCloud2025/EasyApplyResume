package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.EmploymentInformationMapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.RecruitPositionMapper;
import com.zyh.easyapplyresume.model.form.admin.RecruitPositionForm;
import com.zyh.easyapplyresume.model.pojo.admin.EmploymentInformation;
import com.zyh.easyapplyresume.model.pojo.admin.RecruitPosition;
import com.zyh.easyapplyresume.model.query.admin.RecruitPositionQuery;
import com.zyh.easyapplyresume.model.vo.admin.RecruitPositionInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.RecruitPositionPageVO;
import com.zyh.easyapplyresume.redis.constant.common.RecruitPositionCacheKey;
import com.zyh.easyapplyresume.redis.enums.CacheOperationType;
import com.zyh.easyapplyresume.redis.util.CacheInvalidatePublisher;
import com.zyh.easyapplyresume.redis.util.RedisCacheUtil;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.RecruitPositionService;
import com.zyh.easyapplyresume.utils.adminvalidator.RecruitPositionFormValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class RecruitPositionServiceImpl implements RecruitPositionService {
    @Autowired
    private RecruitPositionMapper recruitPositionMapper;
    @Autowired
    private RedisCacheUtil redisCacheUtil;
    @Autowired
    private CacheInvalidatePublisher cachePublisher;
    @Autowired
    private EmploymentInformationMapper employmentInformationMapper;
    @Override
    public Integer addRecruitPosition(RecruitPositionForm recruitPositionForm) {
        RecruitPositionFormValidator.validateForAdd(recruitPositionForm);
        RecruitPosition recruitPosition = new RecruitPosition();
        BeanUtils.copyProperties(recruitPositionForm, recruitPosition);
        recruitPosition.setCreatedTime(new Date());
        recruitPosition.setUpdatedTime(new Date());
        int result = recruitPositionMapper.insert(recruitPosition);
        
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    cachePublisher.publishInvalidate(
                        RecruitPositionCacheKey.ALL_PATTERN,
                        CacheOperationType.ADD
                    );
                }
            }
        );
        
        return result;
    }

    @Override
    public Integer updateRecruitPosition(RecruitPositionForm recruitPositionForm) {
        RecruitPositionFormValidator.validateForUpdate(recruitPositionForm);
        RecruitPosition recruitPosition = new RecruitPosition();
        BeanUtils.copyProperties(recruitPositionForm, recruitPosition);
        recruitPosition.setUpdatedTime(new Date());
        int result = recruitPositionMapper.updateById(recruitPosition);
        
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    cachePublisher.publishInvalidate(
                        RecruitPositionCacheKey.ALL_PATTERN,
                        CacheOperationType.UPDATE
                    );
                }
            }
        );
        
        return result;
    }

    @Override
    public Integer deleteRecruitPosition(Integer recruitPositionId) {
        try {
            if (recruitPositionId == 1){
                throw new BusException(AdminCodeEnum.NOT_DELETE_RECRUIT_POSITION);
            }

            LambdaQueryWrapper<EmploymentInformation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(EmploymentInformation::getEmploymentInformationRecruitPosition, recruitPositionId);
            lambdaQueryWrapper.eq(EmploymentInformation::getDeleted, 0);

            Long employmentInformationCount = employmentInformationMapper.selectCount(lambdaQueryWrapper);
            if (employmentInformationCount != null && employmentInformationCount > 0) {
                throw new BusException(AdminCodeEnum.NOT_DELETE_RECRUIT_POSITION);
            }

            int result = recruitPositionMapper.deleteById(recruitPositionId);
            
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        cachePublisher.publishInvalidate(
                            RecruitPositionCacheKey.ALL_PATTERN,
                            CacheOperationType.DELETE
                        );
                    }
                }
            );
            
            return result;
        }  catch (BusException e) {
            log.info("删除招聘岗位业务异常: {}", e.getMsg());
            throw e;
        } catch (Exception e) {
            log.error("删除招聘岗位系统异常", e);
            throw new RuntimeException("招聘岗位删除失败");
        }
    }

    @Override
    public RecruitPositionInfoVO queryRecruitPosition(Integer recruitPositionId) {
        String cacheKey = RecruitPositionCacheKey.GET_PREFIX + "_" + recruitPositionId;
        
        Object cached = redisCacheUtil.get(cacheKey);
        if (cached != null) {
            return (RecruitPositionInfoVO) cached;
        }
        
        RecruitPositionInfoVO result = recruitPositionMapper.findRecruitPositionInfoById(recruitPositionId);
        
        redisCacheUtil.set(cacheKey, result, RecruitPositionCacheKey.GET_TTL, TimeUnit.MINUTES);
        
        return result;
    }

    @Override
    public Page<RecruitPositionPageVO> queryRecruitPositionPage(Integer pageNum, Integer pageSize, RecruitPositionQuery recruitPositionQuery) {
        boolean hasQueryCondition = recruitPositionQuery != null && (
                (recruitPositionQuery.getRecruitPositionName() != null
                        && !recruitPositionQuery.getRecruitPositionName().trim().isEmpty())
                        || recruitPositionQuery.getRecruitPositionIndustryCode() != null
                        || recruitPositionQuery.getMinMonthSalary() != null
                        || recruitPositionQuery.getMaxMonthSalary() != null
                        || recruitPositionQuery.getWeekWorkDayNum() != null
        );

        String cacheKey = RecruitPositionCacheKey.PAGE_PREFIX
                + "_" + pageNum
                + "_" + pageSize;

        Page<RecruitPositionPageVO> page = new Page<>(pageNum, pageSize);

        if (!hasQueryCondition) {
            Object cached = redisCacheUtil.get(cacheKey);
            if (cached != null) {
                return (Page<RecruitPositionPageVO>) cached;
            }

            Page<RecruitPositionPageVO> result = recruitPositionMapper.queryRecruitPositionPage(page, recruitPositionQuery);
            redisCacheUtil.set(cacheKey, result, RecruitPositionCacheKey.PAGE_TTL, TimeUnit.MINUTES);
            return result;
        }

        return recruitPositionMapper.queryRecruitPositionPage(page, recruitPositionQuery);
    }

    @Override
    public List<RecruitPositionInfoVO> queryAllRecruitPositionPage() {
        Object cached = redisCacheUtil.get(RecruitPositionCacheKey.LIST);
        if (cached != null) {
            return (List<RecruitPositionInfoVO>) cached;
        }
        
        List<RecruitPosition> recruitPositions = recruitPositionMapper.selectList(null);
        List<RecruitPositionInfoVO> result = BeanUtil.copyToList(recruitPositions, RecruitPositionInfoVO.class);
        
        redisCacheUtil.set(RecruitPositionCacheKey.LIST, result, RecruitPositionCacheKey.LIST_TTL, TimeUnit.MINUTES);
        
        return result;
    }

    @Override
    public List<RecruitPositionInfoVO> queryAllRecruitPositionForUser() {
        List<RecruitPositionInfoVO> recruitPositionInfoVOS = queryAllRecruitPositionPage();
        return recruitPositionInfoVOS.stream()
                .filter(recruitPositionInfoVO -> {
                    String recruitPositionName = recruitPositionInfoVO.getRecruitPositionName();
                    return recruitPositionName != null && !recruitPositionName.endsWith("*");
                })
                .toList();
    }

    @Override
    public List<RecruitPositionInfoVO> queryAllRecruitPositionForEmployment() {
        List<RecruitPositionInfoVO> recruitPositionInfoVOS = queryAllRecruitPositionPage();
        return recruitPositionInfoVOS.stream()
                .filter(recruitPositionInfoVO -> {
                    String recruitPositionName = recruitPositionInfoVO.getRecruitPositionName();
                    return recruitPositionName != null && recruitPositionName.endsWith("*");
                })
                .toList();
    }
}
