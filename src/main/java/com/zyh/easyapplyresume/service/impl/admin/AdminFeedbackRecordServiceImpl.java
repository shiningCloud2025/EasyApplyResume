package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminFeedbackRecordMapper;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminMapper;
import com.zyh.easyapplyresume.model.pojo.admin.Admin;
import com.zyh.easyapplyresume.model.pojo.admin.AdminFeedbackRecord;
import com.zyh.easyapplyresume.model.query.admin.AdminFeedbackRecordQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminFeedbackRecordInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminFeedbackRecordPageVO;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.AdminFeedbackRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminFeedbackRecordServiceImpl implements AdminFeedbackRecordService {
    @Autowired
    private AdminFeedbackRecordMapper adminFeedbackRecordMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Override
    public AdminFeedbackRecordInfoVO findAdminFeedbackRecordByFeedbackRecordId(Integer feedbackRecordId) {
        try{
            log.info("管理员反馈记录ID：{}", feedbackRecordId);
            AdminFeedbackRecord adminFeedbackRecord = adminFeedbackRecordMapper.selectById(feedbackRecordId);
            AdminFeedbackRecordInfoVO adminFeedbackRecordInfoVO = new AdminFeedbackRecordInfoVO();
            BeanUtil.copyProperties(adminFeedbackRecord, adminFeedbackRecordInfoVO);
            adminFeedbackRecordInfoVO.setAdminFeedbackRecordName(adminMapper.selectById(adminFeedbackRecord.getAdminFeedbackRecordAdminId()).getAdminUsername());
            adminFeedbackRecordInfoVO.setAdminFeedbackRecordApprovalPersonName(adminMapper.selectById(adminFeedbackRecord.getAdminFeedbackRecordApprovalPersonId()).getAdminUsername());
            log.info("管理员反馈记录信息：{}", adminFeedbackRecordInfoVO);
            return adminFeedbackRecordInfoVO;
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.error("管理员反馈记录查询失败：{}", e.getMessage());
            return null;
        }

    }

    @Override
    public Page<AdminFeedbackRecordPageVO> findAdminFeedbackRecordPage(Integer pageNum, Integer pageSize, AdminFeedbackRecordQuery adminFeedbackRecordQuery) {
        LambdaQueryWrapper<AdminFeedbackRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (adminFeedbackRecordQuery!= null){
            if (adminFeedbackRecordQuery.getAdminFeedbackRecordName()!= null&&!adminFeedbackRecordQuery.getAdminFeedbackRecordName().trim().isEmpty()){
                LambdaQueryWrapper<Admin> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.eq(Admin::getAdminUsername, adminFeedbackRecordQuery.getAdminFeedbackRecordName());
                List<Integer> idList = adminMapper.selectList(lambdaQueryWrapper1).stream().map(admin -> {
                    Integer adminId = admin.getAdminId();
                    return adminId;
                }).toList();
                lambdaQueryWrapper.in(AdminFeedbackRecord::getAdminFeedbackRecordId, idList);
            }
            if (adminFeedbackRecordQuery.getAdminFeedbackRecordTitle()!= null&&!adminFeedbackRecordQuery.getAdminFeedbackRecordTitle().trim().isEmpty()){
                lambdaQueryWrapper.like(AdminFeedbackRecord::getAdminFeedbackRecordTitle, adminFeedbackRecordQuery.getAdminFeedbackRecordTitle());
            }
            if (adminFeedbackRecordQuery.getAdminFeedbackRecordApprovalPersonName()!= null&&!adminFeedbackRecordQuery.getAdminFeedbackRecordApprovalPersonName().trim().isEmpty()){
                LambdaQueryWrapper<Admin> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.eq(Admin::getAdminUsername, adminFeedbackRecordQuery.getAdminFeedbackRecordApprovalPersonName());
                List<Integer> idList = adminMapper.selectList(lambdaQueryWrapper1).stream().map(admin -> {
                    Integer adminId = admin.getAdminId();
                    return adminId;
                }).toList();
                lambdaQueryWrapper.in(AdminFeedbackRecord::getAdminFeedbackRecordApprovalPersonId, idList);
            }
        }
        lambdaQueryWrapper.orderByDesc(AdminFeedbackRecord::getAdminFeedbackRecordId);

        Page<AdminFeedbackRecord> feedbackRecordPage = adminFeedbackRecordMapper.selectPage(
                new Page<>(pageNum, pageSize),
                lambdaQueryWrapper
        );

        Page<AdminFeedbackRecordPageVO> resultPage = new Page<>();
        resultPage.setCurrent(feedbackRecordPage.getCurrent());
        resultPage.setPages(feedbackRecordPage.getPages());
        resultPage.setSize(feedbackRecordPage.getSize());
        resultPage.setTotal(feedbackRecordPage.getTotal());
        resultPage.setRecords(feedbackRecordPage.getRecords().stream().map(
                feedbackRecord -> {
                    AdminFeedbackRecordPageVO adminFeedbackRecordPageVO = new AdminFeedbackRecordPageVO();
                    BeanUtil.copyProperties(feedbackRecord, adminFeedbackRecordPageVO);
                    adminFeedbackRecordPageVO.setAdminFeedbackRecordName(adminMapper.selectById(feedbackRecord.getAdminFeedbackRecordAdminId()).getAdminUsername());
                    adminFeedbackRecordPageVO.setAdminFeedbackRecordApprovalPersonName(adminMapper.selectById(feedbackRecord.getAdminFeedbackRecordApprovalPersonId()).getAdminUsername());
                    return adminFeedbackRecordPageVO;
                }
        ).toList());

        return resultPage;
    }
}
