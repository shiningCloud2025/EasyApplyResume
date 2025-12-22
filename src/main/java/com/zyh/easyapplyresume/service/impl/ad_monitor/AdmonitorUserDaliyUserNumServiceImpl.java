package com.zyh.easyapplyresume.service.impl.ad_monitor;

import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorAdminDaliyAdminNumMapper;
import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorUserDaliyUserNumMapper;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorUserDaliyUserNum;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorUserDaliyUserNumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@Slf4j
public class AdmonitorUserDaliyUserNumServiceImpl implements AdmonitorUserDaliyUserNumService {

    @Autowired
    private AdmonitorUserDaliyUserNumMapper admonitorUserDaliyUserNumMapper;

    @Override
    public Integer addAdmonitorUserDaliyUserNum(AdmonitorUserDaliyUserNum admonitorUserDaliyUserNum) {
        try{
            log.info("新增记录开始");
            return admonitorUserDaliyUserNumMapper.insert(admonitorUserDaliyUserNum);
        }catch (Exception e){
            log.info("新增记录失败");
            throw new RuntimeException("新增记录失败");
        }
    }

    @Override
    public List<Integer> findFromTimeToEndTimeAdmonitorUserDaliyUserNum(Date fromDate, Date endDate) {
        try {
            log.info("查询开始");
            return admonitorUserDaliyUserNumMapper.findFromTimeToEndTimeAdmonitorUserDaliyUserNum(fromDate,endDate);
        }catch (Exception e){
            log.info("查询失败");
            throw new RuntimeException("查询失败");
        }
    }
}
