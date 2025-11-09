package com.zyh.easyapplyresume.service.admin;

import com.zyh.easyapplyresume.model.form.admin.EmploymentInformationForm;
import com.zyh.easyapplyresume.model.query.admin.EmploymentInformationQuery;
import com.zyh.easyapplyresume.model.vo.admin.EmploymentInformationInfoVO;

import java.util.List;

/**
 * @author shiningCloud2025
 */
public interface EmploymentInformationService {
    /**
     * 新增招聘信息
     * @param employmentInformationForm
     */
    public void addEmploymentInformation(EmploymentInformationForm employmentInformationForm);
    /**
     * 修改招聘信息
     * @param employmentInformationForm
     */
    public void updateEmploymentInformation(EmploymentInformationForm employmentInformationForm);
    /**
     * 删除招聘信息
     * @param employmentInformationId
     */
    public void deleteEmploymentInformation(Integer employmentInformationId);
    /**
     * 获取招聘信息信息
     * @param employmentInformationId
     * @return
     */
    public EmploymentInformationInfoVO getEmploymentInformationInfo(Integer employmentInformationId);
    /**
     * 分页查询招聘信息信息
     * @param size
     * @param page
     * @param employmentInformationQuery
     * @return
     */
    public List<EmploymentInformationInfoVO> getEmploymentInformationPage(int size, int page, EmploymentInformationQuery employmentInformationQuery);
    /**
     * 获取所有招聘信息信息
     * @return
     */
    public List<EmploymentInformationInfoVO> getAllEmploymentInformation();
}
