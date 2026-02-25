package com.zyh.easyapplyresume.service.admin;

import com.zyh.easyapplyresume.model.form.admin.AdminMediaReportForm;
import com.zyh.easyapplyresume.model.vo.admin.AdminMediaReportInfoVO;

/**
 * 媒体报道Service接口
 * @author shiningCloud2025
 */
public interface AdminMediaReportService {

    /**
     * 添加媒体报道
     * @param mediaReportForm
     * @return
     */
    Integer addMediaReport(AdminMediaReportForm mediaReportForm);

    /**
     * 修改媒体报道
     * @param mediaReportForm
     * @return
     */
    Integer updateMediaReport(AdminMediaReportForm mediaReportForm);

    /**
     * 获取媒体报道信息
     * @return
     */
    AdminMediaReportInfoVO getMediaReportInfo();
}
