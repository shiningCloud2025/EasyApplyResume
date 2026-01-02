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
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdvertisementService;
import com.zyh.easyapplyresume.utils.admonitorvalidator.AdmonitorAdvertisementValidator;
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
public class AdmonitorAdvertisementServiceImpl implements AdmonitorAdvertisementService {

    @Autowired
    private AdmonitorAdvertisementMapper admonitorAdvertisementMapper;
    @Override
    public Integer addAdmonitorAdvertisement(AdmonitorAdvertisementForm admonitorAdvertisementForm) {
        try{
            log.info("添加广告开始");
            AdmonitorAdvertisementValidator.validateForAdd(admonitorAdvertisementForm);
            AdmonitorAdvertisement admonitorAdvertisement = new AdmonitorAdvertisement();
            BeanUtil.copyProperties(admonitorAdvertisementForm,admonitorAdvertisement);
            return admonitorAdvertisementMapper.insert(admonitorAdvertisement);
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

            AdmonitorAdvertisement admonitorAdvertisement = new AdmonitorAdvertisement();
            BeanUtil.copyProperties(admonitorAdvertisementForm,admonitorAdvertisement);
            log.info("修改广告成功");
            return admonitorAdvertisementMapper.updateById(admonitorAdvertisement);
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
            log.info("删除广告成功");
            return admonitorAdvertisementMapper.updateById(admonitorAdvertisement);
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
            log.info("查询广告开始");
            LambdaQueryWrapper<AdmonitorAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdmonitorAdvertisement::getAdvertisementId,id);
            log.info("查询广告成功");
            return BeanUtil.copyProperties(admonitorAdvertisementMapper.selectOne(lambdaQueryWrapper), AdmonitorAdvertisementInfoVO.class);
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
            log.info("分页查询广告开始");
            Page<AdmonitorAdvertisement> page = new Page<>(pageNum,pageSize);
            LambdaQueryWrapper<AdmonitorAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            if (admonitorAdvertisementQuery != null){
                if (admonitorAdvertisementQuery.getAdvertisementName() != null&& admonitorAdvertisementQuery.getAdvertisementName().isEmpty()){
                    lambdaQueryWrapper.like(AdmonitorAdvertisement::getAdvertisementName,admonitorAdvertisementQuery.getAdvertisementName());
                }
            }
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
            log.info("查询所有广告开始");
            Date today = new Date();
            LambdaQueryWrapper<AdmonitorAdvertisement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdmonitorAdvertisement::getDeleted, 0);
            // 开始时间 ≤ 今天（今天或之前创建）
            lambdaQueryWrapper.le(AdmonitorAdvertisement::getAdvertisementStartedTime, today);
            // 结束时间 > 今天（明天或以后结束）
            lambdaQueryWrapper.gt(AdmonitorAdvertisement::getAdvertisementEndTime, today);
            List<AdmonitorAdvertisement> admonitorAdvertisements = admonitorAdvertisementMapper.selectList(lambdaQueryWrapper);
            log.info("查询所有广告成功");
            return BeanUtil.copyToList(admonitorAdvertisements, AdmonitorAdvertisementInfoVO.class);
        }catch (Exception e){
            log.info("查询所有广告失败");
            throw new RuntimeException("查询所有广告失败");
        }
    }
}
