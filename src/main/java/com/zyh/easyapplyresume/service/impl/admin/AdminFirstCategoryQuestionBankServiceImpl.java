package com.zyh.easyapplyresume.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.user.UserFirstCategoryQuestionBankMapper;
import com.zyh.easyapplyresume.model.pojo.user.UserFirstCategoryQuestionBank;
import com.zyh.easyapplyresume.model.query.user.UserFirstCategoryQuestionBankQuery;
import com.zyh.easyapplyresume.model.vo.user.UserFirstCategoryQuestionBankInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserFirstCategoryQuestionBankPageVO;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.admin.AdminFirstCategoryQuestionBankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户-笔试题目管理实现
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class AdminFirstCategoryQuestionBankServiceImpl implements AdminFirstCategoryQuestionBankService {

    @Autowired
    private UserFirstCategoryQuestionBankMapper firstCategoryQuestionBankMapper;

    @Override
    public Integer updateFirstCategoryQuestionBankFirstCategoryNameByFirstCategoryId(Integer questionFirstCategoryId,
                                                                                     String questionFirstCategoryName) {
        try {
            if (questionFirstCategoryId == null) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_FIRST_CATEGORY_ID_EMPTY);
            }
            if (questionFirstCategoryName == null || questionFirstCategoryName.trim().isEmpty()) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_FIRST_CATEGORY_NAME_EMPTY);
            }
            if (questionFirstCategoryName.trim().length() > 20) {
                throw new BusException(AdminCodeEnum.USER_FIRST_CATEGORY_QUESTION_BANK_FIRST_CATEGORY_NAME_TOO_LONG);
            }

            LambdaUpdateWrapper<UserFirstCategoryQuestionBank> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionFirstCategoryId, questionFirstCategoryId);
            lambdaUpdateWrapper.set(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionFirstCategoryName, questionFirstCategoryName.trim());
            lambdaUpdateWrapper.set(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserUpdateTime, LocalDateTime.now());

            return firstCategoryQuestionBankMapper.update(null, lambdaUpdateWrapper);
        } catch (BusException e) {
            log.info("按大类id同步用户题目大类名称业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("按大类id同步用户题目大类名称失败", e);
            throw new RuntimeException("按大类id同步用户题目大类名称失败");
        }
    }

    @Override
    public Integer updateFirstCategoryQuestionBankCategoryInfoBySecondCategoryId(Integer questionSecondCategoryId,
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
                throw new BusException(AdminCodeEnum.USER_FIRST_CATEGORY_QUESTION_BANK_FIRST_CATEGORY_NAME_TOO_LONG);
            }
            if (questionSecondCategoryName == null || questionSecondCategoryName.trim().isEmpty()) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_SECOND_CATEGORY_NAME_EMPTY);
            }
            if (questionSecondCategoryName.trim().length() > 20) {
                throw new BusException(AdminCodeEnum.USER_FIRST_CATEGORY_QUESTION_BANK_SECOND_CATEGORY_NAME_TOO_LONG);
            }

            LambdaUpdateWrapper<UserFirstCategoryQuestionBank> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionSecondCategoryId, questionSecondCategoryId);
            lambdaUpdateWrapper.set(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionFirstCategoryId, questionFirstCategoryId);
            lambdaUpdateWrapper.set(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionFirstCategoryName, questionFirstCategoryName.trim());
            lambdaUpdateWrapper.set(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionSecondCategoryName, questionSecondCategoryName.trim());
            lambdaUpdateWrapper.set(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserUpdateTime, LocalDateTime.now());

            return firstCategoryQuestionBankMapper.update(null, lambdaUpdateWrapper);
        } catch (BusException e) {
            log.info("按小类id同步用户题目分类信息业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("按小类id同步用户题目分类信息失败", e);
            throw new RuntimeException("按小类id同步用户题目分类信息失败");
        }
    }

    @Override
    public Integer deleteFirstCategoryQuestionBankBySecondCategoryId(Integer questionSecondCategoryId) {
        try {
            if (questionSecondCategoryId == null) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_SECOND_CATEGORY_ID_EMPTY);
            }

            LambdaQueryWrapper<UserFirstCategoryQuestionBank> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionSecondCategoryId, questionSecondCategoryId);

            return firstCategoryQuestionBankMapper.delete(lambdaQueryWrapper);
        } catch (BusException e) {
            log.info("按小类id删除用户题目记录业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("按小类id删除用户题目记录失败", e);
            throw new RuntimeException("按小类id删除用户题目记录失败");
        }
    }

    @Override
    public Integer deleteFirstCategoryQuestionBankByFirstCategoryId(Integer questionFirstCategoryId) {
        try {
            if (questionFirstCategoryId == null) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_FIRST_CATEGORY_ID_EMPTY);
            }

            LambdaQueryWrapper<UserFirstCategoryQuestionBank> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionFirstCategoryId, questionFirstCategoryId);

            return firstCategoryQuestionBankMapper.delete(lambdaQueryWrapper);
        } catch (BusException e) {
            log.info("按大类id删除用户题目记录业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("按大类id删除用户题目记录失败", e);
            throw new RuntimeException("按大类id删除用户题目记录失败");
        }
    }

    @Override
    public Integer updateFirstCategoryQuestionBankCategoryInfo(Integer questionBankId,
                                                               Integer questionFirstCategoryId,
                                                               String questionFirstCategoryName,
                                                               Integer questionSecondCategoryId,
                                                               String questionSecondCategoryName) {
        try {
            if (questionBankId == null) {
                throw new BusException(AdminCodeEnum.USER_FIRST_CATEGORY_QUESTION_BANK_ID_EMPTY);
            }
            if (questionFirstCategoryId == null) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_FIRST_CATEGORY_ID_EMPTY);
            }
            if (questionFirstCategoryName == null || questionFirstCategoryName.trim().isEmpty()) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_FIRST_CATEGORY_NAME_EMPTY);
            }
            if (questionFirstCategoryName.trim().length() > 20) {
                throw new BusException(AdminCodeEnum.USER_FIRST_CATEGORY_QUESTION_BANK_FIRST_CATEGORY_NAME_TOO_LONG);
            }
            if (questionSecondCategoryId == null) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_SECOND_CATEGORY_ID_EMPTY);
            }
            if (questionSecondCategoryName == null || questionSecondCategoryName.trim().isEmpty()) {
                throw new BusException(AdminCodeEnum.QUESTION_BANK_SECOND_CATEGORY_NAME_EMPTY);
            }
            if (questionSecondCategoryName.trim().length() > 20) {
                throw new BusException(AdminCodeEnum.USER_FIRST_CATEGORY_QUESTION_BANK_SECOND_CATEGORY_NAME_TOO_LONG);
            }

            LambdaUpdateWrapper<UserFirstCategoryQuestionBank> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionBankId, questionBankId);
            lambdaUpdateWrapper.set(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionFirstCategoryId, questionFirstCategoryId);
            lambdaUpdateWrapper.set(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionFirstCategoryName, questionFirstCategoryName == null ? null : questionFirstCategoryName.trim());
            lambdaUpdateWrapper.set(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionSecondCategoryId, questionSecondCategoryId);
            lambdaUpdateWrapper.set(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionSecondCategoryName, questionSecondCategoryName == null ? null : questionSecondCategoryName.trim());

            return firstCategoryQuestionBankMapper.update(null, lambdaUpdateWrapper);
        } catch (BusException e) {
            log.info("同步用户-笔试题目分类信息业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("同步用户-笔试题目分类信息失败", e);
            throw new RuntimeException("同步用户-笔试题目分类信息失败");
        }
    }

    @Override
    public Integer deleteFirstCategoryQuestionBank(Integer userId, Integer questionBankId) {
        try {
            if (userId == null) {
                throw new BusException(AdminCodeEnum.USER_FIRST_CATEGORY_QUESTION_BANK_USER_ID_EMPTY);
            }
            if (questionBankId == null) {
                throw new BusException(AdminCodeEnum.USER_FIRST_CATEGORY_QUESTION_BANK_ID_EMPTY);
            }

            LambdaQueryWrapper<UserFirstCategoryQuestionBank> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserUserId, userId);
            lambdaQueryWrapper.eq(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionBankId, questionBankId);

            int deleteCount = firstCategoryQuestionBankMapper.delete(lambdaQueryWrapper);
            if (deleteCount <= 0) {
                throw new BusException(AdminCodeEnum.USER_FIRST_CATEGORY_QUESTION_BANK_NOT_FOUND);
            }

            return deleteCount;
        } catch (BusException e) {
            log.info("删除用户-笔试题目记录业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("删除用户-笔试题目记录失败", e);
            throw new RuntimeException("删除用户-笔试题目记录失败");
        }
    }

    @Override
    public UserFirstCategoryQuestionBankInfoVO findFirstCategoryQuestionBankById(Integer userId, Integer questionBankId) {
        try {
            if (userId == null) {
                throw new BusException(AdminCodeEnum.USER_FIRST_CATEGORY_QUESTION_BANK_USER_ID_EMPTY);
            }
            if (questionBankId == null) {
                throw new BusException(AdminCodeEnum.USER_FIRST_CATEGORY_QUESTION_BANK_ID_EMPTY);
            }

            LambdaQueryWrapper<UserFirstCategoryQuestionBank> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserUserId, userId);
            lambdaQueryWrapper.eq(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionBankId, questionBankId);

            UserFirstCategoryQuestionBank questionBankUserRecord = firstCategoryQuestionBankMapper.selectOne(lambdaQueryWrapper);
            if (questionBankUserRecord == null) {
                throw new BusException(AdminCodeEnum.USER_FIRST_CATEGORY_QUESTION_BANK_NOT_FOUND);
            }

            UserFirstCategoryQuestionBankInfoVO infoVO = new UserFirstCategoryQuestionBankInfoVO();
            BeanUtils.copyProperties(questionBankUserRecord, infoVO);
            return infoVO;
        } catch (BusException e) {
            log.info("查询用户-笔试题目详情业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("查询用户-笔试题目详情失败", e);
            throw new RuntimeException("查询用户-笔试题目详情失败");
        }
    }

    @Override
    public Page<UserFirstCategoryQuestionBankPageVO> findFirstCategoryQuestionBankByPage(Integer pageNum, Integer pageSize, UserFirstCategoryQuestionBankQuery query) {
        try {
            LambdaQueryWrapper<UserFirstCategoryQuestionBank> lambdaQueryWrapper = new LambdaQueryWrapper<>();

            if (query != null) {
                if (query.getUserId() != null) {
                    lambdaQueryWrapper.eq(
                            UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserUserId,
                            query.getUserId()
                    );
                }

                if (query.getQuestionFirstCategoryName() != null
                        && !query.getQuestionFirstCategoryName().trim().isEmpty()) {
                    lambdaQueryWrapper.like(
                            UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionFirstCategoryName,
                            query.getQuestionFirstCategoryName().trim()
                    );
                }

                if (query.getQuestionSecondCategoryName() != null
                        && !query.getQuestionSecondCategoryName().trim().isEmpty()) {
                    lambdaQueryWrapper.like(
                            UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionSecondCategoryName,
                            query.getQuestionSecondCategoryName().trim()
                    );
                }
            }

            lambdaQueryWrapper.orderByDesc(
                    UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserCreateTime
            );

            Page<UserFirstCategoryQuestionBank> page = firstCategoryQuestionBankMapper.selectPage(
                    new Page<>(pageNum, pageSize),
                    lambdaQueryWrapper
            );

            List<UserFirstCategoryQuestionBankPageVO> pageVOList = page.getRecords().stream()
                    .map(questionBankUserRecord -> {
                        UserFirstCategoryQuestionBankPageVO pageVO = new UserFirstCategoryQuestionBankPageVO();
                        BeanUtils.copyProperties(questionBankUserRecord, pageVO);
                        return pageVO;
                    })
                    .collect(Collectors.toList());

            Page<UserFirstCategoryQuestionBankPageVO> result = new Page<>();
            result.setRecords(pageVOList);
            result.setCurrent(page.getCurrent());
            result.setSize(page.getSize());
            result.setTotal(page.getTotal());
            result.setPages(page.getPages());

            return result;
        } catch (BusException e) {
            log.info("分页查询用户-笔试题目业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("分页查询用户-笔试题目失败", e);
            throw new RuntimeException("分页查询用户-笔试题目失败");
        }
    }
}
