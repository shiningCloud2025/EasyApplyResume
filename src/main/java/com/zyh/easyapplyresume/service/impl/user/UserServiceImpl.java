package com.zyh.easyapplyresume.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.zyh.easyapplyresume.mapper.mysql.user.UserMapper;
import com.zyh.easyapplyresume.model.form.user.UserUpdateForm;
import com.zyh.easyapplyresume.model.pojo.user.User;
import com.zyh.easyapplyresume.model.vo.user.UserInfoVO;
import com.zyh.easyapplyresume.service.user.UserService;
import com.zyh.easyapplyresume.utils.uservalidator.UserUpdateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void updateUser(UserUpdateForm userUpdateForm) {
        UserUpdateValidator.validateForUpdate(userUpdateForm);
        userMapper.updateById(BeanUtil.copyProperties(userUpdateForm, User.class));

    }

    @Override
    public UserInfoVO getUserByUserId(String userId) {
        return BeanUtil.copyProperties(userMapper.selectById(userId), UserInfoVO.class);
    }
}
