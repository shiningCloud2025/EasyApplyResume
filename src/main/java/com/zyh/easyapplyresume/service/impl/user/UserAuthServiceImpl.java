package com.zyh.easyapplyresume.service.impl.user;

import cn.hutool.core.date.DateUtil;
import com.zyh.easyapplyresume.bean.locationenum.CityEnum;
import com.zyh.easyapplyresume.bean.locationenum.ProvinceEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.UserCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.user.UserMapper;
import com.zyh.easyapplyresume.model.form.user.EmailLoginForm;
import com.zyh.easyapplyresume.model.form.user.FormalLoginForm;
import com.zyh.easyapplyresume.model.form.user.FormalRegisterForm;
import com.zyh.easyapplyresume.model.form.user.PhoneLoginForm;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDailyVisitNum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorUserDailyVisitNum;
import com.zyh.easyapplyresume.model.pojo.user.User;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorUserDailyVisitNumService;
import com.zyh.easyapplyresume.service.user.UserAuthService;
import com.zyh.easyapplyresume.service.user.UserLoginAndRegisterEmailVerifyService;
import com.zyh.easyapplyresume.service.user.UserSmsService;
import com.zyh.easyapplyresume.utils.jwt.JwtUtil;
import com.zyh.easyapplyresume.utils.uservalidator.FormalRegisterValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author shiningCloud2025
 */
