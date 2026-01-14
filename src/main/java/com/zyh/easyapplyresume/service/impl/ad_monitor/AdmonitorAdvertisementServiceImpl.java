package com.zyh.easyapplyresume.service.impl.ad_monitor;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorAdvertisementMapper;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorAdvertisementForm;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminAdvertisement;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdvertisement;
import com.zyh.easyapplyresume.model.query.ad_monitor.AdmonitorAdvertisementQuery;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdminAdvertisementInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdminAdvertisementPageVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdvertisementInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdvertisementPageVO;
import com.zyh.easyapplyresume.qiniuoss.OssAdMonitorBusinessTypeEnum;
import com.zyh.easyapplyresume.qiniuoss.OssService;
import com.zyh.easyapplyresume.qiniuoss.OssSystemTypeEnum;
import com.zyh.easyapplyresume.redis.constant.admonitor.AdmonitorAdvertisementCacheKey;
import com.zyh.easyapplyresume.redis.enums.CacheOperationType;
import com.zyh.easyapplyresume.redis.util.CacheInvalidatePublisher;
import com.zyh.easyapplyresume.redis.util.RedisCacheUtil;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdvertisementService;
import com.zyh.easyapplyresume.utils.admonitorvalidator.AdmonitorAdvertisementValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdmonitorAdvertisementServiceImpl implements AdmonitorAdvertisementService {

    @Autowired
    private AdmonitorAdvertisementMapper admonitorAdvertisementMapper;

    @Autowired
    private OssService ossService;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Autowired
    private CacheInvalidatePublisher cachePublisher;

    @Override
    public Integer addAdmonitorAdvertisement(AdmonitorAdvertisementForm admonitorAdvertisementForm) {
        try{
            log.info("添加广告开始");
            AdmonitorAdvertisementValidator.validateForAdd(admonitorAdvertisementForm);
            AdmonitorAdvertisement admonitorAdvertisement = new AdmonitorAdvertisement();
            BeanUtil.copyProperties(admonitorAdvertisementForm,admonitorAdvertisement);
            int result = admonitorAdvertisementMapper.insert(admonitorAdvertisement);
            
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        cachePublisher.publishInvalidate(
                            AdmonitorAdvertisementCacheKey.ALL_PATTERN,
                            CacheOperationType.ADD
                        );
                    }
                }
            );
            
            return result;
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.info("添加广告异常");
            throw new RuntimeException("添加广告异常");
        }

    }

    @Override
    public Integer updateAdmonitorAdvertisement(AdmonitorAdvertisementForm admonitorAdvertisementForm) {
        try{
            log.info("修改广告开始");
            AdmonitorAdvertisementValidator.validateForUpdate(admonitorAdvertisementForm);
            // TODO:这个和用户、管理员的头像逻辑还不一样，这个是公有库，要去数据库查正在用的，把不用的干掉
            List<String> strings = ossService.listFilesByOwner(OssSystemTypeEnum.AD_MONITOR, OssAdMonitorBusinessTypeEnum.ADMONITOR_ADMONITOR_AD_IMG, 0, false);
            if (strings.isEmpty()){
                // 如果为空，说明这是第一个广告，直接扔里面就可以
            }else{
                // 不为空，除了当前的URL，其他的URL如果数据库中没有，那就干掉
                List<String> admonitorAdvertisementUrls = admonitorAdvertisementMapper.selectList(null).stream()
                        .map(admonitorAdvertisement -> admonitorAdvertisement.getAdvertisementUrl())
                        .toList();
                for (String str:strings){
                    if (!str.equals(admonitorAdvertisementForm.getAdvertisementUrl())){
                        // 也就是说 除了当前的这个以外，其他的 都要在数据库里能找到不然就干掉
                        if (!admonitorAdvertisementUrls.contains(str)){
                            ossService.deleteByUrl(str,false);
                        }
                    }
                }
            }
            AdmonitorAdvertisement admonitorAdvertisement = new AdmonitorAdvertisement();
            BeanUtil.copyProperties(admonitorAdvertisementForm,admonitorAdvertisement);
            int result = admonitorAdvertisementMapper.updateById(admonitorAdvertisement);
            
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        cachePublisher.publishInvalidate(
                            AdmonitorAdvertisementCacheKey.ALL_PATTERN,
                            CacheOperationType.UPDATE
                        );
                    }
                }
            );
            
            log.info("修改广告成功");
            return result;
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.info("修改广告失败");
            throw new RuntimeException("修改广告失败");
        }

    }

    @Override
    public Integer deleteAdmonitorAdvertisement(Integer id) {
        try{
            log.info("删除广告开始");
            LambdaQueryWrapper<AdmonitorAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdmonitorAdvertisement::getAdvertisementId,id);
            AdmonitorAdvertisement admonitorAdvertisement = admonitorAdvertisementMapper.selectOne(lambdaQueryWrapper);
            admonitorAdvertisement.setDeleted(1);
            ossService.deleteByUrl(admonitorAdvertisement.getAdvertisementUrl(),false);
            int result = admonitorAdvertisementMapper.updateById(admonitorAdvertisement);
            
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        cachePublisher.publishInvalidate(
                            AdmonitorAdvertisementCacheKey.ALL_PATTERN,
                            CacheOperationType.DELETE
                        );
                    }
                }
            );
            
            log.info("删除广告成功");
            return result;
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.info("删除广告失败");
            throw new RuntimeException("删除广告失败");
        }
    }

    @Override
    public AdmonitorAdvertisementInfoVO findAdmonitorAdvertisementById(Integer id) {
        try{
            String cacheKey = AdmonitorAdvertisementCacheKey.GET_PREFIX + "_" + id;
            
            Object cached = redisCacheUtil.get(cacheKey);
            if (cached != null) {
                log.info("从缓存查询广告成功");
                return (AdmonitorAdvertisementInfoVO) cached;
            }
            
            log.info("查询广告开始");
            LambdaQueryWrapper<AdmonitorAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdmonitorAdvertisement::getAdvertisementId,id);
            AdmonitorAdvertisementInfoVO result = BeanUtil.copyProperties(admonitorAdvertisementMapper.selectOne(lambdaQueryWrapper), AdmonitorAdvertisementInfoVO.class);
            
            redisCacheUtil.set(cacheKey, result, AdmonitorAdvertisementCacheKey.GET_TTL, TimeUnit.MINUTES);
            
            log.info("查询广告成功");
            return result;
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.info("查询广告失败");
            throw new RuntimeException("查询广告失败");
        }
    }

    @Override
    public Page<AdmonitorAdvertisementPageVO> findAdmonitorAdvertisementByPage(Integer pageNum, Integer pageSize, AdmonitorAdvertisementQuery admonitorAdvertisementQuery) {
        try{
            String cacheKey = AdmonitorAdvertisementCacheKey.PAGE_PREFIX 
                            + "_" + pageNum 
                            + "_" + pageSize 
                            + "_" + (admonitorAdvertisementQuery != null ? admonitorAdvertisementQuery.hashCode() : 0);
            
            Object cached = redisCacheUtil.get(cacheKey);
            if (cached != null) {
                log.info("从缓存分页查询广告成功");
                return (Page<AdmonitorAdvertisementPageVO>) cached;
            }
            
            log.info("分页查询广告开始");
            Page<AdmonitorAdvertisement> page = new Page<>(pageNum,pageSize);
            LambdaQueryWrapper<AdmonitorAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            if (admonitorAdvertisementQuery != null){
                if (admonitorAdvertisementQuery.getAdvertisementName() != null&& admonitorAdvertisementQuery.getAdvertisementName().isEmpty()){
                    lambdaQueryWrapper.like(AdmonitorAdvertisement::getAdvertisementName,admonitorAdvertisementQuery.getAdvertisementName());
                }
            }
            lambdaQueryWrapper.eq(AdmonitorAdvertisement::getDeleted, 0);
            Page<AdmonitorAdvertisement> admonitorAdvertisementPage = admonitorAdvertisementMapper.selectPage(page, lambdaQueryWrapper);
            List<AdmonitorAdvertisementPageVO> voList = admonitorAdvertisementPage.getRecords().stream()
                    .map(vo -> {
                        AdmonitorAdvertisementPageVO admonitorAdvertisementPageVO = new AdmonitorAdvertisementPageVO();
                        BeanUtil.copyProperties(vo,admonitorAdvertisementPageVO);
                        return admonitorAdvertisementPageVO;
                    })
                    .collect(Collectors.toList());

            Page<AdmonitorAdvertisementPageVO> resultPage = new Page<>();
            resultPage.setCurrent(admonitorAdvertisementPage.getCurrent());
            resultPage.setSize(admonitorAdvertisementPage.getSize());
            resultPage.setTotal(admonitorAdvertisementPage.getTotal());
            resultPage.setPages(admonitorAdvertisementPage.getPages());
            resultPage.setRecords(voList != null ? voList : Collections.emptyList());
            
            redisCacheUtil.set(cacheKey, resultPage, AdmonitorAdvertisementCacheKey.PAGE_TTL, TimeUnit.MINUTES);
            
            log.info("分页查询广告成功");
            return resultPage;
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.info("分页查询广告失败");
            throw new RuntimeException("分页查询广告失败");
        }
    }

    @Override
    public List<AdmonitorAdvertisementInfoVO> findAllAdmonitorAdminAdvertisement() {
        try{
            Object cached = redisCacheUtil.get(AdmonitorAdvertisementCacheKey.LIST);
            if (cached != null) {
                log.info("从缓存查询所有广告成功");
                return (List<AdmonitorAdvertisementInfoVO>) cached;
            }
            
            log.info("查询所有广告开始");
            Date today = new Date();
            LambdaQueryWrapper<AdmonitorAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdmonitorAdvertisement::getDeleted, 0);
            // 开始时间 ≤ 今天（今天或之前创建）
            lambdaQueryWrapper.le(AdmonitorAdvertisement::getAdvertisementStartedTime, today);
            // 结束时间 > 今天（明天或以后结束）
            lambdaQueryWrapper.gt(AdmonitorAdvertisement::getAdvertisementEndTime, today);
            List<AdmonitorAdvertisement> admonitorAdvertisements = admonitorAdvertisementMapper.selectList(lambdaQueryWrapper);
            List<AdmonitorAdvertisementInfoVO> result = BeanUtil.copyToList(admonitorAdvertisements, AdmonitorAdvertisementInfoVO.class);
            
            redisCacheUtil.set(AdmonitorAdvertisementCacheKey.LIST, result, AdmonitorAdvertisementCacheKey.LIST_TTL, TimeUnit.MINUTES);
            
            log.info("查询所有广告成功");
            return result;
        }catch (Exception e){
            log.info("查询所有广告失败");
            throw new RuntimeException("查询所有广告失败");
        }
    }
}
