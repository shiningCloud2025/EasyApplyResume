package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminScoreModelVersionMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminScoreModelVersionForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminScoreModelVersion;
import com.zyh.easyapplyresume.model.query.admin.AdminScoreModelVersionQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminScoreModelVersionInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminScoreModelVersionPageVO;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.AdminScoreModelVersionService;
import com.zyh.easyapplyresume.utils.adminvalidator.AdminScoreModelVersionFormValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 简历评分模型版本服务实现
 *
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
class AdminScoreModelVersionServiceImpl implements AdminScoreModelVersionService {

    private static final Integer MAX_MODEL_COUNT = 10;
    private static final String MODEL_FILE_ROOT_DIR = "mlmodel";

    @Value("${score-model.base-dir:./upload}")
    private String scoreModelBaseDir;

    @Autowired
    private AdminScoreModelVersionMapper adminScoreModelVersionMapper;

    @Override
    public Integer addScoreModelVersion(AdminScoreModelVersionForm form, MultipartFile modelFile) {
        String modelRelativePath = null;
        boolean needDeleteModelFile = false;
        try {
            AdminScoreModelVersionFormValidator.validateForAdd(form, modelFile);
            validateActiveModelUnique(form);

            LambdaQueryWrapper<AdminScoreModelVersion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminScoreModelVersion::getDeleted, 0);
            Long count = adminScoreModelVersionMapper.selectCount(lambdaQueryWrapper);
            if (count != null && count >= MAX_MODEL_COUNT) {
                throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_COUNT_LIMIT);
            }

            modelRelativePath = saveModelFile(form.getScoreModelVersionModelType().trim(), modelFile);
            needDeleteModelFile = true;

            AdminScoreModelVersion adminScoreModelVersion = new AdminScoreModelVersion();
            BeanUtils.copyProperties(form, adminScoreModelVersion);
            adminScoreModelVersion.setScoreModelVersionModelUrl(modelRelativePath);
            adminScoreModelVersion.setScoreModelVersionCreateTime(LocalDateTime.now());
            adminScoreModelVersion.setDeleted(0);

