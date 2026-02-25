package com.zyh.easyapplyresume.service.impl.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminCustomerServiceMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminCustomerServiceForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminCustomerService;
import com.zyh.easyapplyresume.model.vo.admin.AdminCustomerServiceInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminCustomerServiceService;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.utils.adminvalidator.AdminCustomerServiceValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 人工客服Service实现类
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminCustomerServiceServiceImpl implements AdminCustomerServiceService {
    @Autowired
    private AdminCustomerServiceMapper customerServiceMapper;

    @Override
    public Integer addCustomerService(AdminCustomerServiceForm customerServiceForm) {
        try {
            log.info("添加人工客服");
            AdminCustomerServiceValidator.validateForAdd(customerServiceForm);
            List<AdminCustomerService> customerServices = customerServiceMapper.selectList(null);
            if (customerServices.size() > 0) {
                log.error("已添加过人工客服");
                throw new BusException(AdminCodeEnum.CUSTOMER_SERVICE_ALREADY_ADD);
            }
            AdminCustomerService customerService = new AdminCustomerService();
            BeanUtils.copyProperties(customerServiceForm, customerService);
            customerService.setCustomerServiceUpdatedTime(new Date());
            int result = customerServiceMapper.insert(customerService);
            log.info("添加人工客服成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("添加人工客服失败");
            throw new BusException(AdminCodeEnum.CUSTOMER_SERVICE_ADD_FAIL);
        }
    }

    @Override
    public Integer updateCustomerService(AdminCustomerServiceForm customerServiceForm) {
        try {
            log.info("修改人工客服");
            AdminCustomerServiceValidator.validateForUpdate(customerServiceForm);
            AdminCustomerService customerService = new AdminCustomerService();
            BeanUtils.copyProperties(customerServiceForm, customerService);
            customerService.setCustomerServiceUpdatedTime(new Date());
            int result = customerServiceMapper.updateById(customerService);
            log.info("修改人工客服成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("修改人工客服失败");
            throw new BusException(AdminCodeEnum.CUSTOMER_SERVICE_UPDATE_FAIL);
        }
    }

    @Override
    public AdminCustomerServiceInfoVO getCustomerServiceInfo() {
        try {
            log.info("获取人工客服信息");
            List<AdminCustomerService> customerServices = customerServiceMapper.selectList(null);
            if (customerServices.size() > 0) {
                AdminCustomerServiceInfoVO infoVO = new AdminCustomerServiceInfoVO();
                BeanUtils.copyProperties(customerServices.get(0), infoVO);
                return infoVO;
            }
            return null;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取人工客服信息失败");
            throw new BusException(AdminCodeEnum.CUSTOMER_SERVICE_GET_INFO_FAIL);
        }
    }
}

