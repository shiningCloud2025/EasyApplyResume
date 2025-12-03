package com.zyh.easyapplyresume.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.UserCodeEnum;
import com.zyh.easyapplyresume.mapper.mysql.user.UserMapper;
import com.zyh.easyapplyresume.model.form.user.EmailRegisterForm;
import com.zyh.easyapplyresume.model.form.user.FormalRegisterForm;
import com.zyh.easyapplyresume.model.form.user.PhoneRegisterForm;
import com.zyh.easyapplyresume.model.pojo.user.User;
import com.zyh.easyapplyresume.service.user.UserAuthService;
import com.zyh.easyapplyresume.service.user.UserLoginAndRegisterEmailVerifyService;
import com.zyh.easyapplyresume.service.user.UserSmsService;
import com.zyh.easyapplyresume.utils.jwt.JwtUtil;
import com.zyh.easyapplyresume.utils.uservalidator.FormalRegisterValidator;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public boolean formalLogin(String accountOrPhoneOrEmail, String password) {
        User user = userMapper.findByAccountOrPhoneOrEmail(accountOrPhoneOrEmail);
        if (user== null){
            throw new BusException(UserCodeEnum.ACCOUNT_OR_PASSWORD_ERROR);
        }
        if (!passwordEncoder.matches(password, user.getUserPassword())){
            throw new BusException(UserCodeEnum.ACCOUNT_OR_PASSWORD_ERROR);
        }
        String token = jwtUtil.generateToken(user.getUserId(), user.getUserUsername(), "user", jwtSecret, jwtExpiration);
        String redisKey = "user:token:" + user.getUserId();
        stringRedisTemplate.opsForValue().set(redisKey, token, jwtExpiration, TimeUnit.MILLISECONDS);
        return true;
    }

    /**
     * 手机号+短信登录
     * @param phone
     * @param messageCode
     * @return
     */
    @Override
    public boolean phoneLogin(String phone, String messageCode) {
        userSmsService.verifyCode(phone, messageCode);
        User user = userMapper.findByAccountOrPhoneOrEmail(phone);
        if (user== null){
            throw new BusException(UserCodeEnum.NO_REGISTER_ERROR);
        }
        String token = jwtUtil.generateToken(user.getUserId(), user.getUserUsername(), "user", jwtSecret, jwtExpiration);
        String redisKey = "user:token:" + user.getUserId();
        stringRedisTemplate.opsForValue().set(redisKey, token, jwtExpiration, TimeUnit.MILLISECONDS);
        return true;
    }

    /**
     * 邮箱+验证码登录
     * @param email
     * @param messageCode
     * @return
     */
    @Override
    public boolean emailLogin(String email, String messageCode) {
        userLoginAndRegisterEmailVerifyService.verifyCode(email, messageCode);
        User user = userMapper.findByAccountOrPhoneOrEmail(email);
        if (user== null){
            throw new BusException(UserCodeEnum.NO_REGISTER_ERROR);
        }
        String token = jwtUtil.generateToken(user.getUserId(), user.getUserUsername(), "user", jwtSecret, jwtExpiration);
        String redisKey = "user:token:" + user.getUserId();
        stringRedisTemplate.opsForValue().set(redisKey, token, jwtExpiration, TimeUnit.MILLISECONDS);
        return true;
    }

    @Override
    public boolean formalRegister(FormalRegisterForm formalRegisterForm) {
        FormalRegisterValidator.validateForRegister(formalRegisterForm);




        return false;
    }

    @Override
    public boolean phoneRegister(PhoneRegisterForm phoneRegisterForm) {
        return false;
    }

    @Override
    public boolean emailRegister(EmailRegisterForm emailRegisterForm) {
        return false;
    }

    @Override
    public boolean logout() {
        return false;
    }
}
