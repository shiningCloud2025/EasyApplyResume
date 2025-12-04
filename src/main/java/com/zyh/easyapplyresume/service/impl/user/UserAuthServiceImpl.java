package com.zyh.easyapplyresume.service.impl.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.UserCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.user.UserMapper;
import com.zyh.easyapplyresume.model.form.user.FormalRegisterForm;
import com.zyh.easyapplyresume.model.pojo.user.User;
import com.zyh.easyapplyresume.service.user.UserAuthService;
import com.zyh.easyapplyresume.service.user.UserLoginAndRegisterEmailVerifyService;
import com.zyh.easyapplyresume.service.user.UserSmsService;
import com.zyh.easyapplyresume.utils.jwt.JwtUtil;
import com.zyh.easyapplyresume.utils.uservalidator.FormalRegisterValidator;
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

    /**
     * 账号/邮箱/手机号+密码登录
     * @param accountOrPhoneOrEmail
     * @param password
     * @return
     */
    @Override
    public String formalLogin(String accountOrPhoneOrEmail, String password) {
        User user = userMapper.findByAccountOrPhoneOrEmail(accountOrPhoneOrEmail);
        if (user== null){
            throw new BusException(UserCodeEnum.ACCOUNT_OR_PASSWORD_ERROR);
        }
        if (!passwordEncoder.matches(password, user.getUserPassword())){
            throw new BusException(UserCodeEnum.ACCOUNT_OR_PASSWORD_ERROR);
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
     * @param phone
     * @param messageCode
     * @return
     */
    @Override
    public String phoneLogin(String phone, String messageCode) {
        userSmsService.verifyCode(phone, messageCode);
        User user = userMapper.findByAccountOrPhoneOrEmail(phone);
        if (user== null){
            throw new BusException(UserCodeEnum.NO_REGISTER_ERROR);
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
     * @param email
     * @param messageCode
     * @return
     */
    @Override
    public String emailLogin(String email, String messageCode) {
        userLoginAndRegisterEmailVerifyService.verifyCode(email, messageCode);
        User user = userMapper.findByAccountOrPhoneOrEmail(email);
        if (user== null){
            throw new BusException(UserCodeEnum.NO_REGISTER_ERROR);
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
        userSmsService.verifyCode(formalRegisterForm.getUserPhone(), formalRegisterForm.getPhoneMessageCode());
        userLoginAndRegisterEmailVerifyService.verifyCode(formalRegisterForm.getUserEmail(), formalRegisterForm.getEmailMessageCode());

        FormalRegisterValidator.validateForRegister(formalRegisterForm);
        try {
            User user = new User();
            formalRegisterForm.setUserCreateTime(new Date());
            formalRegisterForm.setUserLoginTime(new Date());
            BeanUtils.copyProperties(formalRegisterForm, user);
            userMapper.insert(user);
            String token = jwtUtil.generateToken(user.getUserId(), user.getUserUsername(), "user", jwtSecret, jwtExpiration);
            String redisKey = "user:token:" + user.getUserId();
            stringRedisTemplate.opsForValue().set(redisKey, token, jwtExpiration, TimeUnit.MILLISECONDS);
            return token;
        } catch (DataAccessException e) {
            throw resolveDbException(e);
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

    /**
     * 私有辅助方法：解析数据库异常并转换为业务异常
     */
    private BusException resolveDbException(Exception e) {
        String errorMsg = e.getMessage();
        // 1. 处理唯一约束冲突（DuplicateKeyException 或 SQLIntegrityConstraintViolationException）
        if (errorMsg.contains("Duplicate entry") || e instanceof org.springframework.dao.DuplicateKeyException) {
            if (errorMsg.contains("user_account")|| errorMsg.contains("user_user_pk")) {
                // 匹配用户账号字段或账号唯一索引
                return new BusException(UserCodeEnum.USER_ACCOUNT_DUPLICATE);
            } else if (errorMsg.contains("user_phone")|| errorMsg.contains("user_user_pk_2")) {
                // 匹配用户手机号字段或手机号唯一索引
                return new BusException(UserCodeEnum.USER_PHONE_DUPLICATE);
            } else if (errorMsg.contains("user_email")|| errorMsg.contains("user_user_pk_3")) {
                // 匹配用户邮箱字段或邮箱唯一索引
                return new BusException(UserCodeEnum.USER_EMAIL_DUPLICATE);
            }
        }
        return new BusException(UserCodeEnum.DB_EXCEPTION_TRANSFORM_FAIL_EXCEPTION);
    }
}
