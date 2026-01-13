package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.locationenum.CityEnum;
import com.zyh.easyapplyresume.bean.locationenum.ProvinceEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.EmploymentInformationMapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.IndustryMapMapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.ProvinceMapMapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.RecruitPositionMapper;
import com.zyh.easyapplyresume.model.form.admin.EmploymentInformationForm;
import com.zyh.easyapplyresume.model.pojo.admin.EmploymentInformation;
import com.zyh.easyapplyresume.model.query.admin.EmploymentInformationQuery;
import com.zyh.easyapplyresume.model.vo.admin.EmploymentInformationInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.EmploymentInformationPageVO;
import com.zyh.easyapplyresume.redis.constant.common.EmploymentInformationCacheKey;
import com.zyh.easyapplyresume.redis.enums.CacheOperationType;
import com.zyh.easyapplyresume.redis.util.CacheInvalidatePublisher;
import com.zyh.easyapplyresume.redis.util.RedisCacheUtil;
import com.zyh.easyapplyresume.service.admin.EmploymentInformationService;
import com.zyh.easyapplyresume.utils.adminvalidator.EmploymentInformationFormValidator;
import kotlin.jvm.internal.Lambda;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author shiningCloud2025
 */
@Service
@Transactional
@Slf4j
public class EmploymentInformationServiceImpl implements EmploymentInformationService {
    @Autowired
    private EmploymentInformationMapper employmentInformationMapper;
    @Autowired
    private ProvinceMapMapper provinceMapMapper;
    @Autowired
    private IndustryMapMapper industryMapMapper;
    @Autowired
    private RecruitPositionMapper recruitPositionMapper;
    @Autowired
    private RedisCacheUtil redisCacheUtil;
    @Autowired
    private CacheInvalidatePublisher cachePublisher;