            return adminScoreModelVersionMapper.insert(adminScoreModelVersion);
        } catch (BusException e) {
            if (needDeleteModelFile) {
                deleteModelFileQuietly(modelRelativePath, "新增业务异常后删除模型文件失败");
            }
            log.info("新增模型版本业务异常", e);
            throw e;
        } catch (Exception e) {
            if (needDeleteModelFile) {
                deleteModelFileQuietly(modelRelativePath, "新增失败后删除模型文件失败");
            }
            log.error("新增模型版本失败", e);
            throw new RuntimeException("新增模型版本失败");
        }
    }

    @Override
    public Integer updateScoreModelVersion(AdminScoreModelVersionForm form, MultipartFile modelFile) {
        String newModelRelativePath = null;
        boolean needDeleteNewModelFile = false;
        try {
            AdminScoreModelVersionFormValidator.validateForUpdate(form, modelFile);
            validateActiveModelUnique(form);

            LambdaQueryWrapper<AdminScoreModelVersion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminScoreModelVersion::getScoreModelVersionId, form.getScoreModelVersionId());
            lambdaQueryWrapper.eq(AdminScoreModelVersion::getDeleted, 0);

            AdminScoreModelVersion dbAdminScoreModelVersion = adminScoreModelVersionMapper.selectOne(lambdaQueryWrapper);
            if (dbAdminScoreModelVersion == null) {
                throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_NOT_FOUND);
            }

            if (modelFile != null && !modelFile.isEmpty()) {
                newModelRelativePath = saveModelFile(form.getScoreModelVersionModelType().trim(), modelFile);
                needDeleteNewModelFile = true;
            }

            AdminScoreModelVersion updateAdminScoreModelVersion = new AdminScoreModelVersion();
            BeanUtils.copyProperties(form, updateAdminScoreModelVersion);
            updateAdminScoreModelVersion.setScoreModelVersionModelUrl(
                    newModelRelativePath != null
                            ? newModelRelativePath
                            : dbAdminScoreModelVersion.getScoreModelVersionModelUrl()
            );

            int result = adminScoreModelVersionMapper.updateById(updateAdminScoreModelVersion);
            if (result == 0) {
                throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_NOT_FOUND);
            }

            if (newModelRelativePath != null) {
                deleteModelFile(dbAdminScoreModelVersion.getScoreModelVersionModelUrl());
                needDeleteNewModelFile = false;
            }

            return result;
        } catch (BusException e) {
            if (needDeleteNewModelFile) {
                deleteModelFileQuietly(newModelRelativePath, "修改业务异常后删除新模型文件失败");
            }
            log.info("修改模型版本业务异常", e);
            throw e;
        } catch (Exception e) {
            if (needDeleteNewModelFile) {
                deleteModelFileQuietly(newModelRelativePath, "修改失败后删除新模型文件失败");
            }
            log.error("修改模型版本失败", e);
            throw new RuntimeException("修改模型版本失败");
        }
    }

    @Override
    public Integer deleteScoreModelVersion(Integer scoreModelVersionId) {
        try {
            LambdaQueryWrapper<AdminScoreModelVersion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminScoreModelVersion::getScoreModelVersionId, scoreModelVersionId);
            lambdaQueryWrapper.eq(AdminScoreModelVersion::getDeleted, 0);

            AdminScoreModelVersion adminScoreModelVersion = adminScoreModelVersionMapper.selectOne(lambdaQueryWrapper);
            if (adminScoreModelVersion == null) {
                throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_NOT_FOUND);
            }

            LambdaQueryWrapper<AdminScoreModelVersion> deleteLambdaQueryWrapper = new LambdaQueryWrapper<>();
            deleteLambdaQueryWrapper.eq(AdminScoreModelVersion::getScoreModelVersionId, scoreModelVersionId);
            deleteLambdaQueryWrapper.eq(AdminScoreModelVersion::getDeleted, 0);

            int result = adminScoreModelVersionMapper.delete(deleteLambdaQueryWrapper);
            if (result == 0) {
                throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_NOT_FOUND);
            }

            deleteModelFile(adminScoreModelVersion.getScoreModelVersionModelUrl());
            return result;
        } catch (BusException e) {
            log.info("删除模型版本业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("删除模型版本失败", e);
            throw new RuntimeException("删除模型版本失败");
        }
    }

    @Override
    public AdminScoreModelVersionInfoVO findScoreModelVersionById(Integer scoreModelVersionId) {
        try {
            LambdaQueryWrapper<AdminScoreModelVersion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminScoreModelVersion::getScoreModelVersionId, scoreModelVersionId);
            lambdaQueryWrapper.eq(AdminScoreModelVersion::getDeleted, 0);

            AdminScoreModelVersion adminScoreModelVersion = adminScoreModelVersionMapper.selectOne(lambdaQueryWrapper);
            if (adminScoreModelVersion == null) {
                throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_NOT_FOUND);
            }

            AdminScoreModelVersionInfoVO adminScoreModelVersionInfoVO = new AdminScoreModelVersionInfoVO();
            BeanUtils.copyProperties(adminScoreModelVersion, adminScoreModelVersionInfoVO);
            return adminScoreModelVersionInfoVO;
        } catch (BusException e) {
            log.info("查询模型版本详情业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("查询模型版本详情失败", e);
            throw new RuntimeException("查询模型版本详情失败");
        }
    }

    @Override
    public Page<AdminScoreModelVersionPageVO> findScoreModelVersionByPage(Integer pageNum, Integer pageSize, AdminScoreModelVersionQuery query) {
        try {
            LambdaQueryWrapper<AdminScoreModelVersion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminScoreModelVersion::getDeleted, 0);

            if (query != null) {
                if (query.getScoreModelVersionModelName() != null && !query.getScoreModelVersionModelName().trim().isEmpty()) {
                    lambdaQueryWrapper.like(AdminScoreModelVersion::getScoreModelVersionModelName, query.getScoreModelVersionModelName().trim());
                }
                if (query.getScoreModelVersionModelType() != null && !query.getScoreModelVersionModelType().trim().isEmpty()) {
                    lambdaQueryWrapper.like(AdminScoreModelVersion::getScoreModelVersionModelType, query.getScoreModelVersionModelType().trim());
                }
            }

            lambdaQueryWrapper.orderByDesc(AdminScoreModelVersion::getScoreModelVersionCreateTime);

            Page<AdminScoreModelVersion> page = adminScoreModelVersionMapper.selectPage(
                    new Page<>(pageNum, pageSize),
                    lambdaQueryWrapper
            );

            List<AdminScoreModelVersionPageVO> adminScoreModelVersionPageVOList = page.getRecords().stream().map(item -> {
                AdminScoreModelVersionPageVO adminScoreModelVersionPageVO = new AdminScoreModelVersionPageVO();
                BeanUtils.copyProperties(item, adminScoreModelVersionPageVO);
                return adminScoreModelVersionPageVO;
            }).collect(Collectors.toList());

            Page<AdminScoreModelVersionPageVO> result = new Page<>();
            result.setRecords(adminScoreModelVersionPageVOList);
            result.setCurrent(page.getCurrent());
            result.setSize(page.getSize());
            result.setTotal(page.getTotal());
            result.setPages(page.getPages());

            return result;
        } catch (BusException e) {
            log.info("分页查询模型版本业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("分页查询模型版本失败", e);
            throw new RuntimeException("分页查询模型版本失败");
        }
    }

    @Override
    public List<AdminScoreModelVersionPageVO> findAllScoreModelVersion() {
        try {
            LambdaQueryWrapper<AdminScoreModelVersion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminScoreModelVersion::getDeleted, 0);
            lambdaQueryWrapper.orderByDesc(AdminScoreModelVersion::getScoreModelVersionCreateTime);

            List<AdminScoreModelVersion> adminScoreModelVersionList = adminScoreModelVersionMapper.selectList(lambdaQueryWrapper);

            return adminScoreModelVersionList.stream().map(item -> {
                AdminScoreModelVersionPageVO adminScoreModelVersionPageVO = new AdminScoreModelVersionPageVO();
                BeanUtils.copyProperties(item, adminScoreModelVersionPageVO);
                return adminScoreModelVersionPageVO;
            }).collect(Collectors.toList());
        } catch (BusException e) {
            log.info("查询所有模型版本业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("查询所有模型版本失败", e);
            throw new RuntimeException("查询所有模型版本失败");
        }
    }

    private String saveModelFile(String modelType, MultipartFile modelFile) {
        try {
            Path baseDirPath = Paths.get(scoreModelBaseDir).toAbsolutePath().normalize();
            Files.createDirectories(baseDirPath);

            String originalFilename = modelFile.getOriginalFilename() == null ? "" : modelFile.getOriginalFilename().trim();
            originalFilename = originalFilename.replace("\\", "/");
            int lastSlashIndex = originalFilename.lastIndexOf("/");
            String modelFileName = lastSlashIndex >= 0 ? originalFilename.substring(lastSlashIndex + 1) : originalFilename;

            if (modelFileName.isEmpty()
                    || modelFileName.contains("..")
                    || modelFileName.contains(":")
                    || modelFileName.indexOf('\0') >= 0) {
                throw new RuntimeException("保存模型文件失败");
            }

            String relativeDir = MODEL_FILE_ROOT_DIR
                    + "/"
                    + modelType
                    + "/"
                    + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

            Path targetDirPath = baseDirPath.resolve(relativeDir).normalize();
            if (!targetDirPath.startsWith(baseDirPath)) {
                throw new RuntimeException("保存模型文件失败");
            }
            Files.createDirectories(targetDirPath);

            String saveFileName = modelFileName;
            Path targetPath = targetDirPath.resolve(saveFileName).normalize();
            if (!targetPath.startsWith(baseDirPath)) {
                throw new RuntimeException("保存模型文件失败");
            }

            int suffixIndex = 1;
            int dotIndex = modelFileName.lastIndexOf(".");
            String fileNamePrefix = dotIndex > 0 ? modelFileName.substring(0, dotIndex) : modelFileName;
            String fileNameSuffix = dotIndex > 0 ? modelFileName.substring(dotIndex) : "";

            while (Files.exists(targetPath)) {
                saveFileName = fileNamePrefix + "_" + suffixIndex + fileNameSuffix;
                targetPath = targetDirPath.resolve(saveFileName).normalize();
                if (!targetPath.startsWith(baseDirPath)) {
                    throw new RuntimeException("保存模型文件失败");
                }
                suffixIndex++;
            }

            try (InputStream inputStream = modelFile.getInputStream()) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            return (relativeDir + "/" + saveFileName).replace("\\", "/");
        } catch (Exception e) {
            log.error("保存模型文件失败", e);
            throw new RuntimeException("保存模型文件失败");
        }
    }

    private void deleteModelFile(String modelUrl) {
        if (modelUrl == null || modelUrl.trim().isEmpty()) {
            return;
        }
        try {
            Path baseDirPath = Paths.get(scoreModelBaseDir).toAbsolutePath().normalize();
            Path targetPath = baseDirPath.resolve(modelUrl.trim().replace("\\", "/")).normalize();
            if (!targetPath.startsWith(baseDirPath)) {
                throw new RuntimeException("删除模型文件失败");
            }
            Files.deleteIfExists(targetPath);
        } catch (Exception e) {
            log.error("删除模型文件失败", e);
            throw new RuntimeException("删除模型文件失败");
        }
    }

    private void validateActiveModelUnique(AdminScoreModelVersionForm form) {
        if (form == null) {
            return;
        }

        if (!Integer.valueOf(1).equals(form.getScoreModelVersionIsActive())) {
            return;
        }

        LambdaQueryWrapper<AdminScoreModelVersion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AdminScoreModelVersion::getDeleted, 0);
        lambdaQueryWrapper.eq(AdminScoreModelVersion::getScoreModelVersionIsActive, 1);

        if (form.getScoreModelVersionId() != null) {
            lambdaQueryWrapper.ne(AdminScoreModelVersion::getScoreModelVersionId, form.getScoreModelVersionId());
        }

        Long count = adminScoreModelVersionMapper.selectCount(lambdaQueryWrapper);
        if (count != null && count > 0) {
            throw new BusException(AdminCodeEnum.SCORE_MODEL_VERSION_ACTIVE_DUPLICATE);
        }
    }

    private void deleteModelFileQuietly(String modelUrl, String logMessage) {
        if (modelUrl == null || modelUrl.trim().isEmpty()) {
            return;
        }
        try {
            deleteModelFile(modelUrl);
        } catch (Exception e) {
            log.error(logMessage, e);
        }
    }
}
