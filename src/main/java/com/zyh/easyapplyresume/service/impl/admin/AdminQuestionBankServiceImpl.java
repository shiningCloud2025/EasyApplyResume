package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminQuestionBankMapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserFirstCategoryQuestionBankMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminQuestionBankForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminQuestionBank;
import com.zyh.easyapplyresume.model.pojo.user.UserFirstCategoryQuestionBank;
import com.zyh.easyapplyresume.model.query.admin.AdminQuestionBankQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionBankInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminQuestionBankPageVO;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.AdminFirstCategoryQuestionBankService;
import com.zyh.easyapplyresume.service.admin.AdminQuestionBankService;
import com.zyh.easyapplyresume.utils.adminvalidator.AdminQuestionBankFormValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 题库题目管理实现
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminQuestionBankServiceImpl implements AdminQuestionBankService {

    @Autowired
    private AdminQuestionBankMapper adminQuestionBankMapper;

    @Autowired
    private AdminFirstCategoryQuestionBankService adminFirstCategoryQuestionBankService;

    @Autowired
    private UserFirstCategoryQuestionBankMapper userFirstCategoryQuestionBankMapper;

    @Override
    public Integer updateQuestionBankFirstCategoryNameByFirstCategoryId(Integer questionFirstCategoryId,
                                                                        String questionFirstCategoryName) {
        try {
            if (questionFirstCategoryId == null) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_FIRST_CATEGORY_ID_EMPTY);
            }
            if (questionFirstCategoryName == null || questionFirstCategoryName.trim().isEmpty()) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_FIRST_CATEGORY_NAME_EMPTY);
            }
            if (questionFirstCategoryName.trim().length() > 20) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_FIRST_CATEGORY_NAME_TOO_LONG);
            }

            LambdaUpdateWrapper<AdminQuestionBank> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(AdminQuestionBank::getQuestionFirstCategoryId, questionFirstCategoryId);
            lambdaUpdateWrapper.eq(AdminQuestionBank::getDeleted, 0);
            lambdaUpdateWrapper.set(AdminQuestionBank::getQuestionFirstCategoryName, questionFirstCategoryName.trim());
            lambdaUpdateWrapper.set(AdminQuestionBank::getQuestionBankUpdateTime, LocalDateTime.now());

            return adminQuestionBankMapper.update(null, lambdaUpdateWrapper);
        } catch (BusException e) {
            log.info("按大类id同步题库题目大类名称业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("按大类id同步题库题目大类名称失败", e);
            throw new RuntimeException("按大类id同步题库题目大类名称失败");
        }
    }

    @Override
    public Integer updateQuestionBankSecondCategoryInfoBySecondCategoryId(Integer questionSecondCategoryId,
                                                                          Integer questionFirstCategoryId,
                                                                          String questionFirstCategoryName,
                                                                          String questionSecondCategoryName) {
        try {
            if (questionSecondCategoryId == null) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_SECOND_CATEGORY_ID_EMPTY);
            }
            if (questionFirstCategoryId == null) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_FIRST_CATEGORY_ID_EMPTY);
            }
            if (questionFirstCategoryName == null || questionFirstCategoryName.trim().isEmpty()) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_FIRST_CATEGORY_NAME_EMPTY);
            }
            if (questionFirstCategoryName.trim().length() > 20) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_FIRST_CATEGORY_NAME_TOO_LONG);
            }
            if (questionSecondCategoryName == null || questionSecondCategoryName.trim().isEmpty()) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_SECOND_CATEGORY_NAME_EMPTY);
            }
            if (questionSecondCategoryName.trim().length() > 20) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_SECOND_CATEGORY_NAME_TOO_LONG);
            }

            LambdaUpdateWrapper<AdminQuestionBank> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(AdminQuestionBank::getQuestionSecondCategoryId, questionSecondCategoryId);
            lambdaUpdateWrapper.eq(AdminQuestionBank::getDeleted, 0);
            lambdaUpdateWrapper.set(AdminQuestionBank::getQuestionFirstCategoryId, questionFirstCategoryId);
            lambdaUpdateWrapper.set(AdminQuestionBank::getQuestionFirstCategoryName, questionFirstCategoryName.trim());
            lambdaUpdateWrapper.set(AdminQuestionBank::getQuestionSecondCategoryName, questionSecondCategoryName.trim());
            lambdaUpdateWrapper.set(AdminQuestionBank::getQuestionBankUpdateTime, LocalDateTime.now());

            return adminQuestionBankMapper.update(null, lambdaUpdateWrapper);
        } catch (BusException e) {
            log.info("按小类id同步题库题目分类信息业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("按小类id同步题库题目分类信息失败", e);
            throw new RuntimeException("按小类id同步题库题目分类信息失败");
        }
    }

    @Override
    public Integer deleteQuestionBankBySecondCategoryId(Integer questionSecondCategoryId) {
        try {
            if (questionSecondCategoryId == null) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_SECOND_CATEGORY_ID_EMPTY);
            }

            LambdaUpdateWrapper<AdminQuestionBank> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(AdminQuestionBank::getQuestionSecondCategoryId, questionSecondCategoryId);
            lambdaUpdateWrapper.eq(AdminQuestionBank::getDeleted, 0);
            lambdaUpdateWrapper.set(AdminQuestionBank::getDeleted, 1);
            lambdaUpdateWrapper.set(AdminQuestionBank::getQuestionBankUpdateTime, LocalDateTime.now());

            return adminQuestionBankMapper.update(null, lambdaUpdateWrapper);
        } catch (BusException e) {
            log.info("按小类id删除题库题目业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("按小类id删除题库题目失败", e);
            throw new RuntimeException("按小类id删除题库题目失败");
        }
    }

    @Override
    public Integer deleteQuestionBankByFirstCategoryId(Integer questionFirstCategoryId) {
        try {
            if (questionFirstCategoryId == null) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_FIRST_CATEGORY_ID_EMPTY);
            }

            LambdaUpdateWrapper<AdminQuestionBank> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(AdminQuestionBank::getQuestionFirstCategoryId, questionFirstCategoryId);
            lambdaUpdateWrapper.eq(AdminQuestionBank::getDeleted, 0);
            lambdaUpdateWrapper.set(AdminQuestionBank::getDeleted, 1);
            lambdaUpdateWrapper.set(AdminQuestionBank::getQuestionBankUpdateTime, LocalDateTime.now());

            return adminQuestionBankMapper.update(null, lambdaUpdateWrapper);
        } catch (BusException e) {
            log.info("按大类id删除题库题目业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("按大类id删除题库题目失败", e);
            throw new RuntimeException("按大类id删除题库题目失败");
        }
    }

    @Override
    public Integer addQuestionBank(AdminQuestionBankForm form) {
        try {
            AdminQuestionBankFormValidator.validateForAdd(form);
            fillDefaultStringValue(form);

            AdminQuestionBank questionBank = new AdminQuestionBank();
            BeanUtils.copyProperties(form, questionBank);
            questionBank.setQuestionBankCreateTime(LocalDateTime.now());
            questionBank.setQuestionBankUpdateTime(LocalDateTime.now());
            questionBank.setDeleted(0);

            adminQuestionBankMapper.insert(questionBank);
            return questionBank.getQuestionBankId();
        } catch (BusException e) {
            log.info("新增题库题目业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("新增题库题目失败", e);
            throw new RuntimeException("新增题库题目失败");
        }
    }

    @Override
    public Integer updateQuestionBank(AdminQuestionBankForm form) {
        try {
            AdminQuestionBankFormValidator.validateForUpdate(form);
            fillDefaultStringValue(form);

            LambdaQueryWrapper<AdminQuestionBank> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminQuestionBank::getQuestionBankId, form.getQuestionBankId());
            lambdaQueryWrapper.eq(AdminQuestionBank::getDeleted, 0);

            AdminQuestionBank dbQuestionBank = adminQuestionBankMapper.selectOne(lambdaQueryWrapper);
            if (dbQuestionBank == null) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_NOT_FOUND);
            }

            boolean needSyncCategory = !Objects.equals(dbQuestionBank.getQuestionFirstCategoryId(), form.getQuestionFirstCategoryId())
                    || !Objects.equals(dbQuestionBank.getQuestionFirstCategoryName(), form.getQuestionFirstCategoryName())
                    || !Objects.equals(dbQuestionBank.getQuestionSecondCategoryId(), form.getQuestionSecondCategoryId())
                    || !Objects.equals(dbQuestionBank.getQuestionSecondCategoryName(), form.getQuestionSecondCategoryName());

            AdminQuestionBank questionBank = new AdminQuestionBank();
            BeanUtils.copyProperties(form, questionBank);
            questionBank.setQuestionBankUpdateTime(LocalDateTime.now());

            int updateCount = adminQuestionBankMapper.updateById(questionBank);
            if (updateCount <= 0) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_UPDATE_FAIL);
            }

            if (needSyncCategory) {
                adminFirstCategoryQuestionBankService.updateFirstCategoryQuestionBankCategoryInfo(
                        form.getQuestionBankId(),
                        form.getQuestionFirstCategoryId(),
                        form.getQuestionFirstCategoryName(),
                        form.getQuestionSecondCategoryId(),
                        form.getQuestionSecondCategoryName()
                );
            }

            return updateCount;
        } catch (BusException e) {
            log.info("修改题库题目业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("修改题库题目失败", e);
            throw new RuntimeException("修改题库题目失败");
        }
    }

    @Override
    public Integer deleteQuestionBank(Integer questionBankId) {
        try {
            if (questionBankId == null) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_ID_EMPTY);
            }

            LambdaQueryWrapper<AdminQuestionBank> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminQuestionBank::getQuestionBankId, questionBankId);
            lambdaQueryWrapper.eq(AdminQuestionBank::getDeleted, 0);

            AdminQuestionBank dbQuestionBank = adminQuestionBankMapper.selectOne(lambdaQueryWrapper);
            if (dbQuestionBank == null) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_NOT_FOUND);
            }

            AdminQuestionBank questionBank = new AdminQuestionBank();
            questionBank.setQuestionBankId(questionBankId);
            questionBank.setDeleted(1);
            questionBank.setQuestionBankUpdateTime(LocalDateTime.now());

            int updateCount = adminQuestionBankMapper.updateById(questionBank);
            if (updateCount <= 0) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_DELETE_FAIL);
            }

            LambdaQueryWrapper<UserFirstCategoryQuestionBank> userQuestionBankWrapper = new LambdaQueryWrapper<>();
            userQuestionBankWrapper.eq(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionBankId, questionBankId);
            userFirstCategoryQuestionBankMapper.delete(userQuestionBankWrapper);

            return updateCount;
        } catch (BusException e) {
            log.info("删除题库题目业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("删除题库题目失败", e);
            throw new RuntimeException("删除题库题目失败");
        }
    }

    @Override
    public AdminQuestionBankInfoVO findQuestionBankById(Integer questionBankId) {
        try {
            LambdaQueryWrapper<AdminQuestionBank> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminQuestionBank::getQuestionBankId, questionBankId);
            lambdaQueryWrapper.eq(AdminQuestionBank::getDeleted, 0);

            AdminQuestionBank questionBank = adminQuestionBankMapper.selectOne(lambdaQueryWrapper);
            if (questionBank == null || (questionBank.getDeleted() != null && questionBank.getDeleted() == 1)) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_NOT_FOUND);
            }

            AdminQuestionBankInfoVO vo = new AdminQuestionBankInfoVO();
            BeanUtils.copyProperties(questionBank, vo);
            return vo;
        } catch (BusException e) {
            log.info("查询题库题目详情业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("查询题库题目详情失败", e);
            throw new RuntimeException("查询题库题目详情失败");
        }
    }

    @Override
    public Page<AdminQuestionBankPageVO> findQuestionBankByPage(Integer pageNum, Integer pageSize, AdminQuestionBankQuery query) {
        try {
            LambdaQueryWrapper<AdminQuestionBank> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminQuestionBank::getDeleted, 0);

            if (query != null) {
                if (query.getQuestionBankDescription() != null && !query.getQuestionBankDescription().trim().isEmpty()) {
                    lambdaQueryWrapper.like(AdminQuestionBank::getQuestionBankDescription, query.getQuestionBankDescription().trim());
                }
                if (query.getQuestionBankType() != null) {
                    lambdaQueryWrapper.eq(AdminQuestionBank::getQuestionBankType, query.getQuestionBankType());
                }
                if (query.getQuestionFirstCategoryId() != null) {
                    lambdaQueryWrapper.eq(AdminQuestionBank::getQuestionFirstCategoryId, query.getQuestionFirstCategoryId());
                }
                if (query.getQuestionSecondCategoryId() != null) {
                    lambdaQueryWrapper.eq(AdminQuestionBank::getQuestionSecondCategoryId, query.getQuestionSecondCategoryId());
                }
                if (query.getQuestionBankDifficulty() != null) {
                    lambdaQueryWrapper.eq(AdminQuestionBank::getQuestionBankDifficulty, query.getQuestionBankDifficulty());
                }
                if (query.getQuestionBankState() != null) {
                    lambdaQueryWrapper.eq(AdminQuestionBank::getQuestionBankState, query.getQuestionBankState());
                }
            }

            lambdaQueryWrapper.orderByDesc(AdminQuestionBank::getQuestionBankCreateTime);

            Page<AdminQuestionBank> page = adminQuestionBankMapper.selectPage(
                    new Page<>(pageNum, pageSize),
                    lambdaQueryWrapper
            );

            List<AdminQuestionBankPageVO> voList = page.getRecords().stream()
                    .map(item -> {
                        AdminQuestionBankPageVO vo = new AdminQuestionBankPageVO();
                        BeanUtils.copyProperties(item, vo);
                        return vo;
                    })
                    .collect(Collectors.toList());

            Page<AdminQuestionBankPageVO> result = new Page<>();
            result.setRecords(voList);
            result.setCurrent(page.getCurrent());
            result.setSize(page.getSize());
            result.setTotal(page.getTotal());
            result.setPages(page.getPages());

            return result;
        } catch (BusException e) {
            log.info("分页查询题库题目业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("分页查询题库题目失败", e);
            throw new RuntimeException("分页查询题库题目失败");
        }
    }

    /**
     * 统一把允许为空的字符串字段兜底成空字符串
     */
    private void fillDefaultStringValue(AdminQuestionBankForm form) {
        if (form.getQuestionBankDescription() == null) {
            form.setQuestionBankDescription("");
        }
        if (form.getQuestionBankOptionA() == null) {
            form.setQuestionBankOptionA("");
        }
        if (form.getQuestionBankOptionB() == null) {
            form.setQuestionBankOptionB("");
        }
        if (form.getQuestionBankOptionC() == null) {
            form.setQuestionBankOptionC("");
        }
        if (form.getQuestionBankOptionD() == null) {
            form.setQuestionBankOptionD("");
        }
        if (form.getQuestionBankCorrectAnswer() == null) {
            form.setQuestionBankCorrectAnswer("");
        }
        if (form.getQuestionBankImage() == null) {
            form.setQuestionBankImage("");
        }
        if (form.getQuestionBankCode() == null) {
            form.setQuestionBankCode("");
        }
        if (form.getQuestionFirstCategoryName() == null) {
            form.setQuestionFirstCategoryName("");
        }
        if (form.getQuestionSecondCategoryName() == null) {
            form.setQuestionSecondCategoryName("");
        }
        if (form.getQuestionBankReferenceAnswer() == null) {
            form.setQuestionBankReferenceAnswer("");
        }
        if (form.getQuestionBankAnalysis() == null) {
            form.setQuestionBankAnalysis("");
        }
    }
}
