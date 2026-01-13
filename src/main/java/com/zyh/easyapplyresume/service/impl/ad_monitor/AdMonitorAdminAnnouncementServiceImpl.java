package com.zyh.easyapplyresume.service.impl.ad_monitor;

import cn.hutool.core.bean.BeanUtil;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdMonitorAdminAnnouncementMapper;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdMonitorAdminAnnouncementForm;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdMonitorAdminAnnouncement;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdMonitorAdminAnnouncementInfoVO;
import com.zyh.easyapplyresume.redis.constant.admonitor.AdMonitorAdminAnnouncementCacheKey;
import com.zyh.easyapplyresume.redis.enums.CacheOperationType;
import com.zyh.easyapplyresume.redis.util.CacheInvalidatePublisher;
import com.zyh.easyapplyresume.redis.util.RedisCacheUtil;
import com.zyh.easyapplyresume.service.ad_monitor.AdMonitorAdminAnnouncementService;
import com.zyh.easyapplyresume.utils.admonitorvalidator.AdMonitorAdminAnnouncementValidator;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AdMonitorAdminAnnouncementServiceImpl implements AdMonitorAdminAnnouncementService {
    @Autowired
    AdMonitorAdminAnnouncementMapper adminAnnouncementMapper;
    @Autowired
    private RedisCacheUtil redisCacheUtil;
    @Autowired
    private CacheInvalidatePublisher cachePublisher;
    @Override
    public Integer addAnnouncement(AdMonitorAdminAnnouncementForm adminAnnouncementForm) {
        try{
            log.info("管理员添加公告");
            AdMonitorAdminAnnouncementValidator.validateForAdd(adminAnnouncementForm);
            List<AdMonitorAdminAnnouncement> adminAnnouncements = adminAnnouncementMapper.selectList(null);
            if (adminAnnouncements.size() > 0) {
                log.error("管理员已添加过公告");
                throw new BusException(AdminCodeEnum.ADMIN_ALREADY_ADD_ANNOUNCEMENT);
            }
            AdMonitorAdminAnnouncement adminAnnouncement = new  AdMonitorAdminAnnouncement();
            BeanUtil.copyProperties(adminAnnouncementForm, adminAnnouncement);
            adminAnnouncement.setAnnouncementUpdatedTime(new Date());
            int result = adminAnnouncementMapper.insert(adminAnnouncement);
            
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        cachePublisher.publishInvalidate(
                            AdMonitorAdminAnnouncementCacheKey.GET,
                            CacheOperationType.ADD
                        );
                    }
                }
            );
            
            log.info("管理员添加公告成功");
            return result;
        }catch (BusException e){
            throw e;
        } catch (Exception e){
            log.error("管理员添加公告失败");
            throw new BusException(AdminCodeEnum.ADMIN_ADD_ANNOUNCEMENT_FAIL);
        }
    }

    @Override
    public Integer updateAnnouncement(AdMonitorAdminAnnouncementForm adminAnnouncementForm) {
        try{
            log.info("管理员修改公告");
            AdMonitorAdminAnnouncementValidator.validateForUpdate(adminAnnouncementForm);
            AdMonitorAdminAnnouncement adminAnnouncement = new AdMonitorAdminAnnouncement();
            BeanUtil.copyProperties(adminAnnouncementForm, adminAnnouncement);
            adminAnnouncement.setAnnouncementUpdatedTime(new Date());
            int result = adminAnnouncementMapper.updateById(adminAnnouncement);
            
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        cachePublisher.publishInvalidate(
                            AdMonitorAdminAnnouncementCacheKey.GET,
                            CacheOperationType.UPDATE
                        );
                    }
                }
            );
            
            log.info("管理员修改公告成功");
            return result;
        }catch (BusException e){
            throw e;
        } catch (Exception e){
            log.error("管理员修改公告失败");
            throw new BusException(AdminCodeEnum.ADMIN_UPDATE_ANNOUNCEMENT_FAIL);
        }
    }

    @Override
    public AdMonitorAdminAnnouncementInfoVO getAnnouncementInfo() {
        try{
            Object cached = redisCacheUtil.get(AdMonitorAdminAnnouncementCacheKey.GET);
            if (cached != null) {
                log.info("从缓存获取管理员公告信息成功");
                return (AdMonitorAdminAnnouncementInfoVO) cached;
            }
            
            log.info("管理员获取公告信息");
            AdMonitorAdminAnnouncement adminAnnouncement = adminAnnouncementMapper.selectById(1);
            AdMonitorAdminAnnouncementInfoVO adminAnnouncementInfoVO = new AdMonitorAdminAnnouncementInfoVO();
            BeanUtil.copyProperties(adminAnnouncement, adminAnnouncementInfoVO);
            
            redisCacheUtil.set(AdMonitorAdminAnnouncementCacheKey.GET, adminAnnouncementInfoVO, AdMonitorAdminAnnouncementCacheKey.GET_TTL, TimeUnit.MINUTES);
            
            log.info("管理员获取公告信息成功");
            return adminAnnouncementInfoVO;
        }catch (BusException e){
            throw e;
        } catch (Exception e){
            log.error("管理员获取公告信息失败");
            throw new BusException(AdminCodeEnum.ADMIN_GET_ANNOUNCEMENT_INFO_FAIL);
        }
    }
}
