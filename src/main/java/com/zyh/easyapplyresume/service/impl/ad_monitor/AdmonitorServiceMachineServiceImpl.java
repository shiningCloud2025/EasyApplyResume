package com.zyh.easyapplyresume.service.impl.ad_monitor;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
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
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorServiceMachineService;
import com.zyh.easyapplyresume.utils.admonitorvalidator.AdmonitorServiceMachineFormValidator;
import com.zyh.easyapplyresume.utils.security.ServiceMachinePasswordCryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
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
            String encryptedPassword = ServiceMachinePasswordCryptoUtil.encrypt(admonitorServiceMachine.getServiceMachinePassword());
            admonitorServiceMachine.setServiceMachinePassword(encryptedPassword);
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
            AdmonitorServiceMachine oldServiceMachine =
                    admonitorServiceMachineMapper.selectById(admonitorServiceMachineForm.getServiceMachineId());
            if (oldServiceMachine == null || oldServiceMachine.getDeleted() == 1) {
                throw new RuntimeException("服务器不存在");
            }
            AdmonitorServiceMachine admonitorServiceMachine = BeanUtil.copyProperties(admonitorServiceMachineForm, AdmonitorServiceMachine.class);
            String submitPassword = admonitorServiceMachineForm.getServiceMachinePassword();
            String dbPassword = oldServiceMachine.getServiceMachinePassword();

            // 如果和数据库里的密码一样，说明前端传回来的就是原来的密文，不再重复加密
            if (submitPassword.equals(dbPassword)) {
                admonitorServiceMachine.setServiceMachinePassword(dbPassword);
            } else {
                // 如果不一样，说明用户输入了新密码，此时按明文处理并加密后再入库
                admonitorServiceMachine.setServiceMachinePassword(
                        ServiceMachinePasswordCryptoUtil.encrypt(submitPassword)
                );
            }
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
                if (admonitorServiceMachineQuery.getServiceMachineName() != null && !admonitorServiceMachineQuery.getServiceMachineName().trim().isEmpty()){
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
        Session session = null;
        try{
            JSch jSch = new JSch();
            session = jSch.getSession(admonitorServiceMachineConnectForm.getServiceMachineUsername(),admonitorServiceMachineConnectForm.getServiceMachineHost(),admonitorServiceMachineConnectForm.getServiceMachinePort());
            String plainPassword = ServiceMachinePasswordCryptoUtil.decrypt(
                    admonitorServiceMachineConnectForm.getServiceMachinePassword()
            );
            session.setPassword(plainPassword);
            // 跳过主机密钥检查
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(60000); // 1分钟超时
            session.connect();
            return  true;
        }catch (Exception e){
            log.error("测试服务器连接失败");
            e.printStackTrace();
            throw new RuntimeException("测试服务器连接失败");
        }finally {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }

    @Override
    public AdmonitorServiceMachineJianKongVO getAdmonitorServiceMachineJianKongInfo(AdmonitorServiceMachineJianKongForm admonitorServiceMachineJianKongForm) {
        Session session = null;
        try{
            JSch jSch = new JSch();
            session = jSch.getSession(admonitorServiceMachineJianKongForm.getServiceMachineUsername(),admonitorServiceMachineJianKongForm.getServiceMachineHost(),admonitorServiceMachineJianKongForm.getServiceMachinePort());
            String plainPassword = ServiceMachinePasswordCryptoUtil.decrypt(
                    admonitorServiceMachineJianKongForm.getServiceMachinePassword()
            );
            session.setPassword(plainPassword);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(60000);
            session.connect();
            AdmonitorServiceMachineJianKongVO admonitorServiceMachineJianKongVO = new AdmonitorServiceMachineJianKongVO();
            admonitorServiceMachineJianKongVO.setServiceMachineId(admonitorServiceMachineJianKongForm.getServiceMachineId());
            admonitorServiceMachineJianKongVO.setServiceMachineName(admonitorServiceMachineJianKongForm.getServiceMachineName());


            // 获取CPU使用率
            String cpuResult = executeCommand(session, "top -bn1 | grep 'Cpu(s)' | awk '{print $2}'");
            admonitorServiceMachineJianKongVO.setCpuUsage(parseDouble(cpuResult));

            // 获取内存信息
            String memResult = executeCommand(session, "free -m | grep Mem");
            String[] memParts = memResult.trim().split("\\s+");
            if (memParts.length >= 3) {
                admonitorServiceMachineJianKongVO.setMemoryTotal(parseLong(memParts[1]));
                admonitorServiceMachineJianKongVO.setMemoryUsed(parseLong(memParts[2]));
                if (admonitorServiceMachineJianKongVO.getMemoryTotal() > 0) {
                    admonitorServiceMachineJianKongVO.setMemoryUsage((double) admonitorServiceMachineJianKongVO.getMemoryUsed() / admonitorServiceMachineJianKongVO.getMemoryTotal() * 100);
                }
            }

            // 获取硬盘信息（根分区）
            String diskResult = executeCommand(session, "df -BG / | tail -1");
            String[] diskParts = diskResult.trim().split("\\s+");
            if (diskParts.length >= 5) {
                admonitorServiceMachineJianKongVO.setDiskTotal(parseLong(diskParts[1].replace("G", "")));
                admonitorServiceMachineJianKongVO.setDiskUsed(parseLong(diskParts[2].replace("G", "")));
                admonitorServiceMachineJianKongVO.setDiskUsage(parseDouble(diskParts[4].replace("%", "")));
            }

            // 获取系统负载
            String loadResult = executeCommand(session, "uptime | awk -F'load average:' '{print $2}' | awk -F',' '{print $1}'");
            admonitorServiceMachineJianKongVO.setLoadAverage(parseDouble(loadResult));

            return admonitorServiceMachineJianKongVO;
        } catch (Exception e) {
            log.error("获取服务器监控信息失败", e);
            throw new RuntimeException("获取服务器监控信息失败");
        } finally {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }

    /**
     * 执行SSH命令
     */
    private String executeCommand(Session session, String command) {
        ChannelExec channel = null;
        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.setInputStream(null);
            channel.setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();

            StringBuilder result = new StringBuilder();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                result.append(new String(buffer, 0, len));
            }
            return result.toString().trim();
        } catch (Exception e) {
            log.error("执行命令失败: {}", command, e);
            throw new RuntimeException("执行命令失败");
        } finally {
            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }
        }
    }

    private Double parseDouble(String str) {
        try {
            return Double.parseDouble(str.trim());
        } catch (Exception e) {
            return 0.0;
        }
    }

    private Long parseLong(String str) {
        try {
            return Long.parseLong(str.trim());
        } catch (Exception e) {
            return 0L;
        }
    }

}