    @Override
    public Integer addEmploymentInformation(EmploymentInformationForm employmentInformationForm) {
        try{
            log.info("开始添加招聘信息");
            EmploymentInformationFormValidator.validateForAdd(employmentInformationForm);
            LambdaQueryWrapper<EmploymentInformation> judgeQueryWrapper = new LambdaQueryWrapper<>();
            judgeQueryWrapper.eq(EmploymentInformation::getEmploymentInformationCompanyName, employmentInformationForm.getEmploymentInformationCompanyName());
            judgeQueryWrapper.eq(EmploymentInformation::getDeleted,0);
            List<EmploymentInformation> employmentInformations = employmentInformationMapper.selectList(judgeQueryWrapper);
            if (employmentInformations.size() !=0||!employmentInformations.isEmpty()) {
                throw new BusException(AdminCodeEnum.EMPLOYMENT_COMPANY_NAME_DUPLICATE);
            }

            EmploymentInformation employmentInformation = BeanUtil.copyProperties(employmentInformationForm, EmploymentInformation.class);
            LambdaQueryWrapper<EmploymentInformation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.orderByDesc(EmploymentInformation::getEmploymentInformationId);
            if (employmentInformationMapper.selectList(lambdaQueryWrapper).isEmpty()){
                employmentInformation.setEmploymentInformationCode(1);
            }else {
                Integer employmentInformationId = employmentInformationMapper.selectList(null).get(0).getEmploymentInformationId();
                employmentInformation.setEmploymentInformationCode(employmentInformationId+1);
            }
            List<Integer>  provinceIds = employmentInformationForm.getEmploymentInformationRecruitLocationFirstList();
            List<Integer>  cityIds = employmentInformationForm.getEmploymentInformationRecruitLocationSecondList();
            // 校验长度一致
            if (provinceIds.size() != cityIds.size()) {
                throw new BusException(AdminCodeEnum.EMPLOYMENT_LOCATION_LENGTH_NOT_MATCH);
            }
            // 获取两个集合的迭代器
            Iterator<Integer> provinceIt = provinceIds.iterator();
            Iterator<Integer> cityIt = cityIds.iterator();
            List<EmploymentInformation> res = new LinkedList<>();
            // 同步遍历：一次取一组
            while (provinceIt.hasNext() && cityIt.hasNext()) {
                Integer provinceId = provinceIt.next();
                Integer cityId = cityIt.next();
                EmploymentInformation employmentInformation1 = BeanUtil.copyProperties(employmentInformation, employmentInformation.getClass());
                employmentInformation1.setEmploymentInformationRecruitLocationFirst(provinceId);
                employmentInformation1.setEmploymentInformationRecruitLocationSecond(cityId);
                String provinceName = Objects.requireNonNull(ProvinceEnum.getById(provinceId)).getName();
                String cityName = Objects.requireNonNull(CityEnum.getById(cityId)).getName();
                employmentInformation1.setEmploymentInformationStartTime(new Date());
                employmentInformation1.setEmploymentInformationUpdatedTime(new Date());
                employmentInformation1.setEmploymentInformationRecruitLocationDetail(provinceName + cityName);
                res.add(employmentInformation1);
            }
            List<BatchResult> countList = employmentInformationMapper.insert(res);
            
            // 注册事务提交后的回调，确保事务成功后才发布事件
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        // 事务提交成功后，才发布事件删除缓存
                        cachePublisher.publishInvalidate(
                            EmploymentInformationCacheKey.PAGE_PATTERN,
                            CacheOperationType.ADD
                        );
                    }
                }
            );
            
            log.info("结束添加招聘信息");
            return countList.size();
        }catch (BusException e){
            throw e;
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("添加招聘信息有问题");
        }

    }
    // 为了修改重载的
    private Integer addEmploymentInformationForUpdate(EmploymentInformationForm employmentInformationForm,Date startTime) {
        try{
            EmploymentInformationFormValidator.validateForAdd(employmentInformationForm);
            LambdaQueryWrapper<EmploymentInformation> judgeQueryWrapper = new LambdaQueryWrapper<>();
            judgeQueryWrapper.eq(EmploymentInformation::getEmploymentInformationCompanyName, employmentInformationForm.getEmploymentInformationCompanyName());
            judgeQueryWrapper.eq(EmploymentInformation::getDeleted,0);
            List<EmploymentInformation> employmentInformations = employmentInformationMapper.selectList(judgeQueryWrapper);
            if (employmentInformations.size() !=0||!employmentInformations.isEmpty()) {
                throw new BusException(AdminCodeEnum.EMPLOYMENT_COMPANY_NAME_DUPLICATE);
            }
            employmentInformationForm.setEmploymentInformationId(null);
            EmploymentInformation employmentInformation = BeanUtil.copyProperties(employmentInformationForm, EmploymentInformation.class);
            LambdaQueryWrapper<EmploymentInformation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.orderByDesc(EmploymentInformation::getEmploymentInformationId);
            // TODO:有一定的并发问题，因为是先删除，后新增，那么在删除的时候如果别的增加且你已经完成了查询 那就回有并发问题，但是概率比较低！
            if(employmentInformationMapper.selectList(lambdaQueryWrapper).isEmpty()){
                employmentInformation.setEmploymentInformationCode(1);
            }else{
                Integer employmentInformationId = employmentInformationMapper.selectList(lambdaQueryWrapper).getFirst().getEmploymentInformationId();
                employmentInformation.setEmploymentInformationCode(employmentInformationId+1);
            }
            List<Integer>  provinceIds = employmentInformationForm.getEmploymentInformationRecruitLocationFirstList();
            List<Integer>  cityIds = employmentInformationForm.getEmploymentInformationRecruitLocationSecondList();
            // 校验长度一致
            if (provinceIds.size() != cityIds.size()) {
                throw new BusException(AdminCodeEnum.EMPLOYMENT_LOCATION_LENGTH_NOT_MATCH);
            }
            // 获取两个集合的迭代器
            Iterator<Integer> provinceIt = provinceIds.iterator();
            Iterator<Integer> cityIt = cityIds.iterator();
            List<EmploymentInformation> res = new LinkedList<>();
            // 同步遍历：一次取一组
            while (provinceIt.hasNext() && cityIt.hasNext()) {
                Integer provinceId = provinceIt.next();
                Integer cityId = cityIt.next();
                EmploymentInformation employmentInformation1 = BeanUtil.copyProperties(employmentInformation, employmentInformation.getClass());
                employmentInformation1.setEmploymentInformationRecruitLocationFirst(provinceId);
                employmentInformation1.setEmploymentInformationRecruitLocationSecond(cityId);
                String provinceName = Objects.requireNonNull(ProvinceEnum.getById(provinceId)).getName();
                String cityName = Objects.requireNonNull(CityEnum.getById(cityId)).getName();
                employmentInformation1.setEmploymentInformationStartTime(startTime);
                employmentInformation1.setEmploymentInformationUpdatedTime(new Date());
                employmentInformation1.setEmploymentInformationRecruitLocationDetail(provinceName + cityName);
                res.add(employmentInformation1);
            }
            List<BatchResult> countList = employmentInformationMapper.insert(res);
            return countList.size();
        }catch (BusException e){
            throw e;
        }
        catch (Exception e){
            throw new RuntimeException("");
        }
    }

    private BusException resolveDbException(Exception e) {
        String errorMsg = e.getMessage();

        // 1. 处理唯一约束冲突
        if (errorMsg != null && (errorMsg.contains("Duplicate entry") || e instanceof org.springframework.dao.DuplicateKeyException)) {
            // --- 关键修改点：请根据你的数据库唯一索引名称进行修改 ---
            // 假设你的唯一索引是分别针对 company_name 和 submission_way 字段创建的
            // 或者是一个复合唯一索引，索引名为 idx_company_submission

            // 匹配公司名称字段或其唯一索引
            if (errorMsg.contains("employmentInformation_companyName") || errorMsg.contains("idx_employment_company_name")) {
                return new BusException(AdminCodeEnum.EMPLOYMENT_COMPANY_NAME_DUPLICATE);
            }
            // 匹配投递方式字段或其唯一索引
            else if (errorMsg.contains("employmentInformation_submissionWay") || errorMsg.contains("idx_employment_submission_way")) {
                return new BusException(AdminCodeEnum.EMPLOYMENT_SUBMISSION_WAY_DUPLICATE);
            }

        }

        // 兜底：未匹配到特定的唯一冲突，返回通用数据库异常
        return new BusException(AdminCodeEnum.DB_EXCEPTION_TRANSFORM_FAIL_EXCEPTION);
    }

    @Override
    public Integer updateEmploymentInformation(EmploymentInformationForm employmentInformationForm) {
        EmploymentInformationFormValidator.validateForUpdate(employmentInformationForm);
        LambdaQueryWrapper<EmploymentInformation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EmploymentInformation::getEmploymentInformationCode, employmentInformationForm.getEmploymentInformationId());
        lambdaQueryWrapper.eq(EmploymentInformation::getDeleted, 0);
        List<EmploymentInformation> employmentInformations = employmentInformationMapper.selectList(lambdaQueryWrapper);
        Date employmentInformationStartTime = employmentInformations.get(0).getEmploymentInformationStartTime();
        deleteEmploymentInformation(employmentInformationForm.getEmploymentInformationId());
        Integer result = addEmploymentInformationForUpdate(employmentInformationForm,employmentInformationStartTime);
        
        // 注册事务提交后的回调，确保事务成功后才发布事件
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    // 事务提交成功后，才发布事件删除所有缓存
                    cachePublisher.publishInvalidate(
                        EmploymentInformationCacheKey.ALL_PATTERN,
                        CacheOperationType.UPDATE
                    );
                }
            }
        );
        
        return result;
    }

    @Override
    public Integer deleteEmploymentInformation(Integer employmentInformationId) {
//        LambdaQueryWrapper<EmploymentInformation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(EmploymentInformation::getEmploymentInformationCode, employmentInformationId);
//        List<EmploymentInformation> employmentInformations = employmentInformationMapper.selectList(lambdaQueryWrapper);
//        for(EmploymentInformation employmentInformation : employmentInformations){
//            employmentInformation.setDeleted(1);
//            employmentInformationMapper.updateById(employmentInformation);
//        }
        // 1. 创建 LambdaUpdateWrapper（指定实体类）
        LambdaUpdateWrapper<EmploymentInformation> updateWrapper = new LambdaUpdateWrapper<>();
        // 2. 设置更新字段：deleted = 1
        updateWrapper.set(EmploymentInformation::getDeleted, 1);

        // 3. 设置查询条件：employmentInformationCode = employmentInformationId（和原代码条件一致）
        updateWrapper.eq(EmploymentInformation::getEmploymentInformationCode, employmentInformationId);
        updateWrapper.eq(EmploymentInformation::getDeleted,0);


        // 4. 执行单条UPDATE语句，批量更新所有符合条件的记录
        int updateCount = employmentInformationMapper.update(null, updateWrapper);
        
        // 注册事务提交后的回调，确保事务成功后才发布事件
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    // 事务提交成功后，才发布事件删除所有缓存
                    cachePublisher.publishInvalidate(
                        EmploymentInformationCacheKey.ALL_PATTERN,
                        CacheOperationType.DELETE
                    );
                }
            }
        );
        
        return updateCount;
    }

    @Override
    public EmploymentInformationInfoVO getEmploymentInformationInfo(Integer employmentInformationId) {
        // 1. 生成缓存Key
        String cacheKey = EmploymentInformationCacheKey.GET_PREFIX + "_" + employmentInformationId;
        
        // 2. 先查缓存
        Object cached = redisCacheUtil.get(cacheKey);
        if (cached != null) {
            return (EmploymentInformationInfoVO) cached;
        }
        
        // 3. 缓存未命中，查数据库
        LambdaQueryWrapper<EmploymentInformation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EmploymentInformation::getEmploymentInformationCode, employmentInformationId);
        lambdaQueryWrapper.eq(EmploymentInformation::getDeleted, 0);
        List<EmploymentInformation> employmentInformations = employmentInformationMapper.selectList(lambdaQueryWrapper);
        List<String> provinces = new LinkedList<>();
        List<String> cities = new LinkedList<>();
        List<String> details = new LinkedList<>();
        for(EmploymentInformation employmentInformation : employmentInformations){
            provinces.add(Objects.requireNonNull(ProvinceEnum.getById(employmentInformation.getEmploymentInformationRecruitLocationFirst())).getName());
            cities.add( Objects.requireNonNull(CityEnum.getById(employmentInformation.getEmploymentInformationRecruitLocationSecond())).getName());
            details.add(Objects.requireNonNull(ProvinceEnum.getById(employmentInformation.getEmploymentInformationRecruitLocationFirst())).getName()+
                            Objects.requireNonNull(CityEnum.getById(employmentInformation.getEmploymentInformationRecruitLocationSecond())).getName()
                    );
        }
        EmploymentInformationInfoVO employmentInformationInfoVO = BeanUtil.copyProperties(employmentInformations.getFirst(), EmploymentInformationInfoVO.class);
        employmentInformationInfoVO.setEmploymentInformationRecruitLocationFirstName(provinces);
        employmentInformationInfoVO.setEmploymentInformationRecruitLocationSecondName(cities);
        employmentInformationInfoVO.setEmploymentInformationRecruitLocationDetail(details);
        employmentInformationInfoVO.setEmploymentInformationIndustryCategoriesName(industryMapMapper.selectById(employmentInformations.getFirst().getEmploymentInformationIndustryCategories()).getIndustryMapIndustryName());
        employmentInformationInfoVO.setEmploymentInformationRecruitPositionName(recruitPositionMapper.selectById(employmentInformations.getFirst().getEmploymentInformationRecruitPosition()).getRecruitPositionName());
        
        // 4. 写入缓存
        redisCacheUtil.set(cacheKey, employmentInformationInfoVO, EmploymentInformationCacheKey.GET_TTL, TimeUnit.MINUTES);
        
        return employmentInformationInfoVO;
    }
    /**
     * 招聘信息分页查询实现
     */
    @Override
    public Page<EmploymentInformationPageVO> getEmploymentInformationPage(int page, int size, EmploymentInformationQuery employmentInformationQuery) {
        // 1. 生成缓存Key（带查询条件hash）
        String cacheKey = EmploymentInformationCacheKey.PAGE_PREFIX 
                        + "_" + page 
                        + "_" + size 
                        + "_" + (employmentInformationQuery != null ? employmentInformationQuery.hashCode() : 0);
        
        // 2. 先查缓存
        Object cached = redisCacheUtil.get(cacheKey);
        if (cached != null) {
            return (Page<EmploymentInformationPageVO>) cached;
        }
        
        // 3. 缓存未命中，查数据库
        // 构建 LambdaQueryWrapper（指定数据库实体类 EmploymentInformation）
        LambdaQueryWrapper<EmploymentInformation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EmploymentInformation::getDeleted, 0);

        // 2. 判空过滤：查询条件不为空时，按字段类型添加对应查询规则（String模糊查询，Integer/Date精确匹配）
        if (employmentInformationQuery != null) {
            // 公司名称（String类型 → 模糊查询）
            if (employmentInformationQuery.getEmploymentInformationCompanyName() != null && !employmentInformationQuery.getEmploymentInformationCompanyName().trim().isEmpty()) {
                lambdaQueryWrapper.like(EmploymentInformation::getEmploymentInformationCompanyName, employmentInformationQuery.getEmploymentInformationCompanyName().trim());
            }

            // 行业大类（Integer类型 → 精确匹配）
            if (employmentInformationQuery.getEmploymentInformationIndustryCategories() != null) {
                lambdaQueryWrapper.eq(EmploymentInformation::getEmploymentInformationIndustryCategories, employmentInformationQuery.getEmploymentInformationIndustryCategories());
            }

            // 企业性质（Integer类型 → 精确匹配）
            if (employmentInformationQuery.getEmploymentInformationCompanyType() != null) {
                lambdaQueryWrapper.eq(EmploymentInformation::getEmploymentInformationCompanyType, employmentInformationQuery.getEmploymentInformationCompanyType());
            }

            // 招聘批次（Integer类型 → 精确匹配）
            if (employmentInformationQuery.getEmploymentInformationBatch() != null) {
                lambdaQueryWrapper.eq(EmploymentInformation::getEmploymentInformationBatch, employmentInformationQuery.getEmploymentInformationBatch());
            }

            // 招聘岗位（Integer类型 → 精确匹配）
            if (employmentInformationQuery.getEmploymentInformationRecruitPosition() != null) {
                lambdaQueryWrapper.eq(EmploymentInformation::getEmploymentInformationRecruitPosition, employmentInformationQuery.getEmploymentInformationRecruitPosition());
            }

            // 招聘对象（Integer类型 → 精确匹配）
            if (employmentInformationQuery.getEmploymentInformationRecruitObject() != null) {
                lambdaQueryWrapper.eq(EmploymentInformation::getEmploymentInformationRecruitObject, employmentInformationQuery.getEmploymentInformationRecruitObject());
            }

            // 招聘地址(省级)（Integer类型 → 精确匹配）
            if (employmentInformationQuery.getEmploymentInformationRecruitLocationFirst() != null) {
                lambdaQueryWrapper.eq(EmploymentInformation::getEmploymentInformationRecruitLocationFirst, employmentInformationQuery.getEmploymentInformationRecruitLocationFirst());
            }

            // 招聘地址(市级)（Integer类型 → 精确匹配）
            if (employmentInformationQuery.getEmploymentInformationRecruitLocationSecond() != null) {
                lambdaQueryWrapper.eq(EmploymentInformation::getEmploymentInformationRecruitLocationSecond, employmentInformationQuery.getEmploymentInformationRecruitLocationSecond());
            }

            // 详细招聘地址（String类型 → 模糊查询）
            if (employmentInformationQuery.getEmploymentInformationRecruitLocationDetail() != null && !employmentInformationQuery.getEmploymentInformationRecruitLocationDetail().trim().isEmpty()) {
                lambdaQueryWrapper.like(EmploymentInformation::getEmploymentInformationRecruitLocationDetail, employmentInformationQuery.getEmploymentInformationRecruitLocationDetail().trim());
            }

            // 截止时间（Date类型 → 小于等于查询，适配"截止时间前的招聘信息"需求）
            if (employmentInformationQuery.getEmploymentInformationStopTime() != null) {
                lambdaQueryWrapper.le(EmploymentInformation::getEmploymentInformationStopTime, employmentInformationQuery.getEmploymentInformationStopTime());
            }

            // 网申状态（String类型 → 模糊查询）
            if (employmentInformationQuery.getEmploymentInformationOnlineApplicationStatus() != null && !employmentInformationQuery.getEmploymentInformationOnlineApplicationStatus().trim().isEmpty()) {
                lambdaQueryWrapper.like(EmploymentInformation::getEmploymentInformationOnlineApplicationStatus, employmentInformationQuery.getEmploymentInformationOnlineApplicationStatus().trim());
            }

            // 官方公告（String类型 → 模糊查询）
            if (employmentInformationQuery.getEmploymentInformationOfficialAnnouncement() != null && !employmentInformationQuery.getEmploymentInformationOfficialAnnouncement().trim().isEmpty()) {
                lambdaQueryWrapper.like(EmploymentInformation::getEmploymentInformationOfficialAnnouncement, employmentInformationQuery.getEmploymentInformationOfficialAnnouncement().trim());
            }

            // 投递方式（String类型 → 模糊查询）
            if (employmentInformationQuery.getEmploymentInformationSubmissionWay() != null && !employmentInformationQuery.getEmploymentInformationSubmissionWay().trim().isEmpty()) {
                lambdaQueryWrapper.like(EmploymentInformation::getEmploymentInformationSubmissionWay, employmentInformationQuery.getEmploymentInformationSubmissionWay().trim());
            }

            // 内推码（String类型 → 模糊查询）
            if (employmentInformationQuery.getEmploymentInformationEmployeeReferralCode() != null && !employmentInformationQuery.getEmploymentInformationEmployeeReferralCode().trim().isEmpty()) {
                lambdaQueryWrapper.like(EmploymentInformation::getEmploymentInformationEmployeeReferralCode, employmentInformationQuery.getEmploymentInformationEmployeeReferralCode().trim());
            }
        }

        // 3. 调用 Mapper 分页查询（依赖 EmploymentInformationMapper 继承 MyBatis-Plus BaseMapper）
        Page<EmploymentInformation> employmentInfoPage = employmentInformationMapper.selectPage(
                new Page<>(page, size),  // 分页参数：当前页（page）、每页条数（size）
                lambdaQueryWrapper        // 多条件组合查询（精确+模糊）
        );

        Map<Integer,EmploymentInformationPageVO> map = new HashMap<>();
        for(EmploymentInformation info : employmentInfoPage.getRecords()){
            if(map.containsKey(info.getEmploymentInformationCode())){
                EmploymentInformationPageVO pageVO = map.get(info.getEmploymentInformationCode());
                pageVO.getEmploymentInformationRecruitLocationFirstName().add(Objects.requireNonNull(ProvinceEnum.getById(info.getEmploymentInformationRecruitLocationFirst())).getName());
                pageVO.getEmploymentInformationRecruitLocationSecondName().add(Objects.requireNonNull(CityEnum.getById(info.getEmploymentInformationRecruitLocationSecond())).getName());
                pageVO.getEmploymentInformationRecruitLocationDetail().add(Objects.requireNonNull(ProvinceEnum.getById(info.getEmploymentInformationRecruitLocationFirst())).getName()+
                        Objects.requireNonNull(CityEnum.getById(info.getEmploymentInformationRecruitLocationSecond())).getName()
                );
            }else{
                EmploymentInformationPageVO pageVO = BeanUtil.copyProperties(info, EmploymentInformationPageVO.class);
                List<String> provinces = new LinkedList<>();
                List<String> cities = new LinkedList<>();
                List<String> details = new LinkedList<>();
                provinces.add(Objects.requireNonNull(ProvinceEnum.getById(info.getEmploymentInformationRecruitLocationFirst())).getName());
                cities.add(Objects.requireNonNull(CityEnum.getById(info.getEmploymentInformationRecruitLocationSecond())).getName());
                details.add(Objects.requireNonNull(ProvinceEnum.getById(info.getEmploymentInformationRecruitLocationFirst())).getName()+
                        Objects.requireNonNull(CityEnum.getById(info.getEmploymentInformationRecruitLocationSecond())).getName()
                );
                pageVO.setEmploymentInformationRecruitLocationFirstName(provinces);
                pageVO.setEmploymentInformationRecruitLocationSecondName(cities);
                pageVO.setEmploymentInformationRecruitLocationDetail(details);
                pageVO.setEmploymentInformationIndustryCategoriesName(industryMapMapper.selectById(info.getEmploymentInformationIndustryCategories()).getIndustryMapIndustryName());
                pageVO.setEmploymentInformationRecruitPositionName(recruitPositionMapper.selectById(info.getEmploymentInformationRecruitPosition()).getRecruitPositionName());
                map.put(info.getEmploymentInformationCode(),pageVO);
            }
        }
        List<EmploymentInformationPageVO> voList = new ArrayList<>(map.values());

        // 5. 构建返回的 Page<VO> 对象（保留分页元数据：总条数、总页数等）
        Page<EmploymentInformationPageVO> resultPage = new Page<>();
        resultPage.setCurrent(employmentInfoPage.getCurrent()); // 当前页
        resultPage.setSize(employmentInfoPage.getSize());       // 每页条数
        resultPage.setTotal(employmentInfoPage.getTotal());     // 总数据条数
        resultPage.setPages(employmentInfoPage.getPages());     // 总页数
        resultPage.setRecords(voList != null ? voList : Collections.emptyList()); // 分页数据列表

        // 4. 写入缓存
        redisCacheUtil.set(cacheKey, resultPage, EmploymentInformationCacheKey.PAGE_TTL, TimeUnit.MINUTES);
        
        return resultPage;
    }

    @Override
    public List<EmploymentInformationInfoVO> getAllEmploymentInformation() {
        LambdaQueryWrapper<EmploymentInformation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EmploymentInformation::getDeleted, 0);
        List<EmploymentInformation> employmentInformations = employmentInformationMapper.selectList(lambdaQueryWrapper);
        Map<Integer,EmploymentInformationInfoVO> map = new HashMap<>();
        for(EmploymentInformation employmentInformation : employmentInformations){
            if(map.containsKey(employmentInformation.getEmploymentInformationCode())){
                EmploymentInformationInfoVO employmentInformation1 = map.get(employmentInformation.getEmploymentInformationCode());
                employmentInformation1.getEmploymentInformationRecruitLocationFirstName().add(Objects.requireNonNull(ProvinceEnum.getById(employmentInformation.getEmploymentInformationRecruitLocationFirst())).getName());
                employmentInformation1.getEmploymentInformationRecruitLocationSecondName().add( Objects.requireNonNull(CityEnum.getById(employmentInformation.getEmploymentInformationRecruitLocationSecond())).getName());
                employmentInformation1.getEmploymentInformationRecruitLocationDetail().add(Objects.requireNonNull(ProvinceEnum.getById(employmentInformation.getEmploymentInformationRecruitLocationFirst())).getName()+
                        Objects.requireNonNull(CityEnum.getById(employmentInformation.getEmploymentInformationRecruitLocationSecond())).getName()
                );
                map.put(employmentInformation.getEmploymentInformationCode(),employmentInformation1);
            }else{
                // 第一次出现
            EmploymentInformationInfoVO employmentInformationInfoVO = BeanUtil.copyProperties(employmentInformation, EmploymentInformationInfoVO.class);
            List<String>  provinces = new LinkedList<>();
            List<String>  cities = new LinkedList<>();
            List<String>  details = new LinkedList<>();
            provinces.add(Objects.requireNonNull(ProvinceEnum.getById(employmentInformation.getEmploymentInformationRecruitLocationFirst())).getName());
            cities.add( Objects.requireNonNull(CityEnum.getById(employmentInformation.getEmploymentInformationRecruitLocationSecond())).getName());
            details.add(Objects.requireNonNull(ProvinceEnum.getById(employmentInformation.getEmploymentInformationRecruitLocationFirst())).getName()+
                            Objects.requireNonNull(CityEnum.getById(employmentInformation.getEmploymentInformationRecruitLocationSecond())).getName()
                    );
            employmentInformationInfoVO.setEmploymentInformationRecruitLocationFirstName(provinces);
            employmentInformationInfoVO.setEmploymentInformationRecruitLocationSecondName(cities);
            employmentInformationInfoVO.setEmploymentInformationRecruitLocationDetail(details);
            map.put(employmentInformation.getEmploymentInformationCode(),employmentInformationInfoVO);
            }
        }
        return new ArrayList<>(map.values());
    }
}
