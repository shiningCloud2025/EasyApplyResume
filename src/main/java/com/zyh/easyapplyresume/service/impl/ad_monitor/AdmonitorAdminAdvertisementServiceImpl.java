package com.zyh.easyapplyresume.service.impl.ad_monitor;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorAdminAdvertisementMapper;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorAdminAdvertisementForm;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminAdvertisement;
import com.zyh.easyapplyresume.model.query.ad_monitor.AdmonitorAdminAdvertisementQuery;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdminAdvertisementInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdminAdvertisementPageVO;
import com.zyh.easyapplyresume.qiniuoss.OssAdMonitorBusinessTypeEnum;
import com.zyh.easyapplyresume.qiniuoss.OssService;
import com.zyh.easyapplyresume.qiniuoss.OssSystemTypeEnum;
import com.zyh.easyapplyresume.redis.constant.admonitor.AdmonitorAdminAdvertisementCacheKey;
import com.zyh.easyapplyresume.redis.enums.CacheOperationType;
import com.zyh.easyapplyresume.redis.util.CacheInvalidatePublisher;
import com.zyh.easyapplyresume.redis.util.RedisCacheUtil;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminAdvertisementService;
import com.zyh.easyapplyresume.utils.admonitorvalidator.AdmonitorAdminAdvertisementValidator;
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
public class AdmonitorAdminAdvertisementServiceImpl implements AdmonitorAdminAdvertisementService {

    @Autowired
    private AdmonitorAdminAdvertisementMapper admonitorAdminAdvertisementMapper;

    @Autowired
    private OssService ossService;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Autowired
    private CacheInvalidatePublisher cachePublisher;

