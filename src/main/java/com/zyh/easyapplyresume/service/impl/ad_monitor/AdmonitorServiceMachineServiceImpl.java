package com.zyh.easyapplyresume.service.impl.ad_monitor;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorServiceMachineMapper;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorServiceMachineConnectForm;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorServiceMachineForm;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorServiceMachineJianKongForm;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorServiceMachine;
import com.zyh.easyapplyresume.model.query.ad_monitor.AdmonitorServiceMachineQuery;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorServiceMachineInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorServiceMachineJianKongVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorServiceMachinePageVO;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorServiceMachineService;
import com.zyh.easyapplyresume.utils.admonitorvalidator.AdmonitorServiceMachineFormValidator;
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
public class AdmonitorServiceMachineServiceImpl implements AdmonitorServiceMachineService {
    @Autowired
    private AdmonitorServiceMachineMapper admonitorServiceMachineMapper;
    @Override
    public Integer addAdmonitorServiceMachine(AdmonitorServiceMachineForm admonitorServiceMachineForm) {
        try{
            log.info("开始添加服务器");
            AdmonitorServiceMachineFormValidator.validateForAdd(admonitorServiceMachineForm);
            AdmonitorServiceMachine admonitorServiceMachine = BeanUtil.copyProperties(admonitorServiceMachineForm, AdmonitorServiceMachine.class);
            admonitorServiceMachine.setServiceMachineCreatedTime(new Date());
            admonitorServiceMachine.setServiceMachineUpdatedTime(new Date());
            admonitorServiceMachine.setDeleted(0);
            return admonitorServiceMachineMapper.insert(admonitorServiceMachine);
        }catch (BusException e){
            log.info("添加服务器失败1");
            e.printStackTrace();
            throw e;
        }catch (Exception e){
            log.info("添加服务器失败2");
            e.printStackTrace();
            throw new RuntimeException("添加服务器失败");
        }

    }


    @Override
    public Integer updateAdmonitorServiceMachine(AdmonitorServiceMachineForm admonitorServiceMachineForm) {
        try{
            log.info("开始更新服务器");
            AdmonitorServiceMachineFormValidator.validateForUpdate(admonitorServiceMachineForm);
            AdmonitorServiceMachine admonitorServiceMachine = BeanUtil.copyProperties(admonitorServiceMachineForm, AdmonitorServiceMachine.class);
            admonitorServiceMachine.setServiceMachineUpdatedTime(new Date());
            return admonitorServiceMachineMapper.updateById(admonitorServiceMachine);
        }catch (BusException e){
            log.info("更新服务器失败1");
            e.printStackTrace();
            throw e;
        }catch (Exception e){
            log.info("更新服务器失败2");
            e.printStackTrace();
            throw new RuntimeException("更新服务器失败");
        }
    }

    @Override
    public Integer deleteAdmonitorServiceMachine(Integer id) {
        try{
            log.info("开始删除服务器");
            AdmonitorServiceMachine admonitorServiceMachine = admonitorServiceMachineMapper.selectById(id);
            admonitorServiceMachine.setDeleted(1);
            return admonitorServiceMachineMapper.updateById(admonitorServiceMachine);
        }catch (BusException e){
            log.info("删除服务器失败1");
            e.printStackTrace();
            throw e;
        } catch (Exception e){
            log.info("删除服务器失败2");
            e.printStackTrace();
            throw new RuntimeException("删除服务器失败");
        }
    }

    @Override
    public AdmonitorServiceMachineInfoVO getAdmonitorServiceMachineInfo(Integer id) {
        try{
            log.info("开始查询服务器");
            AdmonitorServiceMachine admonitorServiceMachine = admonitorServiceMachineMapper.selectById(id);
            return BeanUtil.copyProperties(admonitorServiceMachine, AdmonitorServiceMachineInfoVO.class);
        }catch (BusException e){
            log.info("查询服务器失败1");
            e.printStackTrace();
            throw e;
        }catch (Exception e){
            log.info("查询服务器失败2");
            e.printStackTrace();
            throw new RuntimeException("查询服务器失败");
        }
    }

    @Override
    public Page<AdmonitorServiceMachinePageVO> getAdmonitorServiceMachinePage(Integer pageNum, Integer pageSize, AdmonitorServiceMachineQuery admonitorServiceMachineQuery) {
        try{
            log.info("开始分页查询服务器");
            Page<AdmonitorServiceMachine> page = new Page<>(pageNum,pageSize);
            LambdaQueryWrapper<AdmonitorServiceMachine> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            if (admonitorServiceMachineQuery != null){
                if (admonitorServiceMachineQuery.getServiceMachineName() != null){
                    lambdaQueryWrapper.like(AdmonitorServiceMachine::getServiceMachineName,admonitorServiceMachineQuery.getServiceMachineName());
                }
            }
            lambdaQueryWrapper.eq(AdmonitorServiceMachine::getDeleted,0);
            Page<AdmonitorServiceMachine> admonitorServiceMachinePage = admonitorServiceMachineMapper.selectPage(page, lambdaQueryWrapper);
            List<AdmonitorServiceMachinePageVO> voList = admonitorServiceMachinePage.getRecords().stream()
                    .map(vo -> {
                        AdmonitorServiceMachinePageVO admonitorServiceMachinePageVO = new AdmonitorServiceMachinePageVO();
                        BeanUtil.copyProperties(vo, admonitorServiceMachinePageVO);
                        return admonitorServiceMachinePageVO;
                    })
                    .collect(Collectors.toList());
            Page<AdmonitorServiceMachinePageVO> resultPage = new Page<>();
            resultPage.setCurrent(admonitorServiceMachinePage.getCurrent());
            resultPage.setSize(admonitorServiceMachinePage.getSize());
            resultPage.setTotal(admonitorServiceMachinePage.getTotal());
            resultPage.setPages(admonitorServiceMachinePage.getPages());
            resultPage.setRecords(voList != null ? voList : Collections.emptyList());
            log.info("分页查询服务器成功");
            return resultPage;

        }catch (BusException e){
            e.printStackTrace();
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("查询服务器失败");
        }
    }

    @Override
    public boolean testServiceMachineConnect(AdmonitorServiceMachineConnectForm admonitorServiceMachineConnectForm) {
        return false;
    }

    @Override
    public AdmonitorServiceMachineJianKongVO getAdmonitorServiceMachineJianKongInfo(AdmonitorServiceMachineJianKongForm admonitorServiceMachineJianKongForm) {
        return null;
    }
}
