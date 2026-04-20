package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminScoreTrainingDataMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminScoreTrainingDataForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminScoreTrainingData;
import com.zyh.easyapplyresume.model.query.admin.AdminScoreTrainingDataQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminScoreTrainingDataInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminScoreTrainingDataPageVO;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.AdminScoreTrainingDataService;
import com.zyh.easyapplyresume.utils.adminvalidator.AdminScoreTrainingDataFormValidator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 简历评分训练数据服务实现
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminScoreTrainingDataServiceImpl implements AdminScoreTrainingDataService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private AdminScoreTrainingDataMapper adminScoreTrainingDataMapper;

    @Override
    public Integer addScoreTrainingData(AdminScoreTrainingDataForm form) {
        try {
            AdminScoreTrainingDataFormValidator.validateForAdd(form);

            AdminScoreTrainingData scoreTrainingData = new AdminScoreTrainingData();
            BeanUtils.copyProperties(form, scoreTrainingData);
            scoreTrainingData.setScoreTrainingDataCreateTime(LocalDateTime.now());
            scoreTrainingData.setDeleted(0);

            return adminScoreTrainingDataMapper.insert(scoreTrainingData);
        } catch (BusException e) {
            log.info("新增简历评分训练数据业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("新增简历评分训练数据失败", e);
            throw new RuntimeException("新增简历评分训练数据失败");
        }
    }

    @Override
    public Integer deleteScoreTrainingData(Integer scoreTrainingDataId) {
        try {
            LambdaQueryWrapper<AdminScoreTrainingData> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminScoreTrainingData::getScoreTrainingDataId, scoreTrainingDataId);
            lambdaQueryWrapper.eq(AdminScoreTrainingData::getDeleted, 0);

            AdminScoreTrainingData scoreTrainingData = adminScoreTrainingDataMapper.selectOne(lambdaQueryWrapper);
            if (scoreTrainingData == null) {
                throw new BusException(AdminCodeEnum.SCORE_TRAINING_DATA_NOT_FOUND);
            }

            AdminScoreTrainingData updateEntity = new AdminScoreTrainingData();
            updateEntity.setScoreTrainingDataId(scoreTrainingDataId);
            updateEntity.setDeleted(1);

            return adminScoreTrainingDataMapper.updateById(updateEntity);
        } catch (BusException e) {
            log.info("删除简历评分训练数据业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("删除简历评分训练数据失败", e);
            throw new RuntimeException("删除简历评分训练数据失败");
        }
    }

    @Override
    public AdminScoreTrainingDataInfoVO findScoreTrainingDataById(Integer scoreTrainingDataId) {
        try {
            LambdaQueryWrapper<AdminScoreTrainingData> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminScoreTrainingData::getScoreTrainingDataId, scoreTrainingDataId);
            lambdaQueryWrapper.eq(AdminScoreTrainingData::getDeleted, 0);

            AdminScoreTrainingData scoreTrainingData = adminScoreTrainingDataMapper.selectOne(lambdaQueryWrapper);
            if (scoreTrainingData == null) {
                throw new BusException(AdminCodeEnum.SCORE_TRAINING_DATA_NOT_FOUND);
            }

            AdminScoreTrainingDataInfoVO vo = new AdminScoreTrainingDataInfoVO();
            BeanUtils.copyProperties(scoreTrainingData, vo);
            return vo;
        } catch (BusException e) {
            log.info("查询简历评分训练数据详情业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("查询简历评分训练数据详情失败", e);
            throw new RuntimeException("查询简历评分训练数据详情失败");
        }
    }

    @Override
    public Page<AdminScoreTrainingDataPageVO> findScoreTrainingDataByPage(Integer pageNum, Integer pageSize, AdminScoreTrainingDataQuery query) {
        try {
            LambdaQueryWrapper<AdminScoreTrainingData> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminScoreTrainingData::getDeleted, 0);

            if (query != null) {
                if (query.getScoreTrainingDataResumeName() != null && !query.getScoreTrainingDataResumeName().trim().isEmpty()) {
                    lambdaQueryWrapper.like(AdminScoreTrainingData::getScoreTrainingDataResumeName, query.getScoreTrainingDataResumeName().trim());
                }

                if (query.getScoreTrainingDataIndustryName() != null && !query.getScoreTrainingDataIndustryName().trim().isEmpty()) {
                    lambdaQueryWrapper.like(AdminScoreTrainingData::getScoreTrainingDataIndustryName, query.getScoreTrainingDataIndustryName().trim());
                }

                if (query.getScoreTrainingDataDataSource() != null) {
                    lambdaQueryWrapper.eq(AdminScoreTrainingData::getScoreTrainingDataDataSource, query.getScoreTrainingDataDataSource());
                }
            }

            lambdaQueryWrapper.orderByDesc(AdminScoreTrainingData::getScoreTrainingDataCreateTime);

            Page<AdminScoreTrainingData> page = adminScoreTrainingDataMapper.selectPage(
                    new Page<>(pageNum, pageSize),
                    lambdaQueryWrapper
            );

            List<AdminScoreTrainingDataPageVO> voList = page.getRecords().stream()
                    .map(item -> {
                        AdminScoreTrainingDataPageVO vo = new AdminScoreTrainingDataPageVO();
                        BeanUtils.copyProperties(item, vo);
                        return vo;
                    })
                    .collect(Collectors.toList());

            Page<AdminScoreTrainingDataPageVO> result = new Page<>();
            result.setRecords(voList);
            result.setCurrent(page.getCurrent());
            result.setSize(page.getSize());
            result.setTotal(page.getTotal());
            result.setPages(page.getPages());

            return result;
        } catch (BusException e) {
            log.info("分页查询简历评分训练数据业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("分页查询简历评分训练数据失败", e);
            throw new RuntimeException("分页查询简历评分训练数据失败");
        }
    }

    @Override
    public List<AdminScoreTrainingDataPageVO> findAllScoreTrainingData() {
        try {
            LambdaQueryWrapper<AdminScoreTrainingData> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminScoreTrainingData::getDeleted, 0);
            lambdaQueryWrapper.orderByDesc(AdminScoreTrainingData::getScoreTrainingDataCreateTime);

            List<AdminScoreTrainingData> scoreTrainingDataList = adminScoreTrainingDataMapper.selectList(lambdaQueryWrapper);

            return scoreTrainingDataList.stream().map(item -> {
                AdminScoreTrainingDataPageVO vo = new AdminScoreTrainingDataPageVO();
                BeanUtils.copyProperties(item, vo);
                return vo;
            }).collect(Collectors.toList());
        } catch (BusException e) {
            log.info("查询所有简历评分训练数据业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("查询所有简历评分训练数据失败", e);
            throw new RuntimeException("查询所有简历评分训练数据失败");
        }
    }

    @Override
    public void exportScoreTrainingData(AdminScoreTrainingDataQuery query, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<AdminScoreTrainingData> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminScoreTrainingData::getDeleted, 0);

            if (query != null) {
                if (query.getScoreTrainingDataResumeName() != null && !query.getScoreTrainingDataResumeName().trim().isEmpty()) {
                    lambdaQueryWrapper.like(AdminScoreTrainingData::getScoreTrainingDataResumeName, query.getScoreTrainingDataResumeName().trim());
                }

                if (query.getScoreTrainingDataIndustryName() != null && !query.getScoreTrainingDataIndustryName().trim().isEmpty()) {
                    lambdaQueryWrapper.like(AdminScoreTrainingData::getScoreTrainingDataIndustryName, query.getScoreTrainingDataIndustryName().trim());
                }

                if (query.getScoreTrainingDataDataSource() != null) {
                    lambdaQueryWrapper.eq(AdminScoreTrainingData::getScoreTrainingDataDataSource, query.getScoreTrainingDataDataSource());
                }
            }

            lambdaQueryWrapper.orderByDesc(AdminScoreTrainingData::getScoreTrainingDataCreateTime);

            List<AdminScoreTrainingData> dataList = adminScoreTrainingDataMapper.selectList(lambdaQueryWrapper);

            String fileName = URLEncoder.encode("简历评分训练数据.xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + fileName);

            try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("简历评分训练数据");

                sheet.setColumnWidth(0, 18 * 256);
                sheet.setColumnWidth(1, 35 * 256);
                sheet.setColumnWidth(2, 25 * 256);
                sheet.setColumnWidth(3, 80 * 256);
                sheet.setColumnWidth(4, 18 * 256);
                sheet.setColumnWidth(5, 18 * 256);
                sheet.setColumnWidth(6, 25 * 256);

                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("训练数据ID");
                headerRow.createCell(1).setCellValue("行业名称");
                headerRow.createCell(2).setCellValue("简历名称");
                headerRow.createCell(3).setCellValue("简历内容");
                headerRow.createCell(4).setCellValue("训练标签分数");
                headerRow.createCell(5).setCellValue("数据来源");
                headerRow.createCell(6).setCellValue("创建时间");

                int rowIndex = 1;
                for (AdminScoreTrainingData item : dataList) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(item.getScoreTrainingDataId() == null ? "" : String.valueOf(item.getScoreTrainingDataId()));
                    row.createCell(1).setCellValue(item.getScoreTrainingDataIndustryName() == null ? "" : item.getScoreTrainingDataIndustryName());
                    row.createCell(2).setCellValue(item.getScoreTrainingDataResumeName() == null ? "" : item.getScoreTrainingDataResumeName());
                    row.createCell(3).setCellValue(item.getScoreTrainingDataResumeContent() == null ? "" : item.getScoreTrainingDataResumeContent());
                    row.createCell(4).setCellValue(item.getScoreTrainingDataLabelScore() == null ? "" : String.valueOf(item.getScoreTrainingDataLabelScore()));
                    row.createCell(5).setCellValue(
                            Integer.valueOf(0).equals(item.getScoreTrainingDataDataSource()) ? "人工" :
                            Integer.valueOf(1).equals(item.getScoreTrainingDataDataSource()) ? "模型" : ""
                    );
                    row.createCell(6).setCellValue(
                            item.getScoreTrainingDataCreateTime() == null ? "" : item.getScoreTrainingDataCreateTime().format(DATE_TIME_FORMATTER)
                    );
                }

                workbook.write(response.getOutputStream());
                response.flushBuffer();
            }
        } catch (BusException e) {
            log.info("导出简历评分训练数据业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("导出简历评分训练数据失败", e);
            throw new RuntimeException("导出简历评分训练数据失败");
        }
    }
}
