package com.zyh.easyapplyresume.service.impl.ad_monitor;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorServiceMachineMapper;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorServiceMachineConnectForm;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorServiceMachineForm;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorServiceMachineJianKongForm;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorServiceMachine;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorServiceMachineInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorServiceMachineJianKongVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorServiceMachinePageVO;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorServiceMachineService;
import com.zyh.easyapplyresume.utils.admonitorvalidator.AdmonitorServiceMachineFormValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
            AdmonitorServiceMachineFormValidator.validateForAdd(admonitorServiceMachineForm);
            AdmonitorServiceMachine admonitorServiceMachine = BeanUtil.copyProperties(admonitorServiceMachineForm, AdmonitorServiceMachine.class);
            admonitorServiceMachine.setServiceMachineCreatedTime(new Date());
            admonitorServiceMachine.setServiceMachineUpdatedTime(new Date());
            admonitorServiceMachine.setDeleted(0);
            return admonitorServiceMachineMapper.insert(admonitorServiceMachine);
        }catch (BusException e){
            e.printStackTrace();
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("添加服务器失败");
        }

    }


    @Override
    public Integer updateAdmonitorServiceMachine(AdmonitorServiceMachineForm admonitorServiceMachineForm) {
        try{
            AdmonitorServiceMachineFormValidator.validateForUpdate(admonitorServiceMachineForm);
            AdmonitorServiceMachine admonitorServiceMachine = BeanUtil.copyProperties(admonitorServiceMachineForm, AdmonitorServiceMachine.class);
            admonitorServiceMachine.setServiceMachineUpdatedTime(new Date());
            return admonitorServiceMachineMapper.updateById(admonitorServiceMachine);
        }catch (BusException e){
            e.printStackTrace();
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("更新服务器失败");
        }
    }

    @Override
    public Integer deleteAdmonitorServiceMachine(Integer id) {
        try{
            AdmonitorServiceMachine admonitorServiceMachine = admonitorServiceMachineMapper.selectById(id);
            admonitorServiceMachine.setDeleted(1);
            return admonitorServiceMachineMapper.updateById(admonitorServiceMachine);
        }catch (BusException e){
            e.printStackTrace();
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("删除服务器失败");
        }
    }

    @Override
    public AdmonitorServiceMachineInfoVO getAdmonitorServiceMachineInfo(Integer id) {
        return null;
    }

    @Override
    public Page<AdmonitorServiceMachinePageVO> getAdmonitorServiceMachinePage(Integer pageNum, Integer pageSize) {
        return null;
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
