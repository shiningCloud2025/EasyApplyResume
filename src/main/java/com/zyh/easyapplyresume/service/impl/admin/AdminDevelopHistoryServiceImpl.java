package com.zyh.easyapplyresume.service.impl.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminDevelopHistoryMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminDevelopHistoryForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminDevelopHistory;
import com.zyh.easyapplyresume.model.vo.admin.AdminDevelopHistoryInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminDevelopHistoryService;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.utils.adminvalidator.AdminDevelopHistoryValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 发展历程Service实现类
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminDevelopHistoryServiceImpl implements AdminDevelopHistoryService {
    @Autowired
    private AdminDevelopHistoryMapper developHistoryMapper;

    @Override
    public Integer addDevelopHistory(AdminDevelopHistoryForm developHistoryForm) {
        try {
            log.info("添加发展历程");
            AdminDevelopHistoryValidator.validateForAdd(developHistoryForm);
            List<AdminDevelopHistory> developHistories = developHistoryMapper.selectList(null);
            if (developHistories.size() > 0) {
                log.error("已添加过发展历程");
                throw new BusException(AdminCodeEnum.DEVELOP_HISTORY_ALREADY_ADD);
            }
            AdminDevelopHistory developHistory = new AdminDevelopHistory();
            BeanUtils.copyProperties(developHistoryForm, developHistory);
            developHistory.setDevelopHistoryUpdatedTime(LocalDateTime.now());
            int result = developHistoryMapper.insert(developHistory);
            log.info("添加发展历程成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("添加发展历程失败");
            throw new BusException(AdminCodeEnum.DEVELOP_HISTORY_ADD_FAIL);
        }
    }

    @Override
    public Integer updateDevelopHistory(AdminDevelopHistoryForm developHistoryForm) {
        try {
            log.info("修改发展历程");
            AdminDevelopHistoryValidator.validateForUpdate(developHistoryForm);
            AdminDevelopHistory developHistory = new AdminDevelopHistory();
            BeanUtils.copyProperties(developHistoryForm, developHistory);
            developHistory.setDevelopHistoryUpdatedTime(LocalDateTime.now());
            int result = developHistoryMapper.updateById(developHistory);
            log.info("修改发展历程成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("修改发展历程失败");
            throw new BusException(AdminCodeEnum.DEVELOP_HISTORY_UPDATE_FAIL);
        }
    }

    @Override
    public AdminDevelopHistoryInfoVO getDevelopHistoryInfo() {
        try {
            log.info("获取发展历程信息");
            List<AdminDevelopHistory> developHistories = developHistoryMapper.selectList(null);
            if (developHistories.size() > 0) {
                AdminDevelopHistoryInfoVO infoVO = new AdminDevelopHistoryInfoVO();
                BeanUtils.copyProperties(developHistories.get(0), infoVO);
                return infoVO;
            }
            return null;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取发展历程信息失败");
            throw new BusException(AdminCodeEnum.DEVELOP_HISTORY_GET_INFO_FAIL);
        }
    }
}

