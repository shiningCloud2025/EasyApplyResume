package com.zyh.easyapplyresume.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.zyh.easyapplyresume.mapper.mysql.user.UserMapper;
import com.zyh.easyapplyresume.model.form.user.UserUpdateForm;
import com.zyh.easyapplyresume.model.pojo.user.User;
import com.zyh.easyapplyresume.model.vo.user.UserInfoVO;
import com.zyh.easyapplyresume.service.user.UserService;
import com.zyh.easyapplyresume.utils.uservalidator.UserUpdateValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shiningCloud2025
 */
@Slf4j
@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void updateUser(UserUpdateForm userUpdateForm) {
        try{
            log.info("用户更新信息开始");
            UserUpdateValidator.validateForUpdate(userUpdateForm);
            userMapper.updateById(BeanUtil.copyProperties(userUpdateForm, User.class));
            log.info("用户更新信息成功");
        }catch (Exception e){
            log.error("用户更新信息失败");
        }


    }

    @Override
    public UserInfoVO getUserByUserId(String userId) {
        try{
            log.info("根据用户id查询用户信息开始");
            return BeanUtil.copyProperties(userMapper.selectById(userId), UserInfoVO.class);
        }catch (Exception e){
            log.error("根据用户id查询用户信息失败");
        }
        return null;
    }
}
