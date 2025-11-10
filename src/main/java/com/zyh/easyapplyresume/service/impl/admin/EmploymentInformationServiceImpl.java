package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.CodeEnum;
import com.zyh.easyapplyresume.mapper.admin.EmploymentInformationMapper;
import com.zyh.easyapplyresume.mapper.admin.ProvinceMapMapper;
import com.zyh.easyapplyresume.model.form.admin.EmploymentInformationForm;
import com.zyh.easyapplyresume.model.pojo.admin.EmploymentInformation;
import com.zyh.easyapplyresume.model.query.admin.EmploymentInformationQuery;
import com.zyh.easyapplyresume.model.vo.admin.EmploymentInformationInfoVO;
import com.zyh.easyapplyresume.service.admin.EmploymentInformationService;
import com.zyh.easyapplyresume.utils.Validator.EmploymentInformationFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

/**
 * @author shiningCloud2025
 */
@Service
@Transactional
public class EmploymentInformationServiceImpl implements EmploymentInformationService {
    @Autowired
    private EmploymentInformationMapper employmentInformationMapper;
    @Autowired
    private ProvinceMapMapper provinceMapMapper;
    @Override
    public void addEmploymentInformation(EmploymentInformationForm employmentInformationForm) {
        EmploymentInformationFormValidator.validateForAdd(employmentInformationForm);
        EmploymentInformation employmentInformation = BeanUtil.copyProperties(employmentInformationForm, EmploymentInformation.class);
        List<Integer>  provinceIds = employmentInformationForm.getEmploymentInformationRecruitLocationFirst();
        List<Integer>  cityIds = employmentInformationForm.getEmploymentInformationRecruitLocationSecond();
        // 校验长度一致
        if (provinceIds.size() != cityIds.size()) {
            throw new BusException(CodeEnum.EMPLOYMENT_LOCATION_LENGTH_NOT_MATCH);
        }
        // 获取两个集合的迭代器
        Iterator<Integer> provinceIt = provinceIds.iterator();
        Iterator<Integer> cityIt = cityIds.iterator();
        // 同步遍历：一次取一组
        while (provinceIt.hasNext() && cityIt.hasNext()) {
            Integer provinceId = provinceIt.next();
            Integer cityId = cityIt.next();
            employmentInformation.setEmploymentInformationRecruitLocationFirst(provinceId);
            employmentInformation.setEmploymentInformationRecruitLocationSecond(cityId);

        }


    }

    @Override
    public void updateEmploymentInformation(EmploymentInformationForm employmentInformationForm) {
        EmploymentInformationFormValidator.validateForUpdate(employmentInformationForm);
    }

    @Override
    public void deleteEmploymentInformation(Integer employmentInformationId) {
//        LambdaQueryWrapper<EmploymentInformation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(EmploymentInformation::getEmploymentInformationCode, employmentInformationId);
//        List<EmploymentInformation> employmentInformations = employmentInformationMapper.selectList(lambdaQueryWrapper);
//        for(EmploymentInformation employmentInformation : employmentInformations){
//            employmentInformation.setDeleted(1);
//            employmentInformationMapper.updateById(employmentInformation);
//        }
        // 1. 创建 LambdaUpdateWrapper（指定实体类）
        LambdaUpdateWrapper<EmploymentInformation> updateWrapper = new LambdaUpdateWrapper<>();

        // 2. 设置更新字段：deleted = 1
        updateWrapper.set(EmploymentInformation::getDeleted, 1);

        // 3. 设置查询条件：employmentInformationCode = employmentInformationId（和原代码条件一致）
        updateWrapper.eq(EmploymentInformation::getEmploymentInformationCode, employmentInformationId);

        // 4. 执行单条UPDATE语句，批量更新所有符合条件的记录
        int updateCount = employmentInformationMapper.update(null, updateWrapper);
    }

    @Override
    public EmploymentInformationInfoVO getEmploymentInformationInfo(Integer employmentInformationId) {
        return null;
    }

    @Override
    public List<EmploymentInformationInfoVO> getEmploymentInformationPage(int size, int page, EmploymentInformationQuery employmentInformationQuery) {
        return List.of();
    }

    @Override
    public List<EmploymentInformationInfoVO> getAllEmploymentInformation() {
        return List.of();
    }
}
