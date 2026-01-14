package com.zyh.easyapplyresume.service.impl.ad_monitor;

import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorAdminDaliyAdminNumMapper;
import com.zyh.easyapplyresume.mapper.mysql.ad_monitor.AdmonitorUserDaliyUserNumMapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserMapper;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorUserDaliyUserNum;
import com.zyh.easyapplyresume.model.pojo.user.User;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorUserDaliyUserNumService;
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
public class AdmonitorUserDaliyUserNumServiceImpl implements AdmonitorUserDaliyUserNumService {

    @Autowired
    private AdmonitorUserDaliyUserNumMapper admonitorUserDaliyUserNumMapper;

    @Autowired
    private UserMapper userMapper;
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
            LocalDate localFromDate = fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localEndDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return admonitorUserDaliyUserNumMapper.findFromTimeToEndTimeAdmonitorUserDaliyUserNum(localFromDate,localEndDate);
        }catch (Exception e){
            log.info("查询失败");
            throw new RuntimeException("查询失败");
        }
    }

    @Override
    public Integer calculateUserTotalNum() {
        try {
            log.info("计算用户总数开始");
            List<User> users = userMapper.selectList(null);
            log.info("用户总数为：{}",users.size());
            return users.size();
        }catch (Exception e){
            log.info("计算用户总数失败");
            throw new RuntimeException("计算用户总数失败");
        }
    }


}
