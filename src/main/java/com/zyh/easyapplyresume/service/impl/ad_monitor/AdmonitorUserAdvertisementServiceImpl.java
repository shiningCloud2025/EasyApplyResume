package com.zyh.easyapplyresume.service.impl.ad_monitor;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorAdminAdvertisementMapper;
import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorUserAdvertisementMapper;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorUserAdvertisementForm;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminAdvertisement;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorUserAdvertisement;
import com.zyh.easyapplyresume.model.query.ad_monitor.AdmonitorUserAdvertisementQuery;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdminAdvertisementInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdminAdvertisementPageVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorUserAdvertisementInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorUserAdvertisementPageVO;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorUserAdvertisementService;
import com.zyh.easyapplyresume.utils.admonitorvalidator.AdmonitorAdminAdvertisementValidator;
import com.zyh.easyapplyresume.utils.admonitorvalidator.AdmonitorUserAdvertisementValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
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
    @Override
    public Integer addAdmonitorUserAdvertisement(AdmonitorUserAdvertisementForm admonitorUserAdvertisementForm) {
        try{
            log.info("添加广告开始");
            AdmonitorUserAdvertisementValidator.validateForAdd(admonitorUserAdvertisementForm);
            AdmonitorUserAdvertisement admonitorUserAdvertisement = new AdmonitorUserAdvertisement();
            BeanUtil.copyProperties(admonitorUserAdvertisementForm,admonitorUserAdvertisement);
            log.info("添加广告成功");
            return admonitorUserAdvertisementMapper.insert(admonitorUserAdvertisement);
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
            AdmonitorUserAdvertisement admonitorUserAdvertisement = new AdmonitorUserAdvertisement();
            BeanUtil.copyProperties(admonitorUserAdvertisementForm,admonitorUserAdvertisement);
            log.info("修改广告成功");
            return admonitorUserAdvertisementMapper.updateById(admonitorUserAdvertisement);
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
            log.info("删除广告成功");
            return admonitorUserAdvertisementMapper.updateById(admonitorUserAdvertisement);
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
            log.info("查询广告开始");
            LambdaQueryWrapper<AdmonitorUserAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdmonitorUserAdvertisement::getAdvertisementId,id);
            log.info("查询广告成功");
            return BeanUtil.copyProperties(admonitorUserAdvertisementMapper.selectOne(lambdaQueryWrapper), AdmonitorUserAdvertisementInfoVO.class);
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
            log.info("分页查询广告开始");
            Page<AdmonitorUserAdvertisement> page = new Page<>(pageNum,pageSize);
            LambdaQueryWrapper<AdmonitorUserAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            if (admonitorUserAdvertisementQuery != null){
                if (admonitorUserAdvertisementQuery.getAdvertisementName() != null&& admonitorUserAdvertisementQuery.getAdvertisementName().isEmpty()){
                    lambdaQueryWrapper.like(AdmonitorUserAdvertisement::getAdvertisementName,admonitorUserAdvertisementQuery.getAdvertisementName());
                }
            }
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
            log.info("查询所有广告开始");
            List<AdmonitorUserAdvertisement> admonitorUserAdvertisements = admonitorUserAdvertisementMapper.selectList(null);
            log.info("查询所有广告成功");
            return BeanUtil.copyToList(admonitorUserAdvertisements, AdmonitorUserAdvertisementInfoVO.class);
        }catch (Exception e){
            log.info("查询所有广告失败");
            throw new RuntimeException("查询所有广告失败");
        }
    }
}
