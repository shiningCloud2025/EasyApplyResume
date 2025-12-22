package com.zyh.easyapplyresume.service.impl.ad_monitor;

import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorAdminDaliyAdminNumMapper;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDaliyAdminNum;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDaliyAdminNumService;
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
public class AdmonitorAdminDaliyAdminNumServiceImpl implements AdmonitorAdminDaliyAdminNumService {
    @Autowired
    private AdmonitorAdminDaliyAdminNumMapper admonitorAdminDaliyAdminNumMapper;
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
    public Integer findFromTimeToEndTimeAdmonitorAdminDaliyAdminNum(Date fromDate, Date endDate) {
        try {
            log.info("查询开始");
            return admonitorAdminDaliyAdminNumMapper.findFromTimeToEndTimeAdmonitorAdminDaliyAdminNum(fromDate,endDate);
        }catch (Exception e){
            log.info("查询失败");
            throw new RuntimeException("查询失败");
        }
    }
}
