package com.zyh.easyapplyresume.mapper.mysql.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.model.pojo.admin.AdminQuestionBank;
import com.zyh.easyapplyresume.model.query.user.UserQuestionBankQuery;
import com.zyh.easyapplyresume.model.vo.user.UserQuestionBankPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户端题库题目Mapper
 * @author shiningCloud2025
 */
public interface UserQuestionBankMapper extends BaseMapper<AdminQuestionBank> {

    /**
     * 分页查询用户端题库题目
     */
    IPage<UserQuestionBankPageVO> selectQuestionBankByPage(
            Page<UserQuestionBankPageVO> page,
            @Param("userId") Integer userId,
            @Param("query") UserQuestionBankQuery query
    );
}