    @Override
    public Integer addAdmonitorAdminAdvertisement(AdmonitorAdminAdvertisementForm admonitorAdminAdvertisementForm) {
        try{
            log.info("添加广告开始");
            AdmonitorAdminAdvertisementValidator.validateForAdd(admonitorAdminAdvertisementForm);
            List<String> strings = ossService.listFilesByOwner(OssSystemTypeEnum.AD_MONITOR, OssAdMonitorBusinessTypeEnum.ADMONITOR_ADMIN_AD_IMG, 0, false);
            AdmonitorAdminAdvertisement admonitorAdminAdvertisement = new AdmonitorAdminAdvertisement();
            BeanUtil.copyProperties(admonitorAdminAdvertisementForm,admonitorAdminAdvertisement);
            int result = admonitorAdminAdvertisementMapper.insert(admonitorAdminAdvertisement);
            
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        cachePublisher.publishInvalidate(
                            AdmonitorAdminAdvertisementCacheKey.ALL_PATTERN,
                            CacheOperationType.ADD
                        );
                    }
                }
            );
            
            log.info("添加广告成功");
            return result;
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.info("添加广告失败");
            throw new RuntimeException("添加广告失败");
        }

    }

    @Override
    public Integer updateAdmonitorAdminAdvertisement(AdmonitorAdminAdvertisementForm admonitorAdminAdvertisementForm) {
        try{
            log.info("修改广告开始");
            AdmonitorAdminAdvertisementValidator.validateForUpdate(admonitorAdminAdvertisementForm);
            // TODO:这个和用户、管理员的头像逻辑还不一样，这个是公有库，要去数据库查正在用的，把不用的干掉
            List<String> strings = ossService.listFilesByOwner(OssSystemTypeEnum.AD_MONITOR, OssAdMonitorBusinessTypeEnum.ADMONITOR_ADMIN_AD_IMG, 0, false);
            if (strings.isEmpty()){
                // 如果为空，说明这是第一个广告，直接扔里面就可以
            }else{
                // 不为空，除了当前的URL，其他的URL如果数据库中没有，那就干掉
                List<String> admonitorAdvertisementUrls = admonitorAdminAdvertisementMapper.selectList(null).stream()
                        .map(admonitorAdminAdvertisement -> admonitorAdminAdvertisement.getAdvertisementUrl())
                        .toList();
                for (String str:strings){
                    if (!str.equals(admonitorAdminAdvertisementForm.getAdvertisementUrl())){
                        // 也就是说 除了当前的这个以外，其他的 都要在数据库里能找到不然就干掉
                        if (!admonitorAdvertisementUrls.contains(str)){
                            ossService.deleteByUrl(str,false);
                        }
                    }
                }
            }

            AdmonitorAdminAdvertisement admonitorAdminAdvertisement = new AdmonitorAdminAdvertisement();
            BeanUtil.copyProperties(admonitorAdminAdvertisementForm,admonitorAdminAdvertisement);
            int result = admonitorAdminAdvertisementMapper.updateById(admonitorAdminAdvertisement);
            
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        cachePublisher.publishInvalidate(
                            AdmonitorAdminAdvertisementCacheKey.ALL_PATTERN,
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
    public Integer deleteAdmonitorAdminAdvertisement(Integer id) {
        try{
            log.info("删除广告开始");
            LambdaQueryWrapper<AdmonitorAdminAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdmonitorAdminAdvertisement::getAdvertisementId,id);
            AdmonitorAdminAdvertisement admonitorAdminAdvertisement = admonitorAdminAdvertisementMapper.selectOne(lambdaQueryWrapper);
            admonitorAdminAdvertisement.setDeleted(1);
            ossService.deleteByUrl(admonitorAdminAdvertisement.getAdvertisementUrl(),false);
            int result = admonitorAdminAdvertisementMapper.updateById(admonitorAdminAdvertisement);
            
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        cachePublisher.publishInvalidate(
                            AdmonitorAdminAdvertisementCacheKey.ALL_PATTERN,
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
    public AdmonitorAdminAdvertisementInfoVO findAdmonitorAdminAdvertisementById(Integer id) {
        try {
            String cacheKey = AdmonitorAdminAdvertisementCacheKey.GET_PREFIX + "_" + id;
            
            Object cached = redisCacheUtil.get(cacheKey);
            if (cached != null) {
                log.info("从缓存查询广告成功");
                return (AdmonitorAdminAdvertisementInfoVO) cached;
            }
            
            log.info("查询广告开始");
            LambdaQueryWrapper<AdmonitorAdminAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdmonitorAdminAdvertisement::getAdvertisementId, id);
            AdmonitorAdminAdvertisementInfoVO result = BeanUtil.copyProperties(admonitorAdminAdvertisementMapper.selectOne(lambdaQueryWrapper), AdmonitorAdminAdvertisementInfoVO.class);
            
            redisCacheUtil.set(cacheKey, result, AdmonitorAdminAdvertisementCacheKey.GET_TTL, TimeUnit.MINUTES);
            
            log.info("查询广告成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.info("查询广告失败");
            throw new RuntimeException("查询广告失败");
        }
    }

    @Override
    public Page<AdmonitorAdminAdvertisementPageVO> findAdmonitorAdminAdvertisementByPage(Integer pageNum, Integer pageSize, AdmonitorAdminAdvertisementQuery admonitorAdminAdvertisementQuery) {
        try{
            String cacheKey = AdmonitorAdminAdvertisementCacheKey.PAGE_PREFIX 
                            + "_" + pageNum 
                            + "_" + pageSize 
                            + "_" + (admonitorAdminAdvertisementQuery != null ? admonitorAdminAdvertisementQuery.hashCode() : 0);
            
            Object cached = redisCacheUtil.get(cacheKey);
            if (cached != null) {
                log.info("从缓存分页查询广告成功");
                return (Page<AdmonitorAdminAdvertisementPageVO>) cached;
            }
            
            log.info("分页查询广告开始");
            Page<AdmonitorAdminAdvertisement> page = new Page<>(pageNum,pageSize);
            LambdaQueryWrapper<AdmonitorAdminAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            if (admonitorAdminAdvertisementQuery != null){
                if (admonitorAdminAdvertisementQuery.getAdvertisementName() != null&& admonitorAdminAdvertisementQuery.getAdvertisementName().isEmpty()){
                    lambdaQueryWrapper.like(AdmonitorAdminAdvertisement::getAdvertisementName,admonitorAdminAdvertisementQuery.getAdvertisementName());
                }
            }
            lambdaQueryWrapper.eq(AdmonitorAdminAdvertisement::getDeleted,0);
            Page<AdmonitorAdminAdvertisement> admonitorAdminAdvertisementPage = admonitorAdminAdvertisementMapper.selectPage(page, lambdaQueryWrapper);
            List<AdmonitorAdminAdvertisementPageVO> voList = admonitorAdminAdvertisementPage.getRecords().stream()
                    .map(vo -> {
                        AdmonitorAdminAdvertisementPageVO admonitorAdminAdvertisementPageVO = new AdmonitorAdminAdvertisementPageVO();
                        BeanUtil.copyProperties(vo,admonitorAdminAdvertisementPageVO);
                        return admonitorAdminAdvertisementPageVO;
                    })
                    .collect(Collectors.toList());

            Page<AdmonitorAdminAdvertisementPageVO> resultPage = new Page<>();
            resultPage.setCurrent(admonitorAdminAdvertisementPage.getCurrent());
            resultPage.setSize(admonitorAdminAdvertisementPage.getSize());
            resultPage.setTotal(admonitorAdminAdvertisementPage.getTotal());
            resultPage.setPages(admonitorAdminAdvertisementPage.getPages());
            resultPage.setRecords(voList != null ? voList : Collections.emptyList());
            
            redisCacheUtil.set(cacheKey, resultPage, AdmonitorAdminAdvertisementCacheKey.PAGE_TTL, TimeUnit.MINUTES);
            
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
    public List<AdmonitorAdminAdvertisementInfoVO> findAllAdmonitorAdminAdvertisement() {
       try{
           Object cached = redisCacheUtil.get(AdmonitorAdminAdvertisementCacheKey.LIST);
           if (cached != null) {
               log.info("从缓存查询所有广告成功");
               return (List<AdmonitorAdminAdvertisementInfoVO>) cached;
           }
           
           log.info("查询所有广告开始");
           Date today = new Date();
           LambdaQueryWrapper<AdmonitorAdminAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
           lambdaQueryWrapper.eq(AdmonitorAdminAdvertisement::getDeleted, 0);
           // 开始时间 ≤ 今天（今天或之前创建）
           lambdaQueryWrapper.le(AdmonitorAdminAdvertisement::getAdvertisementStartedTime, today);
           // 结束时间 > 今天（明天或以后结束）
           lambdaQueryWrapper.gt(AdmonitorAdminAdvertisement::getAdvertisementEndTime, today);
           List<AdmonitorAdminAdvertisement> admonitorAdminAdvertisements = admonitorAdminAdvertisementMapper.selectList(lambdaQueryWrapper);
           List<AdmonitorAdminAdvertisementInfoVO> result = BeanUtil.copyToList(admonitorAdminAdvertisements, AdmonitorAdminAdvertisementInfoVO.class);
           
           redisCacheUtil.set(AdmonitorAdminAdvertisementCacheKey.LIST, result, AdmonitorAdminAdvertisementCacheKey.LIST_TTL, TimeUnit.MINUTES);

           log.info("查询所有广告成功");
           return result;
       }catch (Exception e){
           log.info("查询所有广告失败");
           throw new RuntimeException("查询所有广告失败");
       }
    }
}
