package com.zyh.easyapplyresume.service.impl.admin;

import cn.hutool.core.date.DateUtil;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import com.zyh.easyapplyresume.mapper.mysql.admin.AdminMapper;
import com.zyh.easyapplyresume.model.form.admin.AdminFormalLoginForm;
import com.zyh.easyapplyresume.model.form.admin.AdminPhoneLoginForm;
import com.zyh.easyapplyresume.model.form.user.EmailLoginForm;
import com.zyh.easyapplyresume.model.pojo.ad_monitor.AdmonitorAdminDailyVisitNum;
import com.zyh.easyapplyresume.model.pojo.admin.Admin;
import com.zyh.easyapplyresume.selfannotation.service.ServiceLog.ServiceLog;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDailyVisitNumService;
import com.zyh.easyapplyresume.service.admin.AdminAuthService;
import com.zyh.easyapplyresume.service.admin.AdminLoginAndRegisterEmailVerifyService;
import com.zyh.easyapplyresume.service.admin.AdminSmsService;
import com.zyh.easyapplyresume.service.user.UserLoginAndRegisterEmailVerifyService;
import com.zyh.easyapplyresume.service.user.UserSmsService;
import com.zyh.easyapplyresume.utils.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class AdminAuthServiceImpl implements AdminAuthService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${jwt.admin.secret}")
    private String jwtSecret;

    @Value("${jwt.admin.expiration}")
    private long jwtExpiration;

    @Autowired
    private AdminSmsService adminSmsService;

    @Autowired
    private AdminLoginAndRegisterEmailVerifyService adminLoginAndRegisterEmailVerifyService;

    @Autowired
    private AdmonitorAdminDailyVisitNumService admonitorAdminDailyVisitNumService;

    /**
     * 普通登录(账号/手机号/邮箱号+密码)
     */
    @Override
    public String formalLogin(AdminFormalLoginForm formalLoginForm) {
        Admin admin = adminMapper.findByAccountOrPhoneOrEmail(formalLoginForm.getAccountOrPhoneOrEmail());
        if (admin== null){
            throw new BusException(AdminCodeEnum.ACCOUNT_OR_PASSWORD_ERROR);
        }
        if (!passwordEncoder.matches(formalLoginForm.getPassword(), admin.getAdminPassword())){
            throw new BusException(AdminCodeEnum.ACCOUNT_OR_PASSWORD_ERROR);
        }
        if (admin.getAdminLoginTime() == null || !DateUtil.isSameDay(admin.getAdminLoginTime(),new Date())){
            // TODO:这里不能用构造 因为构造没有自动类型转换
            AdmonitorAdminDailyVisitNum admonitorAdminDailyVisitNum = new AdmonitorAdminDailyVisitNum();
            admonitorAdminDailyVisitNum.setAdminDailyVisitNumAdminId(admin.getAdminId());
            admonitorAdminDailyVisitNum.setAdminDailyVisitNumVisitTime(new Date());
            admonitorAdminDailyVisitNumService.addAdmonitorAdminDailyVisitNum(admonitorAdminDailyVisitNum);

        }
        admin.setAdminLoginTime(new Date());
        adminMapper.updateById(admin);
        String token = jwtUtil.generateToken(admin.getAdminId(),admin.getAdminUsername(),"admin",jwtSecret,jwtExpiration);
        String redisKey = "admin:token:" + admin.getAdminId();
        stringRedisTemplate.opsForValue().set(redisKey, token, jwtExpiration, TimeUnit.MILLISECONDS);
        return token;

    }

    /**
     * 手机号+短信登录
     * @param phoneLoginForm
     * @return
     */
    @Override
    public String phoneLogin(AdminPhoneLoginForm phoneLoginForm) {
        adminSmsService.verifyCode(phoneLoginForm.getPhone(), phoneLoginForm.getMessageCode());
        Admin admin = adminMapper.findByAccountOrPhoneOrEmail(phoneLoginForm.getPhone());
        if (admin== null){
            throw new BusException(AdminCodeEnum.NO_REGISTER_ERROR);
        }
        if (admin.getAdminLoginTime() == null || !DateUtil.isSameDay(admin.getAdminLoginTime(),new Date())){
            // TODO:这里不能用构造 因为构造没有自动类型转换
            AdmonitorAdminDailyVisitNum admonitorAdminDailyVisitNum = new AdmonitorAdminDailyVisitNum();
            admonitorAdminDailyVisitNum.setAdminDailyVisitNumAdminId(admin.getAdminId());
            admonitorAdminDailyVisitNum.setAdminDailyVisitNumVisitTime(new Date());
            admonitorAdminDailyVisitNumService.addAdmonitorAdminDailyVisitNum(admonitorAdminDailyVisitNum);

        }
        admin.setAdminLoginTime(new Date());
        adminMapper.updateById(admin);
        String token = jwtUtil.generateToken(admin.getAdminId(),admin.getAdminUsername(),"admin",jwtSecret,jwtExpiration);
        String redisKey = "admin:token:" + admin.getAdminId();
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
        adminLoginAndRegisterEmailVerifyService.verifyCode(emailLoginForm.getEmail(), emailLoginForm.getMessageCode());
        Admin admin = adminMapper.findByAccountOrPhoneOrEmail(emailLoginForm.getEmail());
        if (admin== null){
            throw new BusException(AdminCodeEnum.NO_REGISTER_ERROR);
        }
        if (admin.getAdminLoginTime() == null || !DateUtil.isSameDay(admin.getAdminLoginTime(),new Date())){
            // TODO:这里不能用构造 因为构造没有自动类型转换
            AdmonitorAdminDailyVisitNum admonitorAdminDailyVisitNum = new AdmonitorAdminDailyVisitNum();
            admonitorAdminDailyVisitNum.setAdminDailyVisitNumAdminId(admin.getAdminId());
            admonitorAdminDailyVisitNum.setAdminDailyVisitNumVisitTime(new Date());
            admonitorAdminDailyVisitNumService.addAdmonitorAdminDailyVisitNum(admonitorAdminDailyVisitNum);

        }
        admin.setAdminLoginTime(new Date());
        adminMapper.updateById(admin);
        String token = jwtUtil.generateToken(admin.getAdminId(),admin.getAdminUsername(),"admin",jwtSecret,jwtExpiration);
        String redisKey = "admin:token:" + admin.getAdminId();
        stringRedisTemplate.opsForValue().set(redisKey, token, jwtExpiration, TimeUnit.MILLISECONDS);
        return token;

    }


}
