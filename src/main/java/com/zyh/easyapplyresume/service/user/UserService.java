package com.zyh.easyapplyresume.service.user;

import com.zyh.easyapplyresume.model.form.user.UserUpdateForm;
import com.zyh.easyapplyresume.model.vo.user.UserInfoVO;
/**
 * @author shiningCloud2025
 */
public interface UserService {
    /**
     * 修改用户
     */
    public void updateUser(UserUpdateForm userUpdateForm);

    /**
     * 根据id
     */
    public UserInfoVO getUserByUserId(String userId);
}
