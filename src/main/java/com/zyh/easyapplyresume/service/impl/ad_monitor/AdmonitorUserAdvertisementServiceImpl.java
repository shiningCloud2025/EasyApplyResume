package com.zyh.easyapplyresume.service.impl.ad_monitor;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorAdminAdvertisementMapper;
import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorUserAdvertisementMapper;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorUserAdvertisementForm;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminAdvertisement;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdvertisement;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorUserAdvertisement;
import com.zyh.easyapplyresume.model.query.ad_monitor.AdmonitorUserAdvertisementQuery;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdminAdvertisementInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdminAdvertisementPageVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorUserAdvertisementInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorUserAdvertisementPageVO;
import com.zyh.easyapplyresume.qiniuoss.OssAdMonitorBusinessTypeEnum;
import com.zyh.easyapplyresume.qiniuoss.OssService;
import com.zyh.easyapplyresume.qiniuoss.OssSystemTypeEnum;
import com.zyh.easyapplyresume.redis.constant.admonitor.AdmonitorUserAdvertisementCacheKey;
import com.zyh.easyapplyresume.redis.enums.CacheOperationType;
import com.zyh.easyapplyresume.redis.util.CacheInvalidatePublisher;
import com.zyh.easyapplyresume.redis.util.RedisCacheUtil;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorUserAdvertisementService;
import com.zyh.easyapplyresume.utils.admonitorvalidator.AdmonitorAdminAdvertisementValidator;
import com.zyh.easyapplyresume.utils.admonitorvalidator.AdmonitorUserAdvertisementValidator;
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
@Slf4j
public class AdmonitorUserAdvertisementServiceImpl implements AdmonitorUserAdvertisementService {

    @Autowired
    private AdmonitorUserAdvertisementMapper admonitorUserAdvertisementMapper;

