package com.zyh.easyapplyresume.mapper.mysql.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.easyapplyresume.model.pojo.user.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author shiningCloud2025
 */
public interface UserMapper extends BaseMapper<User> {
    // 根据账号/邮箱/手机号查找
    User findByAccountOrPhoneOrEmail(@Param("accountOrPhoneOrEmail") String accountOrPhoneOrEmail);

}
