package com.zyh.easyapplyresume.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.zyh.easyapplyresume.bean.locationenum.CityEnum;
import com.zyh.easyapplyresume.bean.locationenum.ProvinceEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.admin.RecruitPositionMapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UniversityMapMapper;
import com.zyh.easyapplyresume.mapper.mysql.user.UserMapper;
import com.zyh.easyapplyresume.model.form.user.UserUpdateForm;
import com.zyh.easyapplyresume.model.pojo.user.UniversityMap;
import com.zyh.easyapplyresume.model.pojo.user.User;
import com.zyh.easyapplyresume.model.vo.user.UserInfoVO;
import com.zyh.easyapplyresume.service.user.UserService;
import com.zyh.easyapplyresume.utils.uservalidator.UserUpdateValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private UniversityMapMapper universityMapMapper;

    @Autowired
    private RecruitPositionMapper recruitPositionMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void updateUser(UserUpdateForm userUpdateForm) {
        try{
            log.info("用户更新信息开始");
            UserUpdateValidator.validateForUpdate(userUpdateForm);
            User user = BeanUtil.copyProperties(userUpdateForm, User.class);
            user.setUserRecruitLocationDetail(ProvinceEnum.getById(user.getUserRecruitLocationFirst()).getName()+
                    CityEnum.getById(user.getUserRecruitLocationSecond()).getName());
            user.setUserPassword(passwordEncoder.encode(userUpdateForm.getUserPassword()));
            userMapper.updateById(user);
            log.info("用户更新信息成功");
        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.error("用户更新信息失败", e);
            throw new RuntimeException("用户更新信息失败");
        }
    }

    @Override
    public UserInfoVO getUserByUserId(String userId) {
        try{
            log.info("根据用户id查询用户信息开始");
            UserInfoVO userInfoVO = BeanUtil.copyProperties(userMapper.selectById(userId), UserInfoVO.class);
            userInfoVO.setUserRecruitLocationFirstName(ProvinceEnum.getById(userInfoVO.getUserRecruitLocationFirst()).getName());
            userInfoVO.setUserRecruitLocationSecondName(CityEnum.getById(userInfoVO.getUserRecruitLocationSecond()).getName());
            userInfoVO.setUserUniversityCodeName(universityMapMapper.selectById(userInfoVO.getUserUniversityCode()).getUniversityMapName());
            userInfoVO.setUserDreamPositionName(recruitPositionMapper.selectById(userInfoVO.getUserDreamPosition()).getRecruitPositionName());
            return userInfoVO;

        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.error("根据用户id查询用户信息失败", e);
            throw new RuntimeException("根据用户id查询用户信息失败");
        }
    }
}
