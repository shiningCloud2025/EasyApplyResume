package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.RecruitPositionMapper;
import com.zyh.easyapplyresume.model.form.admin.RecruitPositionForm;
import com.zyh.easyapplyresume.model.pojo.admin.RecruitPosition;
import com.zyh.easyapplyresume.model.query.admin.RecruitPositionQuery;
import com.zyh.easyapplyresume.model.vo.admin.RecruitPositionInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.RecruitPositionPageVO;
import com.zyh.easyapplyresume.service.admin.RecruitPositionService;
import com.zyh.easyapplyresume.utils.adminvalidator.RecruitPositionFormValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author shiningCloud2025
 */
@Service
@Transactional
public class RecruitPositionServiceImpl implements RecruitPositionService {
    @Autowired
    private RecruitPositionMapper recruitPositionMapper;
    @Override
    public Integer addRecruitPosition(RecruitPositionForm recruitPositionForm) {
        RecruitPositionFormValidator.validateForAdd(recruitPositionForm);
        RecruitPosition recruitPosition = new RecruitPosition();
        BeanUtils.copyProperties(recruitPositionForm, recruitPosition);
        recruitPosition.setCreatedTime(new Date());
        recruitPosition.setUpdatedTime(new Date());
        return recruitPositionMapper.insert(recruitPosition);
    }

    @Override
    public Integer updateRecruitPosition(RecruitPositionForm recruitPositionForm) {
        RecruitPositionFormValidator.validateForUpdate(recruitPositionForm);
        RecruitPosition recruitPosition = new RecruitPosition();
        BeanUtils.copyProperties(recruitPositionForm, recruitPosition);
        recruitPosition.setUpdatedTime(new Date());
        return recruitPositionMapper.updateById(recruitPosition);
    }

    @Override
    public Integer deleteRecruitPosition(Integer recruitPositionId) {
        try {
           return recruitPositionMapper.deleteById(recruitPositionId);
        } catch (Exception e) {
            throw new BusException(AdminCodeEnum.NOT_DELETE_RECRUIT_POSITION);
        }
    }

    @Override
    public RecruitPositionInfoVO queryRecruitPosition(Integer recruitPositionId) {
        RecruitPositionInfoVO recruitPositionInfoVO = recruitPositionMapper.findRecruitPositionInfoById(recruitPositionId);
        return recruitPositionInfoVO;
    }

    @Override
    public Page<RecruitPositionPageVO> queryRecruitPositionPage(Integer pageNum, Integer pageSize, RecruitPositionQuery recruitPositionQuery) {
        Page<RecruitPositionPageVO> page = new Page<>(pageNum, pageSize);
        Page<RecruitPositionPageVO> recruitPositionPageVO = recruitPositionMapper.queryRecruitPositionPage(page, recruitPositionQuery);
        return recruitPositionPageVO;
    }

    @Override
    public List<RecruitPositionInfoVO> queryAllRecruitPositionPage() {
        List<RecruitPosition> recruitPositions = recruitPositionMapper.selectList(null);
        return BeanUtil.copyToList(recruitPositions, RecruitPositionInfoVO.class);

    }
}