@Transactional
@Service
@ServiceLog
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${jwt.user.secret}")
    private String jwtSecret;

    @Value("${jwt.user.expiration}")
    private long jwtExpiration;

    @Autowired
    private UserSmsService userSmsService;

    @Autowired
    private UserLoginAndRegisterEmailVerifyService userLoginAndRegisterEmailVerifyService;

    @Autowired
    private AdmonitorUserDailyVisitNumService admonitorUserDailyVisitNumService;

    /**
     * 普通登录(账号/手机号/邮箱号)
     * @param formalLoginForm
     * @return
     */
    @Override
    public String formalLogin(FormalLoginForm formalLoginForm) {
        User user = userMapper.findByAccountOrPhoneOrEmail(formalLoginForm.getAccountOrPhoneOrEmail());
        if (user== null){
            throw new BusException(UserCodeEnum.ACCOUNT_OR_PASSWORD_ERROR);
        }
        if (!passwordEncoder.matches(formalLoginForm.getPassword(), user.getUserPassword())){
            throw new BusException(UserCodeEnum.ACCOUNT_OR_PASSWORD_ERROR);
        }
        if (user.getUserLoginTime() == null || !DateUtil.isSameDay(user.getUserLoginTime(),new Date())){
            // TODO:这里不能用构造 因为构造没有自动类型转换
            AdmonitorUserDailyVisitNum admonitorUserDailyVisitNum = new AdmonitorUserDailyVisitNum();
            admonitorUserDailyVisitNum.setUserDailyVisitNumAdminId(user.getUserId());
            admonitorUserDailyVisitNum.setUserDailyVisitNumVisitTime(new Date());
            admonitorUserDailyVisitNumService.addAdmonitorUserDailyVisitNum(admonitorUserDailyVisitNum);

        }
        user.setUserLoginTime(new Date());
        userMapper.updateById(user);
        String token = jwtUtil.generateToken(user.getUserId(), user.getUserUsername(), "user", jwtSecret, jwtExpiration);
        String redisKey = "user:token:" + user.getUserId();
        stringRedisTemplate.opsForValue().set(redisKey, token, jwtExpiration, TimeUnit.MILLISECONDS);
        return token;
    }

    /**
     * 手机号+短信登录
     * @param phoneLoginForm
     * @return
     */
    @Override
    public String phoneLogin(PhoneLoginForm phoneLoginForm) {
        userSmsService.verifyCode(phoneLoginForm.getPhone(), phoneLoginForm.getMessageCode());
        User user = userMapper.findByAccountOrPhoneOrEmail(phoneLoginForm.getPhone());
        if (user== null){
            throw new BusException(UserCodeEnum.NO_REGISTER_ERROR);
        }
        if (user.getUserLoginTime() == null || !DateUtil.isSameDay(user.getUserLoginTime(),new Date())){
            // TODO:这里不能用构造 因为构造没有自动类型转换
            AdmonitorUserDailyVisitNum admonitorUserDailyVisitNum = new AdmonitorUserDailyVisitNum();
            admonitorUserDailyVisitNum.setUserDailyVisitNumAdminId(user.getUserId());
            admonitorUserDailyVisitNum.setUserDailyVisitNumVisitTime(new Date());
            admonitorUserDailyVisitNumService.addAdmonitorUserDailyVisitNum(admonitorUserDailyVisitNum);

        }
        user.setUserLoginTime(new Date());
        userMapper.updateById(user);
        String token = jwtUtil.generateToken(user.getUserId(), user.getUserUsername(), "user", jwtSecret, jwtExpiration);
        String redisKey = "user:token:" + user.getUserId();
        stringRedisTemplate.opsForValue().set(redisKey, token, jwtExpiration, TimeUnit.MILLISECONDS);
        return token;
    }

    /**
     * 邮箱+验证码登录
     * @param emailLoginForm
     * @return
     */
    @Override
    public String emailLogin(EmailLoginForm emailLoginForm) {
        userLoginAndRegisterEmailVerifyService.verifyCode(emailLoginForm.getEmail(), emailLoginForm.getMessageCode());
        User user = userMapper.findByAccountOrPhoneOrEmail(emailLoginForm.getEmail());
        if (user== null){
            throw new BusException(UserCodeEnum.NO_REGISTER_ERROR);
        }
        if (user.getUserLoginTime() == null || !DateUtil.isSameDay(user.getUserLoginTime(),new Date())){
            // TODO:这里不能用构造 因为构造没有自动类型转换
            AdmonitorUserDailyVisitNum admonitorUserDailyVisitNum = new AdmonitorUserDailyVisitNum();
            admonitorUserDailyVisitNum.setUserDailyVisitNumAdminId(user.getUserId());
            admonitorUserDailyVisitNum.setUserDailyVisitNumVisitTime(new Date());
            admonitorUserDailyVisitNumService.addAdmonitorUserDailyVisitNum(admonitorUserDailyVisitNum);

        }
        user.setUserLoginTime(new Date());
        userMapper.updateById(user);
        String token = jwtUtil.generateToken(user.getUserId(), user.getUserUsername(), "user", jwtSecret, jwtExpiration);
        String redisKey = "user:token:" + user.getUserId();
        stringRedisTemplate.opsForValue().set(redisKey, token, jwtExpiration, TimeUnit.MILLISECONDS);
        return token;
    }

    @Override
    public String formalRegister(FormalRegisterForm formalRegisterForm) {
        if (formalRegisterForm == null) {
            throw new BusException(UserCodeEnum.USER_REGISTER_FORM_NOT_NULL);
        }
        userSmsService.checkCode(formalRegisterForm.getUserPhone(), formalRegisterForm.getPhoneMessageCode());
        userLoginAndRegisterEmailVerifyService.checkCode(
                formalRegisterForm.getUserEmail(),
                formalRegisterForm.getEmailMessageCode()
        );
        // TODO:用户端注册的头像一定是为空的，就需要用户去修改的时候改变头像，这样也简化了后端的流程
        FormalRegisterValidator.validateForRegister(formalRegisterForm);
        validateUserUnique(formalRegisterForm);
        try {
            User user = new User();
            formalRegisterForm.setUserCreatedTime(new Date());
            formalRegisterForm.setUserLoginTime(new Date());
            BeanUtils.copyProperties(formalRegisterForm, user);
            user.setUserPassword(passwordEncoder.encode(formalRegisterForm.getUserPassword()));
            user.setUserRecruitLocationDetail(ProvinceEnum.getById(user.getUserRecruitLocationFirst()).getName()+
                    CityEnum.getById(user.getUserRecruitLocationSecond()).getName());
            userMapper.insert(user);
            String token = jwtUtil.generateToken(user.getUserId(), user.getUserUsername(), "user", jwtSecret, jwtExpiration);
            // TODO:这两行代码导致了用户注册完，点击登录发送验证码的时候要等待三分钟，去掉这个代码解决这个
            // TODO:由于我改成了注册即登录，也就不需要去掉了，直接留着就可以
            String redisKey = "user:token:" + user.getUserId();
            stringRedisTemplate.opsForValue().set(redisKey, token, jwtExpiration, TimeUnit.MILLISECONDS);
            userSmsService.deleteCode(formalRegisterForm.getUserPhone());
            userLoginAndRegisterEmailVerifyService.deleteCode(formalRegisterForm.getUserEmail());

            return token;
        } catch (BusException e) {
            log.info("注册失败", e);
            throw e;
        }catch (Exception e){
            log.error("注册失败",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generateRandomAccount() {
        String account;
        int maxRetries = 10;
        int retryCount = 0;
        
        do {
            long timestamp = System.currentTimeMillis() % 100000000L;
            int randomNum = (int) (Math.random() * 100);
            account = String.format("%d%02d", timestamp, randomNum);
            
            if (account.length() > 10) {
                account = account.substring(account.length() - 10);
            } else if (account.length() < 7) {
                account = String.format("%07d", Long.parseLong(account));
            }
            
            retryCount++;
            if (retryCount > maxRetries) {
                throw new BusException(UserCodeEnum.GENERATE_ACCOUNT_FAIL);
            }
        } while (userMapper.findByAccountOrPhoneOrEmail(account) != null);
        
        return account;
    }


    @Override
    public void logout(Integer userId) {
        String redisKey = "user:token:" + userId;
        stringRedisTemplate.delete(redisKey);
    }

    private void validateUserUnique(FormalRegisterForm formalRegisterForm) {
        // 1. 校验账号是否重复
        LambdaQueryWrapper<User> accountWrapper = new LambdaQueryWrapper<>();
        accountWrapper.eq(User::getUserAccount, formalRegisterForm.getUserAccount());
        Long accountCount = userMapper.selectCount(accountWrapper);
        if (accountCount != null && accountCount > 0) {
            throw new BusException(UserCodeEnum.USER_ACCOUNT_DUPLICATE);
        }

        // 2. 校验手机号是否重复
        LambdaQueryWrapper<User> phoneWrapper = new LambdaQueryWrapper<>();
        phoneWrapper.eq(User::getUserPhone, formalRegisterForm.getUserPhone());
        Long phoneCount = userMapper.selectCount(phoneWrapper);
        if (phoneCount != null && phoneCount > 0) {
            throw new BusException(UserCodeEnum.USER_PHONE_DUPLICATE);
        }

        // 3. 校验邮箱是否重复
        LambdaQueryWrapper<User> emailWrapper = new LambdaQueryWrapper<>();
        emailWrapper.eq(User::getUserEmail, formalRegisterForm.getUserEmail());
        Long emailCount = userMapper.selectCount(emailWrapper);
        if (emailCount != null && emailCount > 0) {
            throw new BusException(UserCodeEnum.USER_EMAIL_DUPLICATE);
        }
    }
}
