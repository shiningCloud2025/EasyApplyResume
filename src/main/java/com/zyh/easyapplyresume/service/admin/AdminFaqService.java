package com.zyh.easyapplyresume.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.form.admin.AdminFaqForm;
import com.zyh.easyapplyresume.model.vo.admin.AdminFaqInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminFaqPageVO;

/**
 * 常见问题Service接口
 * @author shiningCloud2025
 */
public interface AdminFaqService {

    /**
     * 添加常见问题
     * @param faqForm
     * @return
     */
    Integer addFaq(AdminFaqForm faqForm);

    /**
     * 修改常见问题
     * @param faqForm
     * @return
     */
    Integer updateFaq(AdminFaqForm faqForm);

    /**
     * 删除常见问题
     * @param faqId
     * @return
     */
    Integer deleteFaq(Integer faqId);

    /**
     * 获取常见问题信息
     * @param faqId
     * @return
     */
    AdminFaqInfoVO getFaqInfo(Integer faqId);

    /**
     * 分页查询常见问题
     * @param size
     * @param page
     * @return
     */
    Page<AdminFaqPageVO> getFaqPage(int size, int page);
}
