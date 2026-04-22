package com.zyh.easyapplyresume.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.UserCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.user.UserQuestionBankMapper;
import com.zyh.easyapplyresume.model.pojo.admin.AdminQuestionBank;
import com.zyh.easyapplyresume.model.query.user.UserQuestionBankQuery;
import com.zyh.easyapplyresume.model.vo.user.UserQuestionBankInfoVO;
import com.zyh.easyapplyresume.model.vo.user.UserQuestionBankPageVO;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.user.UserQuestionBankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
