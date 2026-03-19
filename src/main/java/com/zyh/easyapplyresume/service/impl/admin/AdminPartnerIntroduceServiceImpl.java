package com.zyh.easyapplyresume.service.impl.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminPartnerIntroduceMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminPartnerIntroduceForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminPartnerIntroduce;
import com.zyh.easyapplyresume.model.vo.admin.AdminPartnerIntroduceInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminPartnerIntroduceService;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.utils.adminvalidator.AdminPartnerIntroduceValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 合作伙伴Service实现类
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminPartnerIntroduceServiceImpl implements AdminPartnerIntroduceService {
    @Autowired
    private AdminPartnerIntroduceMapper partnerIntroduceMapper;

    @Override
    public Integer addPartnerIntroduce(AdminPartnerIntroduceForm partnerIntroduceForm) {
        try {
            log.info("添加合作伙伴");
            AdminPartnerIntroduceValidator.validateForAdd(partnerIntroduceForm);
            List<AdminPartnerIntroduce> partnerIntroduces = partnerIntroduceMapper.selectList(null);
            if (partnerIntroduces.size() > 0) {
                log.error("已添加过合作伙伴");
                throw new BusException(AdminCodeEnum.PARTNER_INTRODUCE_ALREADY_ADD);
            }
            AdminPartnerIntroduce partnerIntroduce = new AdminPartnerIntroduce();
            BeanUtils.copyProperties(partnerIntroduceForm, partnerIntroduce);
            partnerIntroduce.setPartnerIntroduceUpdatedTime(LocalDateTime.now());
            int result = partnerIntroduceMapper.insert(partnerIntroduce);
            log.info("添加合作伙伴成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("添加合作伙伴失败");
            throw new BusException(AdminCodeEnum.PARTNER_INTRODUCE_ADD_FAIL);
        }
    }

    @Override
    public Integer updatePartnerIntroduce(AdminPartnerIntroduceForm partnerIntroduceForm) {
        try {
            log.info("修改合作伙伴");
            AdminPartnerIntroduceValidator.validateForUpdate(partnerIntroduceForm);
            AdminPartnerIntroduce partnerIntroduce = new AdminPartnerIntroduce();
            BeanUtils.copyProperties(partnerIntroduceForm, partnerIntroduce);
            partnerIntroduce.setPartnerIntroduceUpdatedTime(LocalDateTime.now());
            int result = partnerIntroduceMapper.updateById(partnerIntroduce);
            log.info("修改合作伙伴成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("修改合作伙伴失败");
            throw new BusException(AdminCodeEnum.PARTNER_INTRODUCE_UPDATE_FAIL);
        }
    }

    @Override
    public AdminPartnerIntroduceInfoVO getPartnerIntroduceInfo() {
        try {
            log.info("获取合作伙伴信息");
            List<AdminPartnerIntroduce> partnerIntroduces = partnerIntroduceMapper.selectList(null);
            if (partnerIntroduces.size() > 0) {
                AdminPartnerIntroduceInfoVO infoVO = new AdminPartnerIntroduceInfoVO();
                BeanUtils.copyProperties(partnerIntroduces.get(0), infoVO);
                return infoVO;
            }
            return null;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取合作伙伴信息失败");
            throw new BusException(AdminCodeEnum.PARTNER_INTRODUCE_GET_INFO_FAIL);
        }
    }
}