    @Autowired
    private OssService ossService;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Autowired
    private CacheInvalidatePublisher cachePublisher;
    @Override
    public Integer addAdmonitorUserAdvertisement(AdmonitorUserAdvertisementForm admonitorUserAdvertisementForm) {
        try{
            log.info("添加广告开始");
            AdmonitorUserAdvertisementValidator.validateForAdd(admonitorUserAdvertisementForm);
            AdmonitorUserAdvertisement admonitorUserAdvertisement = new AdmonitorUserAdvertisement();
            BeanUtil.copyProperties(admonitorUserAdvertisementForm,admonitorUserAdvertisement);
            int result = admonitorUserAdvertisementMapper.insert(admonitorUserAdvertisement);
            
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        cachePublisher.publishInvalidate(
                            AdmonitorUserAdvertisementCacheKey.ALL_PATTERN,
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
    public Integer updateAdmonitorUserAdvertisement(AdmonitorUserAdvertisementForm admonitorUserAdvertisementForm) {
        try{
            log.info("修改广告开始");
            AdmonitorUserAdvertisementValidator.validateForUpdate(admonitorUserAdvertisementForm);

            // TODO:这个和用户、管理员的头像逻辑还不一样，这个是公有库，要去数据库查正在用的，把不用的干掉
            List<String> strings = ossService.listFilesByOwner(OssSystemTypeEnum.AD_MONITOR, OssAdMonitorBusinessTypeEnum.ADMONITOR_USER_AD_IMG, 0, false);
            if (strings.isEmpty()){
                // 如果为空，说明这是第一个广告，直接扔里面就可以
            }else{
                // 不为空，除了当前的URL，其他的URL如果数据库中没有，那就干掉
                List<String> admonitorAdvertisementUrls = admonitorUserAdvertisementMapper.selectList(null).stream()
                        .map(admonitorUserAdvertisement -> admonitorUserAdvertisement.getAdvertisementUrl())
                        .toList();
                for (String str:strings){
                    if (!str.equals(admonitorUserAdvertisementForm.getAdvertisementUrl())){
                        // 也就是说 除了当前的这个以外，其他的 都要在数据库里能找到不然就干掉
                        if (!admonitorAdvertisementUrls.contains(str)){
                            ossService.deleteByUrl(str,false);
                        }
                    }
                }
            }

            AdmonitorUserAdvertisement admonitorUserAdvertisement = new AdmonitorUserAdvertisement();
            BeanUtil.copyProperties(admonitorUserAdvertisementForm,admonitorUserAdvertisement);
            int result = admonitorUserAdvertisementMapper.updateById(admonitorUserAdvertisement);
            
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        cachePublisher.publishInvalidate(
                            AdmonitorUserAdvertisementCacheKey.ALL_PATTERN,
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
    public Integer deleteAdmonitorUserAdvertisement(Integer id) {
        try{
            log.info("删除广告开始");
            LambdaQueryWrapper<AdmonitorUserAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdmonitorUserAdvertisement::getAdvertisementId,id);
            AdmonitorUserAdvertisement admonitorUserAdvertisement = admonitorUserAdvertisementMapper.selectOne(lambdaQueryWrapper);
            admonitorUserAdvertisement.setDeleted(1);
            ossService.deleteByUrl(admonitorUserAdvertisement.getAdvertisementUrl(),false);
            int result = admonitorUserAdvertisementMapper.updateById(admonitorUserAdvertisement);
            
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        cachePublisher.publishInvalidate(
                            AdmonitorUserAdvertisementCacheKey.ALL_PATTERN,
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
    public AdmonitorUserAdvertisementInfoVO findAdmonitorUserAdvertisementById(Integer id) {
        try{
            String cacheKey = AdmonitorUserAdvertisementCacheKey.GET_PREFIX + "_" + id;
            
            Object cached = redisCacheUtil.get(cacheKey);
            if (cached != null) {
                log.info("从缓存查询广告成功");
                return (AdmonitorUserAdvertisementInfoVO) cached;
            }
            
            log.info("查询广告开始");
            LambdaQueryWrapper<AdmonitorUserAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdmonitorUserAdvertisement::getAdvertisementId,id);
            AdmonitorUserAdvertisementInfoVO result = BeanUtil.copyProperties(admonitorUserAdvertisementMapper.selectOne(lambdaQueryWrapper), AdmonitorUserAdvertisementInfoVO.class);
            
            redisCacheUtil.set(cacheKey, result, AdmonitorUserAdvertisementCacheKey.GET_TTL, TimeUnit.MINUTES);
            
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
    public Page<AdmonitorUserAdvertisementPageVO> findAdmonitorUserAdvertisementByPage(Integer pageNum, Integer pageSize, AdmonitorUserAdvertisementQuery admonitorUserAdvertisementQuery) {
        try{
            String cacheKey = AdmonitorUserAdvertisementCacheKey.PAGE_PREFIX 
                            + "_" + pageNum 
                            + "_" + pageSize 
                            + "_" + (admonitorUserAdvertisementQuery != null ? admonitorUserAdvertisementQuery.hashCode() : 0);
            
            Object cached = redisCacheUtil.get(cacheKey);
            if (cached != null) {
                log.info("从缓存分页查询广告成功");
                return (Page<AdmonitorUserAdvertisementPageVO>) cached;
            }
            
            log.info("分页查询广告开始");
            Page<AdmonitorUserAdvertisement> page = new Page<>(pageNum,pageSize);
            LambdaQueryWrapper<AdmonitorUserAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            if (admonitorUserAdvertisementQuery != null){
                if (admonitorUserAdvertisementQuery.getAdvertisementName() != null&& admonitorUserAdvertisementQuery.getAdvertisementName().isEmpty()){
                    lambdaQueryWrapper.like(AdmonitorUserAdvertisement::getAdvertisementName,admonitorUserAdvertisementQuery.getAdvertisementName());
                }
            }
            lambdaQueryWrapper.eq(AdmonitorUserAdvertisement::getDeleted,0);
            Page<AdmonitorUserAdvertisement> admonitorUserAdvertisementPage = admonitorUserAdvertisementMapper.selectPage(page, lambdaQueryWrapper);
            List<AdmonitorUserAdvertisementPageVO> voList = admonitorUserAdvertisementPage.getRecords().stream()
                    .map(vo -> {
                        AdmonitorUserAdvertisementPageVO admonitorUserAdvertisementPageVO = new AdmonitorUserAdvertisementPageVO();
                        BeanUtil.copyProperties(vo,admonitorUserAdvertisementPageVO);
                        return admonitorUserAdvertisementPageVO;
                    })
                    .collect(Collectors.toList());

            Page<AdmonitorUserAdvertisementPageVO> resultPage = new Page<>();
            resultPage.setCurrent(admonitorUserAdvertisementPage.getCurrent());
            resultPage.setSize(admonitorUserAdvertisementPage.getSize());
            resultPage.setTotal(admonitorUserAdvertisementPage.getTotal());
            resultPage.setPages(admonitorUserAdvertisementPage.getPages());
            resultPage.setRecords(voList != null ? voList : Collections.emptyList());
            
            redisCacheUtil.set(cacheKey, resultPage, AdmonitorUserAdvertisementCacheKey.PAGE_TTL, TimeUnit.MINUTES);
            
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
    public List<AdmonitorUserAdvertisementInfoVO> findAllAdmonitorUserAdvertisement() {
        try{
            Object cached = redisCacheUtil.get(AdmonitorUserAdvertisementCacheKey.LIST);
            if (cached != null) {
                log.info("从缓存查询所有广告成功");
                return (List<AdmonitorUserAdvertisementInfoVO>) cached;
            }
            
            log.info("查询所有广告开始");
            Date today = new Date();
            LambdaQueryWrapper<AdmonitorUserAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdmonitorUserAdvertisement::getDeleted, 0);
            // 开始时间 ≤ 今天（今天或之前创建）
            lambdaQueryWrapper.le(AdmonitorUserAdvertisement::getAdvertisementStartedTime, today);
            // 结束时间 > 今天（明天或以后结束）
            lambdaQueryWrapper.gt(AdmonitorUserAdvertisement::getAdvertisementEndTime, today);

            List<AdmonitorUserAdvertisement> admonitorUserAdvertisements = admonitorUserAdvertisementMapper.selectList(lambdaQueryWrapper);
            List<AdmonitorUserAdvertisementInfoVO> result = BeanUtil.copyToList(admonitorUserAdvertisements, AdmonitorUserAdvertisementInfoVO.class);
            
            redisCacheUtil.set(AdmonitorUserAdvertisementCacheKey.LIST, result, AdmonitorUserAdvertisementCacheKey.LIST_TTL, TimeUnit.MINUTES);
            
            log.info("查询所有广告成功");
            return result;
        }catch (Exception e){
            log.info("查询所有广告失败");
            throw new RuntimeException("查询所有广告失败");
        }
    }
}
