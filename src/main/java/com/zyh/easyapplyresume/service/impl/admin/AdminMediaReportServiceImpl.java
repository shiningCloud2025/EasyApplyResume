package com.zyh.easyapplyresume.service.impl.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminMediaReportMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminMediaReportForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminMediaReport;
import com.zyh.easyapplyresume.model.vo.admin.AdminMediaReportInfoVO;
import com.zyh.easyapplyresume.service.admin.AdminMediaReportService;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 媒体报道Service实现类
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminMediaReportServiceImpl implements AdminMediaReportService {
    @Autowired
    private AdminMediaReportMapper mediaReportMapper;

    @Override
    public Integer addMediaReport(AdminMediaReportForm mediaReportForm) {
        try {
            log.info("添加媒体报道");
            List<AdminMediaReport> mediaReports = mediaReportMapper.selectList(null);
            if (mediaReports.size() > 0) {
                log.error("已添加过媒体报道");
                throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
            }
            AdminMediaReport mediaReport = new AdminMediaReport();
            BeanUtils.copyProperties(mediaReportForm, mediaReport);
            mediaReport.setMediaReportUpdatedTime(new Date());
            int result = mediaReportMapper.insert(mediaReport);
            log.info("添加媒体报道成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("添加媒体报道失败");
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public Integer updateMediaReport(AdminMediaReportForm mediaReportForm) {
        try {
            log.info("修改媒体报道");
            AdminMediaReport mediaReport = new AdminMediaReport();
            BeanUtils.copyProperties(mediaReportForm, mediaReport);
            mediaReport.setMediaReportUpdatedTime(new Date());
            int result = mediaReportMapper.updateById(mediaReport);
            log.info("修改媒体报道成功");
            return result;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("修改媒体报道失败");
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public AdminMediaReportInfoVO getMediaReportInfo() {
        try {
            log.info("获取媒体报道信息");
            List<AdminMediaReport> mediaReports = mediaReportMapper.selectList(null);
            if (mediaReports.size() > 0) {
                AdminMediaReportInfoVO infoVO = new AdminMediaReportInfoVO();
                BeanUtils.copyProperties(mediaReports.get(0), infoVO);
                return infoVO;
            }
            return null;
        } catch (BusException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取媒体报道信息失败");
            throw new BusException(AdminCodeEnum.SYSTEM_ERROR);
        }
    }
}

