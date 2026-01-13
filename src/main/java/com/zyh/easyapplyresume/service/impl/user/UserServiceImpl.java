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
import com.zyh.easyapplyresume.qiniuoss.OssService;
import com.zyh.easyapplyresume.qiniuoss.OssSystemTypeEnum;
import com.zyh.easyapplyresume.qiniuoss.OssUserBusinessTypeEnum;
import com.zyh.easyapplyresume.redis.constant.common.UserCacheKey;
import com.zyh.easyapplyresume.redis.util.RedisCacheUtil;
import com.zyh.easyapplyresume.service.user.UserService;
import com.zyh.easyapplyresume.utils.uservalidator.UserUpdateValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private OssService ossService;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Override
    public void updateUser(UserUpdateForm userUpdateForm) {
        try{
            log.info("用户更新信息开始");

            if (userUpdateForm.getUserImage().equals("https://ts4.tc.mm.bing.net/th/id/OIP-C.sPOk8TwPGgwtB2SU6ngYUgAAAA?rs=1&pid=ImgDetMain&o=7&rm=3")){
                // 如果用户的头像地址等于默认，那么不用任何处理直接保存就可以了
            }else{
                // 说明用户传了新的
                List<String> strings = ossService.listFilesByOwner(OssSystemTypeEnum.USER, OssUserBusinessTypeEnum.USER_HEAD_IMG, userUpdateForm.getUserId(), false);
                if (strings.isEmpty()){
                    // 说明是第一次传新的，不用处理
                }else {
                    // 说明不是第一次传新的，要处理不等于当前的
                    for (String string : strings){
                        if (!string.equals(userUpdateForm.getUserImage())){
                            ossService.deleteByUrl(string, false);
                        }
                    }
                }

            }


            UserUpdateValidator.validateForUpdate(userUpdateForm);
            User user = BeanUtil.copyProperties(userUpdateForm, User.class);
            user.setUserRecruitLocationDetail(ProvinceEnum.getById(user.getUserRecruitLocationFirst()).getName()+
                    CityEnum.getById(user.getUserRecruitLocationSecond()).getName());
            user.setUserPassword(passwordEncoder.encode(userUpdateForm.getUserPassword()));
            userMapper.updateById(user);
            
            redisCacheUtil.delete(UserCacheKey.GET_PREFIX + "_" + userUpdateForm.getUserId());
            
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
            String cacheKey = UserCacheKey.GET_PREFIX + "_" + userId;
            
            Object cached = redisCacheUtil.get(cacheKey);
            if (cached != null) {
                log.info("从缓存获取用户信息成功");
                return (UserInfoVO) cached;
            }
            
            log.info("根据用户id查询用户信息开始");
            UserInfoVO userInfoVO = BeanUtil.copyProperties(userMapper.selectById(userId), UserInfoVO.class);
            userInfoVO.setUserRecruitLocationFirstName(ProvinceEnum.getById(userInfoVO.getUserRecruitLocationFirst()).getName());
            userInfoVO.setUserRecruitLocationSecondName(CityEnum.getById(userInfoVO.getUserRecruitLocationSecond()).getName());
            userInfoVO.setUserUniversityCodeName(universityMapMapper.selectById(userInfoVO.getUserUniversityCode()).getUniversityMapName());
            userInfoVO.setUserDreamPositionName(recruitPositionMapper.selectById(userInfoVO.getUserDreamPosition()).getRecruitPositionName());
            
            redisCacheUtil.set(cacheKey, userInfoVO, UserCacheKey.GET_TTL, TimeUnit.MINUTES);
            
            return userInfoVO;

        }catch (BusException e){
            throw e;
        }catch (Exception e){
            log.error("根据用户id查询用户信息失败", e);
            throw new RuntimeException("根据用户id查询用户信息失败");
        }
    }
}
