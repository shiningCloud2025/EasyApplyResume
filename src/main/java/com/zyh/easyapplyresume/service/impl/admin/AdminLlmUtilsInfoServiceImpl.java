package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminLlmUtilsInfoMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminLlmUtilsInfoForm;
import com.zyh.easyapplyresume.model.query.admin.AdminLlmUtilsInfoQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminLlmUtilsInfoPageVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminLlmUtilsInfoVO;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.AdminLlmUtilsInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminLlmUtilsInfoServiceImpl implements AdminLlmUtilsInfoService {

    @Autowired
    private AdminLlmUtilsInfoMapper adminLlmUtilsInfoMapper;

    @Override
    public void addAdminLlmUtilsInfo(AdminLlmUtilsInfoForm adminLlmUtilsInfoForm) {

    }

    @Override
    public AdminLlmUtilsInfoVO findAdminLlmUtilsInfoById(Long llmUtilsInfoId) {
        return null;
    }

    @Override
    public Page<AdminLlmUtilsInfoPageVO> findAdminLlmUtilsInfoByPage(Integer pageNum, Integer pageSize, AdminLlmUtilsInfoQuery adminLlmUtilsInfoQuery) {
        return null;
    }
}
