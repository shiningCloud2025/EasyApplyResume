package com.zyh.easyapplyresume.service.impl.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminTeamIntroduceMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminTeamIntroduceForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminTeamIntroduce;
import com.zyh.easyapplyresume.model.vo.admin.AdminTeamIntroduceInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminTeamIntroduceService;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.utils.adminvalidator.AdminTeamIntroduceValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 团队介绍Service实现类
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminTeamIntroduceServiceImpl implements AdminTeamIntroduceService {
    @Autowired
    private AdminTeamIntroduceMapper teamIntroduceMapper;

    @Override
    public Integer addTeamIntroduce(AdminTeamIntroduceForm teamIntroduceForm) {
        try {
            log.info("添加团队介绍");
            // 校验表单字段
            AdminTeamIntroduceValidator.validateForAdd(teamIntroduceForm);
            List<AdminTeamIntroduce> teamIntroduces = teamIntroduceMapper.selectList(null);
            if (teamIntroduces.size() > 0) {
                log.error("已添加过团队介绍");
                throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
            }
            AdminTeamIntroduce teamIntroduce = new AdminTeamIntroduce();
            BeanUtils.copyProperties(teamIntroduceForm, teamIntroduce);
            teamIntroduce.setTeamIntroduceUpdatedTime(new Date());
            int result = teamIntroduceMapper.insert(teamIntroduce);
            log.info("添加团队介绍成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("添加团队介绍失败");
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public Integer updateTeamIntroduce(AdminTeamIntroduceForm teamIntroduceForm) {
        try {
            log.info("修改团队介绍");
            // 校验表单字段
            ValidationUtils.validateFormFields(teamIntroduceForm.getTeamIntroduceId(), teamIntroduceForm.getTeamIntroduceTitle(), teamIntroduceForm.getTeamIntroduceContent());
            AdminTeamIntroduce teamIntroduce = new AdminTeamIntroduce();
            BeanUtils.copyProperties(teamIntroduceForm, teamIntroduce);
            teamIntroduce.setTeamIntroduceUpdatedTime(new Date());
            int result = teamIntroduceMapper.updateById(teamIntroduce);
            log.info("修改团队介绍成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("修改团队介绍失败");
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public AdminTeamIntroduceInfoVO getTeamIntroduceInfo() {
        try {
            log.info("获取团队介绍信息");
            List<AdminTeamIntroduce> teamIntroduces = teamIntroduceMapper.selectList(null);
            if (teamIntroduces.size() > 0) {
                AdminTeamIntroduceInfoVO infoVO = new AdminTeamIntroduceInfoVO();
                BeanUtils.copyProperties(teamIntroduces.get(0), infoVO);
                return infoVO;
            }
            return null;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取团队介绍信息失败");
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }
}

