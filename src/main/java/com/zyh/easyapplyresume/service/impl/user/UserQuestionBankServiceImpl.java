package com.zyh.easyapplyresume.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.UserCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.user.UserFirstCategoryQuestionBankMapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserQuestionBankMapper;
import com.zyh.easyapplyresume.model.form.user.UserQuestionBankAnswerForm;
import com.zyh.easyapplyresume.model.pojo.admin.AdminQuestionBank;
import com.zyh.easyapplyresume.model.pojo.user.UserFirstCategoryQuestionBank;
import com.zyh.easyapplyresume.model.query.user.UserQuestionBankQuery;
import com.zyh.easyapplyresume.model.vo.user.UserQuestionBankAnswerResultVO;
import com.zyh.easyapplyresume.model.vo.user.UserQuestionBankInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserQuestionBankPageVO;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.user.UserQuestionBankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用户端题库题目管理实现
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class UserQuestionBankServiceImpl implements UserQuestionBankService {

    @Autowired
    private UserQuestionBankMapper userQuestionBankMapper;

    @Autowired
    private UserFirstCategoryQuestionBankMapper userFirstCategoryQuestionBankMapper;

    @Override
    public Page<UserQuestionBankPageVO> findQuestionBankByPage(Integer userId, Integer pageNum, Integer pageSize, UserQuestionBankQuery query) {
        try {
            if (userId == null) {
                throw new BusException(UserCodeEnum.USER_QUESTION_BANK_USER_ID_EMPTY);
            }

            IPage<UserQuestionBankPageVO> userQuestionBankIPage = userQuestionBankMapper.selectQuestionBankByPage(
                    new Page<>(pageNum, pageSize),
                    userId,
                    query
            );

            Page<UserQuestionBankPageVO> result = new Page<>();
            result.setRecords(userQuestionBankIPage.getRecords());
            result.setCurrent(userQuestionBankIPage.getCurrent());
            result.setSize(userQuestionBankIPage.getSize());
            result.setTotal(userQuestionBankIPage.getTotal());
            result.setPages(userQuestionBankIPage.getPages());

            return result;
        } catch (BusException e) {
            log.info("分页查询用户端题库题目业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("分页查询用户端题库题目失败", e);
            throw new BusException(UserCodeEnum.USER_QUESTION_BANK_PAGE_FAIL);
        }
    }

    @Override
    public UserQuestionBankInfoVO findQuestionBankById(Integer questionBankId) {
        try {
            if (questionBankId == null) {
                throw new BusException(UserCodeEnum.USER_QUESTION_BANK_ID_EMPTY);
            }

            LambdaQueryWrapper<AdminQuestionBank> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminQuestionBank::getQuestionBankId, questionBankId);
            lambdaQueryWrapper.eq(AdminQuestionBank::getDeleted, 0);
            lambdaQueryWrapper.eq(AdminQuestionBank::getQuestionBankState, 1);

            AdminQuestionBank questionBank = userQuestionBankMapper.selectOne(lambdaQueryWrapper);
            if (questionBank == null) {
                throw new BusException(UserCodeEnum.USER_QUESTION_BANK_NOT_FOUND);
            }

            UserQuestionBankInfoVO userQuestionBankInfoVO = new UserQuestionBankInfoVO();
            BeanUtils.copyProperties(questionBank, userQuestionBankInfoVO);
            return userQuestionBankInfoVO;
        } catch (BusException e) {
            log.info("查询用户端题库题目详情业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("查询用户端题库题目详情失败", e);
            throw new BusException(UserCodeEnum.USER_QUESTION_BANK_INFO_FAIL);
        }
    }

    @Override
    public UserQuestionBankAnswerResultVO submitQuestionBankAnswer(UserQuestionBankAnswerForm userQuestionBankAnswerForm) {
        try {
            if (userQuestionBankAnswerForm == null) {
                throw new BusException(UserCodeEnum.USER_QUESTION_BANK_ANSWER_FORM_EMPTY);
            }
            if (userQuestionBankAnswerForm.getUserId() == null) {
                throw new BusException(UserCodeEnum.USER_QUESTION_BANK_USER_ID_EMPTY);
            }
            if (userQuestionBankAnswerForm.getQuestionBankId() == null) {
                throw new BusException(UserCodeEnum.USER_QUESTION_BANK_ID_EMPTY);
            }
            if (userQuestionBankAnswerForm.getUserAnswers() == null || userQuestionBankAnswerForm.getUserAnswers().isEmpty()) {
                throw new BusException(UserCodeEnum.USER_QUESTION_BANK_ANSWERS_EMPTY);
            }

            LambdaQueryWrapper<AdminQuestionBank> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AdminQuestionBank::getQuestionBankId, userQuestionBankAnswerForm.getQuestionBankId());
            lambdaQueryWrapper.eq(AdminQuestionBank::getDeleted, 0);
            lambdaQueryWrapper.eq(AdminQuestionBank::getQuestionBankState, 1);

            AdminQuestionBank questionBank = userQuestionBankMapper.selectOne(lambdaQueryWrapper);
            if (questionBank == null) {
                throw new BusException(UserCodeEnum.USER_QUESTION_BANK_NOT_FOUND);
            }

            boolean correct = isAnswerCorrect(questionBank, userQuestionBankAnswerForm.getUserAnswers());
            Integer answerStatus = correct ? 2 : 1;

            saveUserQuestionAnswerStatus(userQuestionBankAnswerForm.getUserId(), questionBank, answerStatus);

            UserQuestionBankAnswerResultVO resultVO = new UserQuestionBankAnswerResultVO();
            resultVO.setQuestionBankId(questionBank.getQuestionBankId());
            resultVO.setCorrect(correct);
            if (questionBank.getQuestionBankType() != null && questionBank.getQuestionBankType() == 5) {
                resultVO.setReferenceAnswer(questionBank.getQuestionBankReferenceAnswer());
            } else {
                resultVO.setCorrectAnswer(questionBank.getQuestionBankCorrectAnswer());
            }

            resultVO.setQuestionBankAnalysis(questionBank.getQuestionBankAnalysis());
            return resultVO;
        } catch (BusException e) {
            log.info("提交用户端题库题目答案业务异常", e);
            throw e;
        } catch (Exception e) {
            log.error("提交用户端题库题目答案失败", e);
            throw new BusException(UserCodeEnum.USER_QUESTION_BANK_SUBMIT_FAIL);
        }
    }

    private boolean isAnswerCorrect(AdminQuestionBank questionBank, List<String> userAnswers) {
        Integer questionBankType = questionBank.getQuestionBankType();
        if (questionBankType == null) {
            return false;
        }

        if (questionBankType == 1 || questionBankType == 3 || questionBankType == 4) {
            String correctAnswer = normalizeSingleAnswer(questionBank.getQuestionBankCorrectAnswer());
            String userAnswer = normalizeSingleAnswer(userAnswers.get(0));
            return correctAnswer.equals(userAnswer);
        }

        if (questionBankType == 2) {
            String correctAnswer = normalizeMultiAnswer(questionBank.getQuestionBankCorrectAnswer());
            String userAnswer = normalizeMultiAnswer(userAnswers);
            return correctAnswer.equals(userAnswer);
        }

        if (questionBankType == 5) {
            return hasAnyAnswer(userAnswers);
        }

        return false;
    }

    private void saveUserQuestionAnswerStatus(Integer userId, AdminQuestionBank questionBank, Integer answerStatus) {
        LambdaQueryWrapper<UserFirstCategoryQuestionBank> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserUserId, userId);
        lambdaQueryWrapper.eq(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionBankId, questionBank.getQuestionBankId());

        UserFirstCategoryQuestionBank questionBankUserRecord = userFirstCategoryQuestionBankMapper.selectOne(lambdaQueryWrapper);

        if (questionBankUserRecord == null) {
            UserFirstCategoryQuestionBank userQuestionBankRecord = new UserFirstCategoryQuestionBank();
            userQuestionBankRecord.setFirstCategoryQuestionBankUserUserId(userId);
            userQuestionBankRecord.setFirstCategoryQuestionBankUserQuestionBankId(questionBank.getQuestionBankId());
            userQuestionBankRecord.setFirstCategoryQuestionBankUserQuestionFirstCategoryId(questionBank.getQuestionFirstCategoryId());
            userQuestionBankRecord.setFirstCategoryQuestionBankUserQuestionFirstCategoryName(questionBank.getQuestionFirstCategoryName());
            userQuestionBankRecord.setFirstCategoryQuestionBankUserQuestionSecondCategoryId(questionBank.getQuestionSecondCategoryId());
            userQuestionBankRecord.setFirstCategoryQuestionBankUserQuestionSecondCategoryName(questionBank.getQuestionSecondCategoryName());
            userQuestionBankRecord.setFirstCategoryQuestionBankUserAnswerStatus(answerStatus);
            userQuestionBankRecord.setFirstCategoryQuestionBankUserCreateTime(LocalDateTime.now());
            userQuestionBankRecord.setFirstCategoryQuestionBankUserUpdateTime(LocalDateTime.now());
            userFirstCategoryQuestionBankMapper.insert(userQuestionBankRecord);
            return;
        }

        LambdaUpdateWrapper<UserFirstCategoryQuestionBank> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserUserId, userId);
        lambdaUpdateWrapper.eq(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserQuestionBankId, questionBank.getQuestionBankId());
        lambdaUpdateWrapper.set(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserAnswerStatus, answerStatus);
        lambdaUpdateWrapper.set(UserFirstCategoryQuestionBank::getFirstCategoryQuestionBankUserUpdateTime, LocalDateTime.now());
        userFirstCategoryQuestionBankMapper.update(null, lambdaUpdateWrapper);
    }

    private String normalizeSingleAnswer(String answer) {
        if (answer == null) {
            return "";
        }
        return answer.trim().toUpperCase();
    }

    private String normalizeMultiAnswer(String answer) {
        if (answer == null || answer.trim().isEmpty()) {
            return "";
        }

        String[] answerArray = answer.split(",");
        List<String> answerList = new ArrayList<>();
        for (String answerItem : answerArray) {
            if (answerItem != null && !answerItem.trim().isEmpty()) {
                answerList.add(answerItem.trim().toUpperCase());
            }
        }

        Collections.sort(answerList);
        return String.join(",", answerList);
    }

    private String normalizeMultiAnswer(List<String> userAnswers) {
        if (userAnswers == null || userAnswers.isEmpty()) {
            return "";
        }

        List<String> answerList = new ArrayList<>();
        for (String userAnswer : userAnswers) {
            if (userAnswer != null && !userAnswer.trim().isEmpty()) {
                answerList.add(userAnswer.trim().toUpperCase());
            }
        }

        Collections.sort(answerList);
        return String.join(",", answerList);
    }

    private boolean hasAnyAnswer(List<String> userAnswers) {
        if (userAnswers == null || userAnswers.isEmpty()) {
            return false;
        }

        for (String userAnswer : userAnswers) {
            if (userAnswer != null && !userAnswer.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
