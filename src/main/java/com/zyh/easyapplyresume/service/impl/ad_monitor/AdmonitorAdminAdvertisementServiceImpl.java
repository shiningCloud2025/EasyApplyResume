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
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminAdvertisementService;
import com.zyh.easyapplyresume.utils.admonitorvalidator.AdmonitorAdminAdvertisementValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@Slf4j
public class AdmonitorAdminAdvertisementServiceImpl implements AdmonitorAdminAdvertisementService {

    @Autowired
    private AdmonitorAdminAdvertisementMapper admonitorAdminAdvertisementMapper;

    @Override
    public Integer addAdmonitorAdminAdvertisement(AdmonitorAdminAdvertisementForm admonitorAdminAdvertisementForm) {
        try{
            log.info("添加广告开始");
            AdmonitorAdminAdvertisementValidator.validateForAdd(admonitorAdminAdvertisementForm);
            AdmonitorAdminAdvertisement admonitorAdminAdvertisement = new AdmonitorAdminAdvertisement();
            BeanUtil.copyProperties(admonitorAdminAdvertisementForm,admonitorAdminAdvertisement);
            log.info("添加广告成功");
            return admonitorAdminAdvertisementMapper.insert(admonitorAdminAdvertisement);
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
            AdmonitorAdminAdvertisement admonitorAdminAdvertisement = new AdmonitorAdminAdvertisement();
            BeanUtil.copyProperties(admonitorAdminAdvertisementForm,admonitorAdminAdvertisement);
            log.info("修改广告成功");
            return admonitorAdminAdvertisementMapper.updateById(admonitorAdminAdvertisement);
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
            log.info("删除广告成功");
            return admonitorAdminAdvertisementMapper.updateById(admonitorAdminAdvertisement);
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
            log.info("查询广告开始");
            LambdaQueryWrapper<AdmonitorAdminAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdmonitorAdminAdvertisement::getAdvertisementId, id);
            log.info("查询广告成功");
            return BeanUtil.copyProperties(admonitorAdminAdvertisementMapper.selectOne(lambdaQueryWrapper), AdmonitorAdminAdvertisementInfoVO.class);
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
            log.info("分页查询广告开始");
            Page<AdmonitorAdminAdvertisement> page = new Page<>(pageNum,pageSize);
            LambdaQueryWrapper<AdmonitorAdminAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            if (admonitorAdminAdvertisementQuery != null){
                if (admonitorAdminAdvertisementQuery.getAdvertisementName() != null&& admonitorAdminAdvertisementQuery.getAdvertisementName().isEmpty()){
                    lambdaQueryWrapper.like(AdmonitorAdminAdvertisement::getAdvertisementName,admonitorAdminAdvertisementQuery.getAdvertisementName());
                }
            }
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
           log.info("查询所有广告开始");
           LambdaQueryWrapper<AdmonitorAdminAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
           lambdaQueryWrapper.eq(AdmonitorAdminAdvertisement::getDeleted,0);
           lambdaQueryWrapper.ge(AdmonitorAdminAdvertisement::getAdvertisementEndTime, new Date());
           List<AdmonitorAdminAdvertisement> admonitorAdminAdvertisements = admonitorAdminAdvertisementMapper.selectList(null);

           log.info("查询所有广告成功");
           return BeanUtil.copyToList(admonitorAdminAdvertisements, AdmonitorAdminAdvertisementInfoVO.class);
       }catch (Exception e){
           log.info("查询所有广告失败");
           throw new RuntimeException("查询所有广告失败");
       }
    }
}
