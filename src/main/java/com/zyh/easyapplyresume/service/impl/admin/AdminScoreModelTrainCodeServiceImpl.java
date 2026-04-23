package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminScoreModelTrainCodeMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminScoreModelTrainCodeForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminScoreModelTrainCode;
import com.zyh.easyapplyresume.model.query.admin.AdminScoreModelTrainCodeQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminScoreModelTrainCodeInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminScoreModelTrainCodePageVO;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.AdminScoreModelTrainCodeService;
import com.zyh.easyapplyresume.utils.adminvalidator.AdminScoreModelTrainCodeFormValidator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 简历评分模型训练代码服务实现
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminScoreModelTrainCodeServiceImpl implements AdminScoreModelTrainCodeService {

    private static final String FILE_SEPARATOR = "++++???++++";
    private static final String FILE_NAME_PREFIX = "文件名:";

    @Autowired
    private AdminScoreModelTrainCodeMapper adminScoreModelTrainCodeMapper;

    @Override
    public Integer addScoreModelTrainCode(AdminScoreModelTrainCodeForm form) {
        try {
            AdminScoreModelTrainCodeFormValidator.validateForAdd(form);

            AdminScoreModelTrainCode adminScoreModelTrainCode = new AdminScoreModelTrainCode();
            BeanUtils.copyProperties(form, adminScoreModelTrainCode);
            adminScoreModelTrainCode.setScoreModelTrainCodeCreateTime(LocalDateTime.now());
            adminScoreModelTrainCode.setDeleted(0);

            return adminScoreModelTrainCodeMapper.insert(adminScoreModelTrainCode);
        } catch (BusException e) {
            log.info("新增训练代码业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("新增训练代码失败", e);
            throw new RuntimeException("新增训练代码失败");
        }
    }

    @Override
    public Integer updateScoreModelTrainCode(AdminScoreModelTrainCodeForm form) {
        try {
            AdminScoreModelTrainCodeFormValidator.validateForUpdate(form);

            AdminScoreModelTrainCode adminScoreModelTrainCode = new AdminScoreModelTrainCode();
            BeanUtils.copyProperties(form, adminScoreModelTrainCode);

            LambdaQueryWrapper<AdminScoreModelTrainCode> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminScoreModelTrainCode::getScoreModelTrainCodeId, form.getScoreModelTrainCodeId());
            lambdaQueryWrapper.eq(AdminScoreModelTrainCode::getDeleted, 0);

            int result = adminScoreModelTrainCodeMapper.update(adminScoreModelTrainCode, lambdaQueryWrapper);
            if (result == 0) {
                throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_NOT_FOUND);
            }

            return result;
        } catch (BusException e) {
            log.info("修改训练代码业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("修改训练代码失败", e);
            throw new RuntimeException("修改训练代码失败");
        }
    }

    @Override
    public Integer deleteScoreModelTrainCode(Integer scoreModelTrainCodeId) {
        try {
            LambdaUpdateWrapper<AdminScoreModelTrainCode> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(AdminScoreModelTrainCode::getScoreModelTrainCodeId, scoreModelTrainCodeId);
            lambdaUpdateWrapper.eq(AdminScoreModelTrainCode::getDeleted, 0);
            lambdaUpdateWrapper.set(AdminScoreModelTrainCode::getDeleted, 1);

            int result = adminScoreModelTrainCodeMapper.update(null, lambdaUpdateWrapper);
            if (result == 0) {
                throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_NOT_FOUND);
            }

            return result;
        } catch (BusException e) {
            log.info("删除训练代码业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("删除训练代码失败", e);
            throw new RuntimeException("删除训练代码失败");
        }
    }

    @Override
    public AdminScoreModelTrainCodeInfoVO findScoreModelTrainCodeById(Integer scoreModelTrainCodeId) {
        try {
            LambdaQueryWrapper<AdminScoreModelTrainCode> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminScoreModelTrainCode::getScoreModelTrainCodeId, scoreModelTrainCodeId);
            lambdaQueryWrapper.eq(AdminScoreModelTrainCode::getDeleted, 0);

            AdminScoreModelTrainCode adminScoreModelTrainCode = adminScoreModelTrainCodeMapper.selectOne(lambdaQueryWrapper);
            if (adminScoreModelTrainCode == null) {
                throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_NOT_FOUND);
            }

            AdminScoreModelTrainCodeInfoVO adminScoreModelTrainCodeInfoVO = new AdminScoreModelTrainCodeInfoVO();
            BeanUtils.copyProperties(adminScoreModelTrainCode, adminScoreModelTrainCodeInfoVO);
            return adminScoreModelTrainCodeInfoVO;
        } catch (BusException e) {
            log.info("查询训练代码详情业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("查询训练代码详情失败", e);
            throw new RuntimeException("查询训练代码详情失败");
        }
    }

    @Override
    public Page<AdminScoreModelTrainCodePageVO> findScoreModelTrainCodeByPage(Integer pageNum, Integer pageSize, AdminScoreModelTrainCodeQuery query) {
        try {
            LambdaQueryWrapper<AdminScoreModelTrainCode> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminScoreModelTrainCode::getDeleted, 0);

            if (query != null) {
                if (query.getScoreModelTrainCodeName() != null && !query.getScoreModelTrainCodeName().trim().isEmpty()) {
                    lambdaQueryWrapper.like(AdminScoreModelTrainCode::getScoreModelTrainCodeName, query.getScoreModelTrainCodeName().trim());
                }
                if (query.getScoreModelTrainCodeLanguage() != null && !query.getScoreModelTrainCodeLanguage().trim().isEmpty()) {
                    lambdaQueryWrapper.like(AdminScoreModelTrainCode::getScoreModelTrainCodeLanguage, query.getScoreModelTrainCodeLanguage().trim());
                }
            }

            lambdaQueryWrapper.orderByDesc(AdminScoreModelTrainCode::getScoreModelTrainCodeCreateTime);

            Page<AdminScoreModelTrainCode> page = adminScoreModelTrainCodeMapper.selectPage(
                    new Page<>(pageNum, pageSize),
                    lambdaQueryWrapper
            );

            List<AdminScoreModelTrainCodePageVO> adminScoreModelTrainCodePageVOList = page.getRecords().stream()
                    .map(item -> {
                        AdminScoreModelTrainCodePageVO adminScoreModelTrainCodePageVO = new AdminScoreModelTrainCodePageVO();
                        BeanUtils.copyProperties(item, adminScoreModelTrainCodePageVO);
                        return adminScoreModelTrainCodePageVO;
                    })
                    .collect(Collectors.toList());

            Page<AdminScoreModelTrainCodePageVO> result = new Page<>();
            result.setRecords(adminScoreModelTrainCodePageVOList);
            result.setCurrent(page.getCurrent());
            result.setSize(page.getSize());
            result.setTotal(page.getTotal());
            result.setPages(page.getPages());

            return result;
        } catch (BusException e) {
            log.info("分页查询训练代码业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("分页查询训练代码失败", e);
            throw new RuntimeException("分页查询训练代码失败");
        }
    }

    @Override
    public List<AdminScoreModelTrainCodePageVO> findAllScoreModelTrainCode() {
        try {
            LambdaQueryWrapper<AdminScoreModelTrainCode> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminScoreModelTrainCode::getDeleted, 0);
            lambdaQueryWrapper.orderByDesc(AdminScoreModelTrainCode::getScoreModelTrainCodeCreateTime);

            List<AdminScoreModelTrainCode> adminScoreModelTrainCodeList = adminScoreModelTrainCodeMapper.selectList(lambdaQueryWrapper);

            return adminScoreModelTrainCodeList.stream().map(item -> {
                AdminScoreModelTrainCodePageVO adminScoreModelTrainCodePageVO = new AdminScoreModelTrainCodePageVO();
                BeanUtils.copyProperties(item, adminScoreModelTrainCodePageVO);
                return adminScoreModelTrainCodePageVO;
            }).collect(Collectors.toList());
        } catch (BusException e) {
            log.info("查询所有训练代码业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("查询所有训练代码失败", e);
            throw new RuntimeException("查询所有训练代码失败");
        }
    }

    @Override
    public void downloadScoreModelTrainCode(Integer scoreModelTrainCodeId, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<AdminScoreModelTrainCode> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminScoreModelTrainCode::getScoreModelTrainCodeId, scoreModelTrainCodeId);
            lambdaQueryWrapper.eq(AdminScoreModelTrainCode::getDeleted, 0);

            AdminScoreModelTrainCode adminScoreModelTrainCode = adminScoreModelTrainCodeMapper.selectOne(lambdaQueryWrapper);
            if (adminScoreModelTrainCode == null) {
                throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_NOT_FOUND);
            }

            List<TrainCodeFilePart> trainCodeFilePartList = parseTrainCodeContent(adminScoreModelTrainCode.getScoreModelTrainCodeContent());

            String zipFileName = URLEncoder.encode(
                    adminScoreModelTrainCode.getScoreModelTrainCodeName() + ".zip",
                    StandardCharsets.UTF_8
            ).replaceAll("\\+", "%20");

            response.setContentType("application/zip");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + zipFileName);

            try (ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()))) {
                for (TrainCodeFilePart trainCodeFilePart : trainCodeFilePartList) {
                    ZipEntry zipEntry = new ZipEntry(trainCodeFilePart.getFileName());
                    zipOutputStream.putNextEntry(zipEntry);
                    zipOutputStream.write(trainCodeFilePart.getContent().getBytes(StandardCharsets.UTF_8));
                    zipOutputStream.closeEntry();
                }
                zipOutputStream.finish();
                response.flushBuffer();
            }
        } catch (BusException e) {
            log.info("下载训练代码业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("下载训练代码失败", e);
            throw new RuntimeException("下载训练代码失败");
        }
    }

    private List<TrainCodeFilePart> parseTrainCodeContent(String scoreModelTrainCodeContent) {
        if (scoreModelTrainCodeContent == null || scoreModelTrainCodeContent.trim().isEmpty()) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_CONTENT_EMPTY);
        }

        String normalizedContent = scoreModelTrainCodeContent.replace("\r\n", "\n");
        String[] lineArray = normalizedContent.split("\n", -1);

        List<List<String>> blockLineList = new ArrayList<>();
        List<String> currentBlockLineList = null;

        for (String line : lineArray) {
            if (FILE_SEPARATOR.equals(line.trim())) {
                if (currentBlockLineList != null && !currentBlockLineList.isEmpty()) {
                    blockLineList.add(currentBlockLineList);
                }
                currentBlockLineList = new ArrayList<>();
                continue;
            }

            if (currentBlockLineList != null) {
                currentBlockLineList.add(line);
            }
        }

        if (currentBlockLineList != null && !currentBlockLineList.isEmpty()) {
            blockLineList.add(currentBlockLineList);
        }

        if (blockLineList.isEmpty()) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_FILE_BLOCK_INVALID);
        }

        List<TrainCodeFilePart> trainCodeFilePartList = new ArrayList<>();
        for (List<String> blockLines : blockLineList) {
            if (blockLines.isEmpty()) {
                continue;
            }

            String fileNameLine = blockLines.get(0) == null ? "" : blockLines.get(0).trim();
            if (!fileNameLine.startsWith(FILE_NAME_PREFIX)) {
                throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_FILE_BLOCK_INVALID);
            }

            String fileName = fileNameLine.substring(FILE_NAME_PREFIX.length()).trim();
            if (fileName.isEmpty()) {
                throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_FILE_NAME_EMPTY);
            }

            String zipEntryFileName = fileName.replace("\\", "/");
            if (zipEntryFileName.startsWith("/") || zipEntryFileName.endsWith("/")
                    || zipEntryFileName.contains(":") || zipEntryFileName.indexOf('\0') >= 0) {
                throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_FILE_BLOCK_INVALID);
            }
            String[] fileNamePartArray = zipEntryFileName.split("/");
            for (String fileNamePart : fileNamePartArray) {
                if (fileNamePart.trim().isEmpty() || ".".equals(fileNamePart) || "..".equals(fileNamePart)) {
                    throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_FILE_BLOCK_INVALID);
                }
            }

            StringBuilder fileContentBuilder = new StringBuilder();
            for (int i = 1; i < blockLines.size(); i++) {
                fileContentBuilder.append(blockLines.get(i));
                if (i < blockLines.size() - 1) {
                    fileContentBuilder.append("\n");
                }
            }

            trainCodeFilePartList.add(new TrainCodeFilePart(zipEntryFileName, fileContentBuilder.toString()));
        }

        if (trainCodeFilePartList.isEmpty()) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_TRAIN_CODE_FILE_BLOCK_INVALID);
        }

        return trainCodeFilePartList;
    }

    private static class TrainCodeFilePart {
        private final String fileName;
        private final String content;

        private TrainCodeFilePart(String fileName, String content) {
            this.fileName = fileName;
            this.content = content;
        }

        public String getFileName() {
            return fileName;
        }

        public String getContent() {
            return content;
        }
    }
}
