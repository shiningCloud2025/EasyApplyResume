package com.zyh.easyapplyresume.service.impl.ad_monitor;

import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorAdminDaliyAdminNumMapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminMapper;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDaliyAdminNum;
import com.zyh.easyapplyresume.model.pojo.admin.Admin;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDaliyAdminNumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdmonitorAdminDaliyAdminNumServiceImpl implements AdmonitorAdminDaliyAdminNumService {
    @Autowired
    private AdmonitorAdminDaliyAdminNumMapper admonitorAdminDaliyAdminNumMapper;

    @Autowired
    private AdminMapper adminMapper;
    @Override
    public Integer addAdmonitorAdminDaliyAdminNum(AdmonitorAdminDaliyAdminNum admonitorAdminDaliyAdminNum) {
        try{
            log.info("新增记录开始");
            return admonitorAdminDaliyAdminNumMapper.insert(admonitorAdminDaliyAdminNum);
        }catch (Exception e){
            log.info("新增记录失败");
            throw new RuntimeException("新增记录失败");
        }
    }

    @Override
    public List<Integer> findFromTimeToEndTimeAdmonitorAdminDaliyAdminNum(Date fromDate, Date endDate) {
        try {
            log.info("查询开始");
            LocalDate localFromDate = fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localEndDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return admonitorAdminDaliyAdminNumMapper.findFromTimeToEndTimeAdmonitorAdminDaliyAdminNum(localFromDate,localEndDate);
        }catch (Exception e){
            log.info("查询失败");
            throw new RuntimeException("查询失败");
        }
    }

    @Override
    public Integer calculateAdminTotalNum() {
        try{
            log.info("计算管理员总数开始");
            List<Admin> admins = adminMapper.selectList(null);
            log.info("管理员总数为：{}",admins.size());
            return admins.size();
        }catch (Exception e){
            log.info("计算管理员总数失败");
            throw new RuntimeException("计算管理员总数失败");
        }
    }
}
